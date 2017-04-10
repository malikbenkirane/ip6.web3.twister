package edu.twister.malik.services.user;

import edu.twister.malik.core.SQLCore;
import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServletMap;
import edu.twister.malik.services.user.SessionManager;

import java.util.Hashtable;
import java.sql.Connection;
import java.sql.*;
import org.json.*;

public class SignUpService {

    public static JSONObject 
        serve(Hashtable<String, String[]> parameters) throws ServiceException {
            try {
                Connection cnx = SQLCore.getConnection();
                try {
                    //serve preprocess
                    Hashtable<String, String> specs = 
                        new Hashtable<String, String>();
                    specs.put("firstname", "Firstname");
                    specs.put("lastname", "Lastname");
                    specs.put("email", "Email Address");
                    specs.put("username", "Username");
                    specs.put("password", "Password");
                    ServletMap signupServletMap = new ServletMap(specs);
                    signupServletMap.setInputs(parameters);
                    String username, firstname, lastname, email, password;
                    username = signupServletMap.getNotSafe("username");
                    firstname = signupServletMap.getNotSafe("firstname");
                    lastname = signupServletMap.getNotSafe("lastname");
                    email = signupServletMap.getNotSafe("email");
                    password = signupServletMap.getNotSafe("password");
                    //serve process
                    new UserDescription(
                            firstname, lastname, email, username, cnx
                            ).verify();
                    PreparedStatement signupStmt = 
                        cnx.prepareStatement(
                                "INSERT INTO users " +
                                "(firstname, lastname, email, username, password) " +
                                "VALUES (?, ?, ?, ?, ?);");
                    signupStmt.setString(1, firstname);
                    signupStmt.setString(2, lastname);
                    signupStmt.setString(3, email);
                    signupStmt.setString(4, username);
                    signupStmt.setString(5, password);
                    signupStmt.executeUpdate();
                    String userkey = SessionManager.newSession(username, cnx);
                    return ServiceFactory.standard(
                            new JSONObject().put("userkey", userkey)
                            );
                }
                catch (ServiceException ue) {
                    return ServiceFactory.error(ue);
                }
                catch (JSONException je) {
                    return ServiceFactory.error(
                            new ServiceException(ServiceException._SERVICE_CLASS)
                            );
                }
            }
            catch (SQLException e) {
                throw new ServiceException(e);
            }
            catch (ServiceException core) {
                //SQLCore.getConnection() throws ServiceException
                return ServiceFactory.error(core);
            }
         }

}
