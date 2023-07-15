package com.flashlearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.util.HashMap;

public class FlashcardSetController {

    @FXML
    public ScrollPane contentHolder;
    @FXML
    public Button backButton;
    public Text flashcardTitle;

    @FXML
    public void initialize(){
        HashMap<String,String> cards = UsersDatabase.getSetData();

    }

    public void back(ActionEvent actionEvent) {
    }
}
