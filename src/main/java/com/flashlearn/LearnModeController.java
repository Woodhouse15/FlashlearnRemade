package com.flashlearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.*;

public class LearnModeController {

    @FXML
    public Text termText;
    @FXML
    public Text definitionText;
    @FXML
    public HBox buttonHolder;
    HashMap<String,String> cards;
    Button easy = new Button("Easy");
    Button medium = new Button("Medium");
    Button hard = new Button("Hard");
    Button again = new Button("Again");

    Button flipButton = new Button("Flip Card or Press Enter");

    public List<String> terms;

    @FXML
    public void initialize() {
        termText.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                flip();
            }
        });
        cards = UsersDatabase.getSetData();
        terms = new ArrayList<>(cards.keySet());
        Collections.shuffle(terms);
        flipButton.setPrefHeight(50.0);
        flipButton.setOnAction(actionEvent -> flip());
        easy.setPrefSize(100.0,50.0);
        easy.setOnAction(this::answer);
        medium.setPrefSize(100.0,50.0);
        medium.setOnAction(this::answer);
        hard.setPrefSize(100.0,50.0);
        hard.setOnAction(this::answer);
        again.setPrefSize(100.0,50.0);
        again.setOnAction(this::answer);
    }

    public void answer(ActionEvent actionEvent) {
        String val = ((Node) actionEvent.getSource()).getId();
        switch (val) {
            case "easy" -> System.out.println("Easy");
            case "medium" -> System.out.println("Not bad");
            case "hard" -> System.out.println("Not great");
            case "again" -> System.out.println("Too hard");
        }
    }

    public void flip(){

    }
}
