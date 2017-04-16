package edu.twister.malik.services.user;

import java.sql.*;

import edu.twister.malik.services.ServiceException;

public class SessionManager {

    private static final String
        sql_new_session =
        "INSERT INTO sessions " +
        "(username, userkey, useriid) " +
        "VALUES (?, MD5(CONCAT(?,NOW())), ?);";
    private static final String
        sql_valid_session =
        "SELECT userkey FROM sessions " +
        "WHERE username = ? AND userkey IS NOT NULL;";
    private static final String
        sql_get_last_session =
        "SELECT userkey FROM sessions " +
        "WHERE username = ? AND useriid = ? " + 
        "AND logoutstmp IS NULL;";
    private static final String
        sql_end_of_session =
        "UPDATE sessions " +
        "SET userkey = NULL," +
        "    logoutstmp = NOW() " +
        "WHERE userkey = ?;";

    private static final String
        sql_get_useriid =
        "SELECT iid FROM users " +
        "WHERE username = ?;";
    
    
    private static String
        new_session_instance
        (int useriid, String username, Connection cnx) 
            throws ServiceException, SQLException {
            ResultSet rs = null;
            //Get the last session if it exists 
            PreparedStatement getlastStmt =
                cnx.prepareStatement(sql_get_last_session);
            getlastStmt.setString(1, username);
            getlastStmt.setInt(2, useriid);
            rs = getlastStmt.executeQuery();
            if ( rs.next() ) {
                String userkey = rs.getString(1);
                //...and close it (if it exists)
                close_session_instance(userkey, cnx);
            }
            //Inserts (new) session entry
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
                throw new SessionException(
                        SessionException._NO_NEW_SESSION_ENTRY);
        }

    /**
     * return hash of username, date and time which stands for the userkey that
     * designate the session and inserts the corresponding entry in sessions
     * sql table.
     * @param username username
     * @param cnx sql connexion in  use
     * @return userkey
     */
    public static String
        newSession 
        (String username, Connection cnx) 
            throws ServiceException {
            /* newSession(username, cnx) -- static method
             * hash of username, date and time makes a userkey inserted in
             * sessions sql table and return the userkey
             */
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
                return new_session_instance(useriid, username, cnx);
            }
            catch (SQLException e) {
                throw new ServiceException(e);
            }
        }

    /**
     * static method to close a session providing a userkey and a connection
     * @param userkey userkey to provide
     * @param cnx sql connexion in use
     */
    public static void
        close_session_instance
        (String userkey, Connection cnx) 
        throws ServiceException, SQLException {
            PreparedStatement closeStmt =
                cnx.prepareStatement(sql_end_of_session);
            closeStmt.setString(1, userkey);
            if ( closeStmt.executeUpdate() < 1 )
                throw new SessionException(SessionException._NO_SESSION_TO_CLOSE);
        }

}
