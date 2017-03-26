package edu.twister.malik.services;

import org.json.*;
import edu.twister.malik.services.ServiceException;

public class ServiceFactory {

    public static JSONObject 
        standard(JSONObject delivery) throws ServiceException {
            JSONObject out = new JSONObject();
            try {
                out.put("state", 0);
                out.put("response", delivery);
            }
            catch (JSONException e) {
                throw new 
                    ServiceException(e.getMessage(), ServiceException._JSON_CLASS);
            }

            return out;
        }

    public static JSONObject
        error(ServiceException e) {
            try {
                return new JSONObject()
                    .put("state", 1)
                    .put("error", new JSONObject()
                            .put("message", e.getMessage())
                            .put("class", e.getErrorClass())
                            .put("code", e.getErrorCode()));
            }
            catch (JSONException core) {
                core.printStackTrace();
                return new JSONObject();
            }
        }

}

