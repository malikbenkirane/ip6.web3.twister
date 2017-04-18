package edu.twister.malik.services.content;

import edu.twister.malik.core.MongoCore;
import edu.twister.malik.core.SQLCore;

import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.ServletMap;

import edu.twister.malik.services.user.SessionManager;
import edu.twister.malik.services.user.SessionDescription;
import edu.twister.malik.services.ServiceFactory;

import java.util.Hashtable;
import java.sql.Connection;

import com.mongodb.DBCollection;
import org.json.JSONObject;
import org.json.JSONException;

public class PostMessageService {

    public static JSONObject
        serve
        (Hashtable<String, String[]> parameters) throws ServiceException {
            //serve preprocess
            Hashtable<String, String> specs =
                new Hashtable<String, String>();
            specs.put("userkey", "User session key");
            specs.put("message", "Message text");
            ServletMap postmServletMap = new ServletMap(specs);
            postmServletMap.setInputs(parameters);
            String userkey = postmServletMap.getNotSafe("userkey");
            String message = postmServletMap.getNotSafe("message");
            //serve process
            MongoCore mongo = new MongoCore();
            DBCollection messagesCollection = mongo.getCollection("messages");
            DBCollection countersCollection = mongo.getCollection("counters");
            try {
                Connection cnx = SQLCore.getConnection();
                SessionDescription session = 
                    SessionManager.retrieve_session(userkey, cnx);
                int messageID = 
                    new MessageDescription(
                            message, session.getSessionIID(),
                            session.getUsername(), session.getUserIID()
                            ).insertIn(messagesCollection,
                        countersCollection);
                return ServiceFactory.standard(
                        new JSONObject().put("mid", messageID));
            } 
            catch (ServiceException se) {
                return ServiceFactory.error(se);
            }
            catch (JSONException je) {
                return ServiceFactory.error(
                        new ServiceException(ServiceException._SERVICE_CLASS));
            }

        }

}
