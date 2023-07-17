package com.flashlearn;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoNamespace;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public final class UsersDatabase {
    private static final MongoClient client = MongoClients.create(System.getenv("mongoDBflashlearn"));
    private static String user;

    private static String currentSetName;

    private UsersDatabase(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void closeDatabase() {
        client.close();
    }

    public static String getCurrentSetName() {
        return currentSetName;
    }

    public static void setCurrentSetName(String currentSetName) {
        UsersDatabase.currentSetName = currentSetName;
    }

    public String getUser(){
        return user;
    }

    public static void start(){
    }

    public static void setUser(String username){
        user = username;
    }

    public static boolean checkUserExists(String username) {
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        FindIterable<Document> iterable = collection.find(Filters.eq("Username",username)).limit(1);
        return iterable.first() != null;
    }

    public static void addUser(String username, String password){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        var d1 = new Document("Username",username);
        d1.append("Password", Integer.toHexString(Arrays.hashCode(password.toCharArray())));
        collection.insertOne(d1);
        setUser(username);
    }

    public static boolean checkDetails(String username, String password){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UsersInfo");
        Bson filter = Filters.and(Filters.eq("Username",username),Filters.eq("Password",Integer.toHexString(Arrays.hashCode(password.toCharArray()))));
        FindIterable<Document> iterable = collection.find(filter).limit(1);
        return iterable.first() != null;
    }

    public static ArrayList<String> getUserSets(){
        MongoCollection<Document> flashcardSets = client.getDatabase("Users").getCollection("UserSets");
        ArrayList<String> setNames = new ArrayList<>();
        FindIterable<Document> iterable =  flashcardSets.find(Filters.eq("User",user));
        for(Document docs : iterable){
            setNames.add(docs.getString("SetName"));
        }
        return setNames;
    }

    public static void newSet(String setName){
        MongoDatabase database = client.getDatabase("Sets");
        String setID = user + "_" + setName;
        database.createCollection(setID);
        MongoCollection<Document> userInfo = client.getDatabase("Users").getCollection("UserSets");
        Document doc = new Document("User",user);
        doc.append("SetName",setName);
        userInfo.insertOne(doc);
    }

    public static HashMap<String, String> getSetData(){
        HashMap<String,String> data = new HashMap<>();
        MongoCollection<Document> mongoCollection = client.getDatabase("Sets").getCollection(user + "_" + currentSetName);
        FindIterable<Document> iterable =  mongoCollection.find();
        for(Document i : iterable){
            data.put(i.getString("Term"), i.getString("Definition"));
        }
        return data;
    }

    public static boolean doesCardExist(String term, String definition){
        MongoCollection<Document> mongoCollection = client.getDatabase("Sets").getCollection(user + "_" + currentSetName);
        Bson filter = Filters.and(Filters.eq("Term",term),Filters.eq("Definition",definition));
        FindIterable<Document> iterable =  mongoCollection.find(filter);
        return iterable.first() != null;
    }

    public static void addCard(String term, String definition){
        MongoCollection<Document> mongoCollection = client.getDatabase("Sets").getCollection(user + "_" + currentSetName);
        Document document = new Document("Term",term);
        document.append("Definition",definition);
        mongoCollection.insertOne(document);
    }

    public static boolean checkSetExists(String setName){
        MongoDatabase mongoDatabase = client.getDatabase("Users");
        MongoCollection<Document> collection = mongoDatabase.getCollection("UserSets");
        FindIterable<Document> iterable = collection.find(new Document("User", user).append("SetName",setName)).limit(1);
        return iterable.first() != null;
    }

    public static void changeSetName(String newSetName){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        MongoNamespace newName = new MongoNamespace("Sets" ,user + "_" +newSetName);
        mongoDatabase.getCollection(user + "_" +currentSetName).renameCollection(newName);
        MongoDatabase database = client.getDatabase("Users");
        MongoCollection<Document> collection = database.getCollection("UserSets");
        Bson filter = Filters.and(Filters.eq("User",user),Filters.eq("SetName",currentSetName));
        collection.updateOne(filter,Updates.set("SetName", newSetName));
    }

    public static void updateCard(String oldTerm, String newTerm, String oldDef, String newDef){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        MongoCollection<Document> collection = mongoDatabase.getCollection(user + "_" +currentSetName);
        Bson filter = Filters.and(Filters.eq("Term",oldTerm),Filters.eq("Definition",oldDef));
        collection.updateMany(filter,Updates.combine(Updates.set("Term",newTerm),Updates.set("Definition",newDef)));
    }

    public static void deleteSet(){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        mongoDatabase.getCollection(user + "_" + currentSetName).drop();
        MongoDatabase userInfo = client.getDatabase("Users");
        MongoCollection<Document> collection = userInfo.getCollection("UserSets");
        Bson filter = Filters.and(Filters.eq("User",user),Filters.eq("SetName",currentSetName));
        collection.deleteOne(filter);
        currentSetName = null;
    }

    public static void deleteCard(String term, String def){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        MongoCollection<Document> collection = mongoDatabase.getCollection(user + "_" + currentSetName);
        Bson filter = Filters.and(Filters.eq("Term",term),Filters.eq("Definition",def));
        collection.deleteOne(filter);
    }
}

