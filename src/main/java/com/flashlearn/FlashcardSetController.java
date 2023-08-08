package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
    public final Text promptText = new Text();
    @FXML
    public VBox cardHolder;
    @FXML
    public Button learnSetButton;
    public HBox buttonHolder;

    ArrayList<CustomCard> data = new ArrayList<>();
    final Button save = new Button("Save Changes");

    @FXML
    public void initialize(){
        flashcardTitle.setText(UsersDatabase.getCurrentSetName());
        HashMap<String,String> cards = UsersDatabase.getSetData();
        for(String i : cards.keySet()){
            CustomCard card = new CustomCard();
            card.setTermText(i);
            card.setDefinitionText(cards.get(i));
            card.setPrefSize(600,50);
            data.add(card);
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

    public void editSet() {
        for(CustomCard i : data){
            i.enableText(true);
            Button delete = new Button("Delete");
            delete.setOnAction(event -> {
                data.remove(i);
                cardHolder.getChildren().remove(i);
            });
            i.add(delete,1,2);
        }
        Button addCard = new Button("+");
        addCard.setTextAlignment(TextAlignment.CENTER);
        addCard.setOnAction(event -> addNewCard());
        cardHolder.getChildren().add(0,addCard);
        Button discard = new Button("Back");
        buttonHolder.getChildren().clear();
        buttonHolder.getChildren().addAll(save,discard);
        save.setOnAction(event -> {
            try {
                saveSet();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        discard.setOnAction(actionEvent -> {
            try {
                reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addNewCard(){
        CustomCard card = new CustomCard();
        card.enableText(true);
        card.setPrefSize(600,50);
        data.add(card);
        Button delete = new Button("Delete");
        delete.setOnAction(event -> {
            data.remove(card);
            cardHolder.getChildren().remove(card);
        });
        card.add(delete,1,2);
        cardHolder.getChildren().add(1,card);
    }

    public void saveSet() throws IOException {
        HashMap<String,String> cards = new HashMap<>();
        boolean duplicates = false;
        for(CustomCard i : data){
            String term = i.getTermText();
            String definition = i.getDefinitionText();
            if(cards.get(term) == null){
                cards.put(term,definition);
            }else{
                System.out.println("Here");
                duplicates = true;
            }
        }
        if(!duplicates){
            UsersDatabase.updateSet(cards);
            reset();
        }else{
            System.out.println("Not working");
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(save.getScene().getWindow());
            VBox dialogVbox = new VBox(20);
            Text promptText = new Text("There cannot be duplicate terms");
            Button ok = new Button("OK");
            ok.setOnAction(event -> dialog.close());
            dialogVbox.getChildren().addAll(promptText,ok);
            Scene dialogScene = new Scene(dialogVbox,300,200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }
}
