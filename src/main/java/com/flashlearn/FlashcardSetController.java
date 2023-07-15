package com.flashlearn;

import javafx.fxml.FXML;

import java.util.HashMap;

public class FlashcardSetController {

    @FXML
    public void initialize(){
        HashMap<String,String> cards = UsersDatabase.getSetData();

    }
}
