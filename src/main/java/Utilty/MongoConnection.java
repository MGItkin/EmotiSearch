package Utilty;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Created by TUSHAR_SK on 11/13/15.
 */
public class MongoConnection{

    public DBCollection getMongoConection() {

        try {

            MongoClientURI uri = new MongoClientURI("mongodb://tushar:mydbpass@ds045021.mongolab.com:45021/tushardb");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB("tushardb");
            DBCollection collection = db.getCollection("GameStopTable6");
            return collection;

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
}
