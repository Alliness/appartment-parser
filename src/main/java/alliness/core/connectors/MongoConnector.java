package alliness.core.connectors;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Core on 02.07.2017.
 */
public class MongoConnector {

    private static final Logger log = Logger.getLogger(MongoConnector.class);

    private MongoClient   client;
    private MongoDatabase db;
    private boolean       isConnected;

    public MongoConnector(String host, int port, String database, String user, String password) {
        MongoClientOptions clientOptions = new MongoClientOptions.Builder()
                .connectTimeout(15000)
                .socketTimeout(10000)
                .build();

        List<MongoCredential> list = new ArrayList<>();
        list.add(MongoCredential.createCredential(user,
                                                  database,
                                                  password.toCharArray()));

        client = new MongoClient(new ServerAddress(host, port), list, clientOptions);
        db = client.getDatabase(database);
    }

    public MongoConnector(String host, int port, String database) {
        MongoClientOptions clientOptions = new MongoClientOptions.Builder()
                .connectTimeout(15000)
                .socketTimeout(10000)
                .build();

        client = new MongoClient(new ServerAddress(host, port), clientOptions);
        db = client.getDatabase(database);
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return db.getCollection(collectionName);
    }

    public boolean isConnected() {
        return isConnected;
    }

}
