package com.flashlearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class FlashcardSetController {

    @FXML
    public ScrollPane contentHolder;
    @FXML
    public Button backButton;
    @FXML
    public Text flashcardTitle;
    @FXML
    public Button addCardButton;
    @FXML
    public Button deleteSetButton;
    public Button editSetNameButton;
    public Text promptText;

    @FXML
    public void initialize(){
        flashcardTitle.setText(UsersDatabase.getCurrentSetName());
        HashMap<String,String> cards = UsersDatabase.getSetData();
        for(String i : cards.keySet()){
            System.out.println(i);
        }
    }

    public void back() throws IOException {
        UsersDatabase.setCurrentSetName(null);
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flashcard-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    public void editSetName() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(editSetNameButton.getScene().getWindow());
        VBox dialogVbox = new VBox(20);
        Text prompt = new Text("Enter Set Name Below");
        TextField setName = new TextField();
        Button enterName = new Button("Add Set");
        enterName.setOnAction(event -> {
            String newSet = setName.getText();
            if(newSet.length() < 1 || newSet.contains("$")){
                promptText.setText("Set name can't be empty or have the $ sign");
            } else if (UsersDatabase.checkSetExists(newSet)) {
                promptText.setText("Set already exists");
            } else{
                UsersDatabase.changeSetName(newSet);
                initialize();
            }
        });
        dialogVbox.getChildren().addAll(prompt,setName,promptText,enterName);
        Scene dialogScene = new Scene(dialogVbox,300,200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
