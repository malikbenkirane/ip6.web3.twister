package edu.twister.malik.services.user;

import edu.twister.malik.core.SQLCore;
import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.user.UserException;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServletMap;
import edu.twister.malik.services.user.SessionManager;

import java.util.Hashtable;
import java.sql.*;
import org.json.*;

public class LoginService {

    public static JSONObject
        serve(Hashtable<String, String[]> parameters) throws ServiceException {
            try {
                Connection cnx = SQLCore.getConnection();
                try {
                    //preprocess
                    Hashtable<String, String> specs =
                        new Hashtable<String, String>();
                    specs.put("username", "Username (login)");
                    specs.put("password", "Password");
                    ServletMap loginServletMap = new ServletMap(specs);
                    loginServletMap.setInputs(parameters);
                    String username, password;
                    username = loginServletMap.getNotSafe("username");
                    password = loginServletMap.getNotSafe("password");
                    //process
                    PreparedStatement loginStmt =
                        cnx.prepareStatement(
                                "SELECT iid FROM users " +
                                "WHERE username = ? AND password = ?");
                    loginStmt.setString(1, username);
                    loginStmt.setString(2, password);
                    ResultSet rs = loginStmt.executeQuery();
                    //checking username and password
                    if ( rs.next() ) {
                        String userkey = SessionManager.newSession(username, cnx);
                        return ServiceFactory.standard(
                                new JSONObject().put("userkey", userkey));
                    } else {
                        throw new UserException(UserException._BAD_LOGIN);
                    }
                }
                catch (ServiceException se) {
                    return ServiceFactory.error(se);
                }
                catch (JSONException je) {
                    return ServiceFactory.error(
                            new ServiceException(ServiceException._SERVICE_CLASS));
                }
            }
            catch (SQLException e) {
                throw new ServiceException(e);
            }
            catch (ServiceException core) {
                return ServiceFactory.error(core);
            }
        }

}

