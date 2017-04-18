package edu.twister.malik.services.content;

import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.content.ContentException;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class MessageDescription {

    private String message;
    private String username;
    private int sessionid;
    private int useriid;
    private Date date;

    public MessageDescription
        (String message, int sessionid, String username, int useriid) {
            this.message = message;
            this.sessionid = sessionid;
            this.username = username;
            this.useriid = useriid;
            this.date = (new GregorianCalendar()).getTime();
        }

    private static void 
        validate (String message) 
            throws  ServiceException {
            //ContentException._MESSAGE_<...>
            return;
    }

    private static int
        update_sequence //incremental messages id 
        (DBCollection messagesColl, DBCollection countersCollection)
            throws ServiceException {
            int iter, seq = 0;
            do {
                //updates countersCollection
                BasicDBObject query = new BasicDBObject();
                query.put("_id", "mcnt");
                BasicDBObject update = new BasicDBObject();
                BasicDBObject _update = new BasicDBObject();
                _update.put("seq", 1);
                update.put("$inc", _update);
                try {
                    DBObject res = countersCollection.findAndModify(
                            query, null, null, false, update, true, false);
                    Double _seq = (Double) res.get("seq");
                    seq = _seq.intValue();
                } catch (ClassCastException ce) {
                    throw new ContentException(
                            ContentException._CONTENT_NOT_CASTING);
                } catch (MongoException me) {
                    throw new ServiceException(me);
                } catch (NullPointerException npe) {
                    throw new ContentException(ContentException._NO_COUNTERS);
                }
                //count duplicates
                BasicDBObject dup = new BasicDBObject();
                dup.put("mid", seq);
                DBCursor c = messagesColl.find(dup);
                iter = c.count();
            } while (iter > 0); //optimistic iteration for no duplicates
            return seq;
        }


    public int 
        insertIn
        (DBCollection messagesCollection, DBCollection countersCollection) 
            throws ServiceException {
            validate(message);
            int mid = update_sequence(messagesCollection, countersCollection);
            BasicDBObject messageDocument = new BasicDBObject();
            BasicDBObject user = new BasicDBObject();
            messageDocument.put("mid", mid);
            messageDocument.put("message", message);
            user.put("iid", useriid);
            user.put("sid", sessionid);
            user.put("name", username);
            messageDocument.put("user", user);
            messageDocument.put("date", date);
            messageDocument.put("ccnt", 0);
            messageDocument.put("comments", new BasicDBList());
            try {
                messagesCollection.insert(messageDocument);
                return mid;
            } catch (MongoException me) {
                throw new ServiceException(me);
            }
        }
            
}
