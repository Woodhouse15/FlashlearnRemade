package com.flashlearn;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Database {
    private static MongoClient client;
    private static Database database;

    private Database(){
        String connectionString = System.getenv("mongoDBflashlearn");
        client = MongoClients.create(connectionString);
    }

    public static MongoClient getConnection() {

        if (database == null) database = new Database();

        return client;
    }
    public static void closeDatabase() {
        client.close();
    }
}
