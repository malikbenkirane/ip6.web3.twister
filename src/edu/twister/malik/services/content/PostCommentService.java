package edu.twister.malik.services.content;

import java.sql.Connection;
import java.util.Hashtable;

import com.mongodb.DBCollection;
import edu.twister.malik.core.MongoCore;
import edu.twister.malik.core.SQLCore;

import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServletMap;

import edu.twister.malik.services.user.SessionDescription;
import edu.twister.malik.services.user.SessionManager;

import org.json.JSONObject;
import org.json.JSONException;

public class PostCommentService {

    public static JSONObject
        serve(Hashtable<String, String[]> parameters)
            throws ServiceException {
            //preserve
            Hashtable<String, String> specs =
                new Hashtable<String, String>();
            specs.put("userkey", "User session key");
            specs.put("mid", "Message ID");
            specs.put("comment", "Commentary text");
            ServletMap postcServletMap = new ServletMap(specs);
            postcServletMap.setInputs(parameters);
            String userkey = postcServletMap.getNotSafe("userkey");
            String comment = postcServletMap.getNotSafe("comment");
            int mid = postcServletMap.getIntNotSafe("mid");
            //serve
            MongoCore mongo = new MongoCore();
            DBCollection messagesCollection = mongo.getCollection("messages");
            try {
                Connection cnx = SQLCore.getConnection();
                SessionDescription session =
                    SessionManager.retrieve_session(userkey, cnx);
                CommentDescription _comment =
                    new CommentDescription(
                            comment, userkey, 
                            session.getUsername(), session.getUserIID());
                int commentID = 
                    _comment.insertIn(messagesCollection, mid);
                //answer
                JSONObject ans = new JSONObject();
                JSONObject user = new JSONObject();
                ans.put("cid", commentID);
                ans.put("comment", comment);
                ans.put("date", _comment.getDate());
                user.put("iid", session.getUserIID());
                user.put("sid", session.getSessionIID());
                user.put("name", session.getUsername());
                ans.put("user", user);
                return ServiceFactory.standard(ans);
            } catch (JSONException je) {
                return ServiceFactory.error(
                        new ServiceException(ServiceException._SERVICE_CLASS));
            } catch (ServiceException se) {
                return ServiceFactory.error(se);
            }
                            
        }

}
