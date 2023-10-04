package com.flashlearn;

import com.mongodb.MongoNamespace;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class UsersDatabase {
    private static MongoClient client;
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

    public static void start(){
        client = MongoClients.create(System.getenv("mongoDBflashlearn"));
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

    public static HashMap<String, String> getSetData(boolean... flag){
        HashMap<String,String> data = new HashMap<>();
        MongoCollection<Document> mongoCollection = client.getDatabase("Sets").getCollection(user + "_" + currentSetName);
        FindIterable<Document> iterable =  mongoCollection.find();
        if(flag[0]){
            for(Document i : iterable){
                data.put(i.getString("Term"), i.getString("Definition"));
            }
        }else{
            for(Document i : iterable){
                if(i.get("Difficulty") != Difficulty.EASY){
                    data.put(i.getString("Term"), i.getString("Definition"));
                }
            }
        }
        return data;
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
        currentSetName = newSetName;
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

    public static void updateSet(HashMap<String,String> cards){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        MongoCollection<Document> collection = mongoDatabase.getCollection(user + "_" + currentSetName);
        FindIterable<Document> iterable =  collection.find();
        for(Document i : iterable){
            String term = (String) i.get("Term");
            //Term exists but definition is different
            if(cards.containsKey(term) && !(cards.get(term).equals(i.get("Definition")))){
                collection.updateOne(i,Updates.set("Definition", cards.get(term)));
                cards.remove(term);
            }
            //Term doesn't exist
            else if(!cards.containsKey(term)){
                collection.deleteOne(i);
                cards.remove(term);
            }
        }
        for(String i : cards.keySet()){
            addCard(i,cards.get(i));
        }

    }

    public static void addDifficulty(String term, String definition, Difficulty difficulty){
        MongoDatabase mongoDatabase = client.getDatabase("Sets");
        MongoCollection<Document> collection = mongoDatabase.getCollection(user + "_" + currentSetName);
        Bson filter = Filters.and(Filters.eq("Term",term),Filters.eq("Definition",definition));
        collection.updateOne(filter,Updates.set("Difficulty", difficulty));
    }

}

