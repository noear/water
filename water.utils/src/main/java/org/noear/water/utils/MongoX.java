package org.noear.water.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author noear 2021/2/1 created
 */
public class MongoX {
    MongoClient client;
    MongoDatabase database;


    public MongoX(Properties props, String db) {
        List<ServerAddress> lists = new ArrayList<>();
        MongoClientOptions options = new MongoClientOptions.Builder().build();

        String schema = props.getProperty("schema");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        String servers = props.getProperty("servers");

        String[] serverAry = servers.split(";");

        for (String sev : serverAry) {
            if (TextUtils.isNotEmpty(sev)) {
                lists.add(buildServer(sev));
            }
        }

        if (TextUtils.isNotEmpty(username)) {
            MongoCredential credential = MongoCredential.createCredential(username, schema, password.toCharArray());
            client = new MongoClient(lists, credential, options);
        } else {
            client = new MongoClient(lists, options);
        }


        database = client.getDatabase(db);
    }

    public MongoX(String host, int port, String db) {
        if (port > 0) {
            client = new MongoClient(host, port);
        } else {
            client = new MongoClient(host);
        }

        database = client.getDatabase(db);
    }

    private ServerAddress buildServer(String sev) {
        String host;
        int port = 0;

        if (sev.contains(":")) {
            String[] ss = sev.split(":");
            host = ss[0];
            port = Integer.parseInt(ss[1]);
        } else {
            host = sev;
            port = 27017;
        }

        return new ServerAddress(host, port);
    }


    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public <T> MongoCollection<T> getCollection(String name, Class<T> clz) {
        return database.getCollection(name, clz);
    }
}
