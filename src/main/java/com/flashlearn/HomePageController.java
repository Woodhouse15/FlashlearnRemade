package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HomePageController {
    @FXML
    public ScrollPane contentHolder;

    @FXML
    public Button addSetButton;

    @FXML
    public FlowPane buttonHolder;
    @FXML
    public Button logOutButton;

    @FXML
    public void initialize(){
        ArrayList<String> setNames = UsersDatabase.getUserSets();
        Button[] sets = new Button[setNames.size()];
        int j = 0;
        for(String i : setNames){
            System.out.println(i);
            sets[j] = new Button(i);
            sets[j].setPrefSize(100.0,100.0);
            /*int finalJ = j;
            sets[j].setOnAction(event -> {
                try {
                    loadSet(sets[finalJ].getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });*/
            j++;
        }
        buttonHolder.getChildren().addAll(sets);
    }

    public void addSet() {
    }

    public void logOut() throws IOException {
        UsersDatabase.setUser(null);
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    public void loadSet(String name) throws IOException {
        UsersDatabase.setCurrentSetName(name);
        Stage stage = (Stage) buttonHolder.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flashcard-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }
}
