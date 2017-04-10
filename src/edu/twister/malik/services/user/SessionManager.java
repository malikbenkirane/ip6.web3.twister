package edu.twister.malik.services.user;

import java.sql.*;

import edu.twister.malik.services.ServiceException;

public class SessionManager {

    private static final String
        sql_new_session =
        "INSERT INTO sessions " +
        "(username, userkey, iid) " +
        "VALUES (?, MD5(?), ?);";
    private static final String
        sql_valid_session =
        "SELECT userkey FROM sessions " +
        "WHERE username = ? AND userkey IS NOT NULL";
    private static final String
        sql_get_useriid =
        "SELECT iid FROM users " +
        "WHERE username = ?;";
    private static final String
        sql_end_of_session =
        "UPDATE sessions " +
        "SET userkey = NULL " +
        "SET logoutstmp = NOW() " +
        "WHERE userkey = ?;";
    
    public static String
        newSession 
        (String username, Connection cnx) 
            throws ServiceException {
            try {
                PreparedStatement uidStmt = 
                    cnx.prepareStatement(sql_get_useriid);
                uidStmt.setString(1, username);
                //Retrieves useriid
                int useriid;
                ResultSet rs = null;
                rs = uidStmt.executeQuery();
                if ( rs.next() ) 
                    useriid = rs.getInt(1);
                else 
                    throw new
                        SessionException(SessionException._FORBIDDEN_USERNAME);
                //Inserts session entry
                PreparedStatement newSessionStmt =
                    cnx.prepareStatement(sql_new_session);
                newSessionStmt.setString(1, username);
                newSessionStmt.setString(2, username);
                newSessionStmt.setInt(3, useriid);
                newSessionStmt.executeUpdate();
                //Get userkey from sessions
                rs = null;
                PreparedStatement verifySessionStmt = 
                    cnx.prepareStatement(sql_valid_session);
                verifySessionStmt.setString(1, username);
                rs = verifySessionStmt.executeQuery();
                if ( rs.next() )
                    return rs.getString(1);
                else
                    throw new SessionException(SessionException._NO_NEW_SESSION_ENTRY);

            }
            catch (SQLException e) {
                throw new ServiceException(e);
            }
        }

}
