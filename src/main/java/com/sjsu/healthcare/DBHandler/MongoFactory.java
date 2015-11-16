package com.sjsu.healthcare.DBHandler;

/**
 * Created by Sindhu Kashyap on 11/3/2015.
 */
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;

import java.net.UnknownHostException;

public class MongoFactory {

    public static DB getConnection() throws UnknownHostException
    {
        MongoClientURI uri = new MongoClientURI("mongodb://295A:295A@ds051893.mongolab.com:51893/healthapp");
        MongoClient mongoClient = new MongoClient(uri);
        DB db = mongoClient.getDB(uri.getDatabase());
        mongoClient.setWriteConcern(WriteConcern.JOURNALED);
        return db;
    }
}
