package edu.twister.malik.services.user;

import java.util.regex.Pattern;
import edu.twister.malik.services.user.UserException;
import edu.twister.malik.services.ServiceException;
import java.sql.*;

public class UserDescription {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Connection connection;

    UserDescription
        (String firstname, String lastname, 
         String email, String username,
         Connection cnx) {
            this.firstname = firstname.replaceAll("\\s+","");
            this.lastname = lastname.replaceAll("\\s+","");
            this.email = email.replaceAll("\\s+","");
            this.username = username.replaceAll("\\s+","");
            this.connection = cnx;
        }

    public boolean 
        verify() throws ServiceException {
            boolean trust = true;
            trust &= trusted_names(firstname, lastname);
            if ( ! trust ) 
                throw new UserException(UserException._INVALID_NAME);
            trust &= valid_email(email);
            if ( ! trust )
                throw new UserException(UserException._INVALID_EMAIL);
            try {
                trust &= check_already(firstname, lastname, connection);
                if ( ! trust )
                    throw new UserException(UserException._NAME_ALREADY_REGISTERED);
                trust &= check_already(username, connection);
                if ( ! trust )
                    throw new UserException(UserException._USERNAME_ALREADY_USED);
                return trust;
            }
            catch (SQLException e) {
                throw new ServiceException(e);
            }

        }

    private static final Pattern 
        TRUSTED_NAME_PATTERN = 
        Pattern.compile( "^\\p{Alpha}{2,20}$", 
                Pattern.CASE_INSENSITIVE);

    private static final Pattern
        VALID_EMAIL_PATTERN = 
        Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 
                Pattern.CASE_INSENSITIVE);

    private static 
        boolean trusted_names (String firstname, String lastname) {
            return 
                TRUSTED_NAME_PATTERN.matcher(firstname).matches() &&
                TRUSTED_NAME_PATTERN.matcher(lastname).matches();
        }

    private static 
        boolean valid_email (String email) {
            return
                VALID_EMAIL_PATTERN.matcher(email).matches();
        }

    public static final String
        sql_exists_username = 
        "SELECT username FROM users "+
        "WHERE username = ?;";

    public static final String
        sql_exists_email = 
        "SELECT email FROM users " + 
        "WHERE email = ?;";

    public static final String
        sql_exists_names =
        "SELECT firstname, lastname FROM users " +
        "WHERE firstname = ? AND lastname = ?;";

    private static boolean 
        check_already 
        (String firstname, String lastname, Connection cnx) 
            throws SQLException {
            PreparedStatement stmt = 
                cnx.prepareStatement(sql_exists_names);
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            return ! (stmt.executeQuery().first());
        }

    private static boolean
        check_already 
        (String username, Connection cnx)
            throws SQLException {
            PreparedStatement stmt =
                cnx.prepareStatement(sql_exists_username);
            stmt.setString(1, username);
            return ! (stmt.executeQuery().first());
        }

}
