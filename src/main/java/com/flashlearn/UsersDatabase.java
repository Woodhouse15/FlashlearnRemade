package com.flashlearn;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class UsersDatabase {
    private static final MongoClient client = MongoClients.create(System.getenv("mongoDBflashlearn"));
    private static String user;

    private UsersDatabase(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void closeDatabase() {
        client.close();
    }

    public String getUser(){
        return user;
    }

    public static void setUser(String username){
        user = username;
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
        setUser(username);
    }

    public static boolean checkDetails(String username, String password){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        FindIterable<Document> iterable = collection.find(new Document("Username", username)).limit(1);
        return iterable.first() != null && Objects.requireNonNull(iterable.first()).get("Password").equals(Integer.toHexString(Arrays.hashCode(password.toCharArray())));
    }

    public static ArrayList<String> getUserSets(){
        FindIterable<Document> flashcardSets = client.getDatabase("Users").getCollection(user).find();
        ArrayList<String> setNames = new ArrayList<>();
        for (Document doc : flashcardSets) {
            setNames.add(doc.getString("Set"));
        }
        return setNames;
    }
}

