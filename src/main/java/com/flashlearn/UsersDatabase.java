package com.flashlearn;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.Arrays;
import java.util.Objects;

public class UsersDatabase {
    private static MongoClient client;
    private static UsersDatabase database = null;

    private UsersDatabase(){
        String connectionString = System.getenv("mongoDBflashlearn");
        client = MongoClients.create(connectionString);
    }

    public static MongoClient getConnection() {

        if (database == null) database = new UsersDatabase();

        return client;
    }
    public static void closeDatabase() {
        client.close();
    }

    public static boolean checkUserExists(String username) {
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        FindIterable<Document> iterable = collection
                .find(new Document("Username", username)).limit(1);
        return iterable.first() != null;
    }

    public static void addUser(String username, String password){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        mongoDatabase.createCollection(username);
        var d1 = new Document("Username",username);
        d1.append("Password", Integer.toHexString(Arrays.hashCode(password.toCharArray())));
        collection.insertOne(d1);
    }

    public static boolean checkDetails(String username, String password){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        FindIterable<Document> iterable = collection.find(new Document("Username", username)).limit(1);
        return iterable.first() != null && Objects.requireNonNull(iterable.first()).get("Password").equals(Integer.toHexString(Arrays.hashCode(password.toCharArray())));
    }
}

