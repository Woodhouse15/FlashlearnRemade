package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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

    public Text promptText = new Text();
    final Stage dialog = new Stage();

    @FXML
    public void initialize(){
        ArrayList<String> setNames = UsersDatabase.getUserSets();
        Button[] sets = new Button[setNames.size()];
        int j = 0;
        for(String i : setNames){
            sets[j] = new Button(i);
            sets[j].wrapTextProperty().setValue(true);
            sets[j].setPrefSize(100.0,100.0);
            int finalJ = j;
            sets[j].setOnAction(event -> {
                try {
                    moveToFlashcardView(sets[finalJ].getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            j++;
        }
        buttonHolder.getChildren().addAll(sets);
    }

    public void addSet() {
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(addSetButton.getScene().getWindow());
        VBox dialogVbox = new VBox(20);
        Text prompt = new Text("Enter Set Name Below");
        TextField setName = new TextField();
        Button enterName = new Button("Add Set");
        enterName.setOnAction(event -> {
            try {
                validateNewSet(setName.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dialogVbox.getChildren().addAll(prompt,setName,promptText,enterName);
        Scene dialogScene = new Scene(dialogVbox,300,200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void validateNewSet(String setName) throws IOException {
        if(setName.length() < 1 || setName.contains("$")){
            promptText.setText("Set name can't be empty or have the $ sign");
        } else if (UsersDatabase.checkSetExists(setName)) {
            promptText.setText("Set already exists");
        } else{
            UsersDatabase.newSet(setName);
            dialog.close();
            moveToFlashcardView(setName);
        }
    }

    private void moveToFlashcardView(String setName) throws IOException {
        UsersDatabase.setCurrentSetName(setName);
        Stage stage = (Stage) buttonHolder.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flashcard-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
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
}
