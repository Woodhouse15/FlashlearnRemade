package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class FlashcardSetController {
    @FXML
    public Button backButton;
    @FXML
    public Text flashcardTitle;
    @FXML
    public Button editCardsButton;
    @FXML
    public Button deleteSetButton;
    @FXML
    public Button editSetNameButton;
    public Text promptText = new Text();
    @FXML
    public VBox cardHolder;
    @FXML
    public Button learnSetButton;

    @FXML
    public void initialize(){
        flashcardTitle.setText(UsersDatabase.getCurrentSetName());
        HashMap<String,String> cards = UsersDatabase.getSetData();
        CustomCard[] data = new CustomCard[cards.size()];
        int j = 0;
        for(String i : cards.keySet()){
            data[j] = new CustomCard();
            data[j].setTermText(i);
            data[j].setDefinitionText(cards.get(i));
            data[j].setPrefSize(600,50);
            j++;
        }
        cardHolder.getChildren().addAll(data);
    }

    public void back() throws IOException {
        UsersDatabase.setCurrentSetName(null);
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-page-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    public void editSetName() {
        final Stage dialog = new Stage();
        VBox dialogVbox = new VBox();
        Text prompt = new Text("Enter Set Name Below");
        TextField setName = new TextField();
        Button enterName = new Button("Change Name");
        enterName.setOnAction(event -> {
            String newSet = setName.getText();
            if(newSet.length() < 1 || newSet.contains("$")){
                promptText.setText("Set name can't be empty or have the $ sign");
            } else if (UsersDatabase.checkSetExists(newSet)) {
                promptText.setText("Set already exists");
            } else{
                UsersDatabase.changeSetName(newSet);
                dialog.close();
                try {
                    reset();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dialogVbox.getChildren().addAll(prompt,setName,promptText,enterName);
        Scene dialogScene = new Scene(dialogVbox,300,200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void reset() throws IOException {
        Stage stage = (Stage) cardHolder.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flashcard-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSet() throws IOException {
        UsersDatabase.deleteSet();
        Stage stage = (Stage) cardHolder.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-page-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }
}
