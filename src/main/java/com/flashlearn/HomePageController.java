package com.flashlearn;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;

public class HomePageController {
    @FXML
    public ScrollPane contentHolder;

    @FXML
    public Button addSetButton;

    @FXML
    public TilePane buttonHolder = new TilePane();

    @FXML
    public void initialize(){
        ArrayList<String> setNames = UsersDatabase.getUserSets();
        Button[] sets = new Button[setNames.size()+1];
        int j = 0;
        for(String i : setNames){
            System.out.println(i);
            sets[j] = new Button(i);
            sets[j].setPrefSize(100.0,100.0);
            int finalJ = j;
            sets[j].setOnAction(event -> loadSet(sets[finalJ].getText()));
            j++;
        }
        buttonHolder.getChildren().addAll(sets);
    }

    public void addSet() {
    }

    public void loadSet(String name){
        System.out.println(name);
    }
}
