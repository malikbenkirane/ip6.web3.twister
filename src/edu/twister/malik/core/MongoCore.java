package edu.twister.malik.core;

import com.mongodb.Mongo;
import java.net.UnknownHostException;

import edu.twister.malik.services.ServiceException;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class MongoCore {

    private Mongo mongo;
    private DB db;

    public MongoCore
        () throws ServiceException {
            try {
                mongo = new Mongo(DBStatic.mongoHost, DBStatic.mongoPort);
                db = mongo.getDB(DBStatic.mongoDatabase);
            } 
            catch (UnknownHostException uhe) {
                throw new ServiceException(
                        "Unkown Host", ServiceException._MONGO_CLASS);
            }
        }

    public DBCollection
        getCollection(String name) {
            return db.getCollection(name);
        }

}
