package com.flashlearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class LearnModeController {

    @FXML
    public Text termText;
    @FXML
    public Text definitionText;
    @FXML
    public HBox buttonHolder;
    @FXML
    public BorderPane borderPane;
    HashMap<String,String> cards;
    Button easy = new Button("Easy");
    Button medium = new Button("Medium");
    Button hard = new Button("Hard");
    Button again = new Button("Again");
    Button flipButton = new Button("Flip Card or Press Enter");
    String currentTerm;
    public List<String> terms;

    @FXML
    public void initialize() {
        borderPane.setOnKeyPressed(e -> {
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
        easy.setId("easy");
        medium.setPrefSize(100.0,50.0);
        medium.setOnAction(this::answer);
        medium.setId("medium");
        hard.setPrefSize(100.0,50.0);
        hard.setOnAction(this::answer);
        hard.setId("hard");
        again.setPrefSize(100.0,50.0);
        again.setOnAction(this::answer);
        again.setId("again");
        buttonHolder.getChildren().add(flipButton);
        if(!terms.isEmpty()){
            currentTerm = terms.get(0);
            termText.setText(currentTerm);
        }
    }

    public void answer(ActionEvent actionEvent) {
        String val = ((Node) actionEvent.getSource()).getId();
        switch (val) {
            case "easy" -> System.out.println("Easy");
            case "medium" -> System.out.println("Not bad");
            case "hard" -> System.out.println("Not great");
            case "again" -> System.out.println("Too hard");
        }
        buttonHolder.getChildren().clear();
        newCard();
    }

    public void flip(){
        buttonHolder.getChildren().clear();
        buttonHolder.getChildren().addAll(easy,medium,hard,again);
        definitionText.setText(cards.get(currentTerm));
    }

    public void newCard(){
        definitionText.setText("");
        terms.remove(currentTerm);
        if(terms.isEmpty()){
            termText.setText("");
            definitionText.setText("Congratulations, you have finished studying this set today!");
            Button end = new Button("Return to flashcard set view");
            end.setOnAction(e -> {
                try {
                    returnToHome();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonHolder.getChildren().add(end);

        }else{
            currentTerm = terms.get(0);
            termText.setText(currentTerm);
            buttonHolder.getChildren().add(flipButton);
        }

    }

    public void returnToHome() throws IOException {
        Stage stage = (Stage) buttonHolder.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("flashcard-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }
}
