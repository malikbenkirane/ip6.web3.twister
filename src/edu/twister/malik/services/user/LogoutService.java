package edu.twister.malik.services.user;

import edu.twister.malik.core.SQLCore;
import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServletMap;
import edu.twister.malik.services.user.SessionManager;

import java.util.Hashtable;
import java.sql.*;
import org.json.*;

public class LogoutService {

    public static JSONObject
        serve(Hashtable<String, String[]> parameters) throws ServiceException {
            try {
                Connection cnx = SQLCore.getConnection();
                try {
                    //peprocess
                    Hashtable<String, String> specs =
                        new Hashtable<String, String>();
                    specs.put("userkey", "generated private use user key");
                    ServletMap logoutServletMap = new ServletMap(specs);
                    logoutServletMap = new ServletMap(specs);
                    logoutServletMap.setInputs(parameters);
                    String userkey = logoutServletMap.getNotSafe("userkey");
                    //process
                    SessionManager.close_session_instance(userkey, cnx);
                    return ServiceFactory.standard(new JSONObject());
                } 
                catch (ServiceException se) {
                    return ServiceFactory.error(se);
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
