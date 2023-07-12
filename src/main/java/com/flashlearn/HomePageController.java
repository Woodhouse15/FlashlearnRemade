package com.flashlearn;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class HomePageController {
    @FXML
    public ScrollPane contentHolder;

    public Button addSetButton;

    @FXML
    public GridPane buttonHolder = new GridPane();

    @FXML
    public void initialize(){
        contentHolder.setContent(buttonHolder);

        ArrayList<String> setNames = UsersDatabase.getUserSets();

    }
}
