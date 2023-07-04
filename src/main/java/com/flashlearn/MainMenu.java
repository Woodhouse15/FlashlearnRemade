package com.flashlearn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;

public class MainMenu extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage(){
        return primaryStage;
    }
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("Flashlearn");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
        char[] password = new char[]{'h','e','l','l','o'};

        /*System.out.println(Integer.toHexString(Arrays.hashCode(password)));
        String connectionString = System.getenv("mongoDBflashlearn");
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase db = mongoClient.getDatabase("Users");
                Document user = new Document().append("Test",Integer.toHexString(Arrays.hashCode(password)));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                db.getCollection("Test").insertOne(user);
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }*/
    }
}