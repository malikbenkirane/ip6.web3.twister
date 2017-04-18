package edu.twister.malik.services.content;

import edu.twister.malik.services.ServiceException;
import edu.twister.malik.services.content.ContentException;

import java.util.Date;
import java.util.GregorianCalendar;

import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.DBCursor;

public class CommentDescription {

    private String comment;
    private String userkey;
    private String username;
    private int useriid;
    private Date date;

    public CommentDescription
        (String comment, String userkey, String username, int useriid) {
            this.comment = comment;
            this.userkey = userkey;
            this.username = username;
            this.date = (new GregorianCalendar()).getTime();
        }

    private static void
        validate (String comment)
            throws ServiceException {
            //ContentException._COMMENT_<...>
            return;
        }

    private static int
        update_sequence //incremental comment id
        (DBCollection messagesCollection, int mid)
            throws ServiceException {
            int iter, seq = 0;
            do {
                BasicDBObject query = new BasicDBObject();
                query.append("mid", mid);
                BasicDBObject update = new BasicDBObject();
                BasicDBObject _update = new BasicDBObject();
                _update.append("ccnt", 1);
                update.put("$inc", _update);
                try {
                    DBObject res = messagesCollection.findAndModify(
                            query, null, null, false, update, true, false);
                    seq = (Integer) res.get("ccnt");
                    //seq = _seq.intValue();
                } 
                catch (ClassCastException ce) {
                    throw new
                        ContentException(ContentException._CONTENT_NOT_CASTING);
                }
                catch (MongoException me) {
                    throw new 
                        ServiceException(me);
                } 
                catch (NullPointerException npe) {
                    throw new 
                        ContentException(ContentException._NO_COUNTERS);
                }
                //resolve duplicates (concur)
                BasicDBObject dup = new BasicDBObject();
                dup.append("mid", mid);
                dup.append("comments.id", seq);
                DBCursor c = messagesCollection.find(dup);
                iter = c.count();
            } while (iter > 0);
            return seq;
        }

    public int
        insertIn(DBCollection messagesCollection, int mid)
            throws ServiceException {
            validate(comment);
            int cid = update_sequence(messagesCollection, mid);
            /* 
             * document (commentDocument)
             * { id, comment, date, user : { iid, sid, name } }
             */ 
            BasicDBObject commentDocument = new BasicDBObject();
            BasicDBObject user = new BasicDBObject();
            commentDocument.append("id", cid);
            commentDocument.append("date", date);
            commentDocument.append("comment", comment);
            user.append("iid", useriid);
            user.append("sid", userkey);
            user.append("name", username);
            commentDocument.append("user", user);
            //query
            BasicDBObject update = new BasicDBObject();
            BasicDBObject _update = new BasicDBObject();
            BasicDBObject query = new BasicDBObject();
            _update.put("comments", commentDocument);
            update.put("$push", _update);
            query.put("mid", mid);
            try {
                messagesCollection.update(query, update);
                return cid;
            } catch (MongoException me) {
                throw new ServiceException(me);
            }
        }

    public Date
        getDate() {
            return date;
        }

}
