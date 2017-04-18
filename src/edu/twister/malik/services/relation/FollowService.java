package edu.twister.malik.services.relation;

import edu.twister.malik.services.ServletMap;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.user.SessionManager;
import edu.twister.malik.services.user.SessionDescription;
import edu.twister.malik.core.SQLCore;

import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.relation.RelationException;

import java.util.Hashtable;
import java.sql.*;
import org.json.JSONObject;

public class FollowService {

    public static JSONObject
        serve(Hashtable<String, String[]> parameters)
            throws ServiceException {
            //preserve
            Hashtable<String, String> specs =
                new Hashtable<String, String>();
            specs.put("userkey", "user session key");
            specs.put("useriid", "id of the user to follow");
            ServletMap followServletMap = new ServletMap(specs);
            followServletMap.setInputs(parameters);
            String userkey = followServletMap.getNotSafe("userkey");
            int useriid = followServletMap.getIntNotSafe("useriid");
            //serve
            Connection cnx = SQLCore.getConnection();
            SessionDescription session =
                SessionManager.retrieve_session(userkey, cnx);
            if ( session.getUserIID() == useriid )
                throw new 
                    RelationException(
                            RelationException._FOLLOW_TOO_SHORT);
            PreparedStatement stmt;
            _checkout(cnx, session.getUserIID(), useriid);
            try {
                stmt = cnx.prepareStatement(
                        "INSERT INTO follows " +
                        "(useriid, following) " +
                        "VALUES(?, ?)"
                        );
                stmt.setInt(1, session.getUserIID());
                stmt.setInt(2, useriid);
                if ( stmt.executeUpdate() == 1 )
                    return 
                        ServiceFactory.standard(new JSONObject());
            } 
            catch (SQLException e) {
                throw new
                    ServiceException(e);
            }
            return
                ServiceFactory.error(new
                        ServiceException(
                            "Unable to Follow", 
                            ServiceException._RELATION_CLASS));
        }

    private static void
        _checkout(Connection cnx, int useriid, int following)
            throws ServiceException {
            try {
                ResultSet rs;
                PreparedStatement stmt;
                //does user with iid `followin` exists ?
                stmt = cnx.prepareStatement(
                        "SELECT * FROM users WHERE " +
                        "iid = ?;"
                        );
                stmt.setInt(1, following);
                rs = stmt.executeQuery();
                if ( ! rs.first() )
                    throw new 
                        RelationException(RelationException._NO_SUCH_USER);
                //does user with iid alread being followed ?
                stmt = cnx.prepareStatement(
                        "SELECT * FROM follows WHERE " +
                        "useriid = ? AND following = ?;"
                        );
                stmt.setInt(1, useriid);
                stmt.setInt(2, following);
                rs = stmt.executeQuery();
                if ( rs.first() )
                    throw new
                        RelationException(RelationException._ALREADY_FOLLOWING);
            } catch (SQLException e) {
                throw new ServiceException(e);
            }
        }

}

