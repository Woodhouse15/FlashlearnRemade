package com.flashlearn;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CustomCard extends VBox {
    public TextArea termField;
    public TextArea definitionField;

    public CustomCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getTermText(){
        return termField.getText();
    }

    public String getDefinitionText(){
        return definitionField.getText();
    }

    public void setTermText(String term){
        termField.setText(term);
        termField.autosize();
    }

    public void setDefinitionText(String definition){
        definitionField.setText(definition);
        autosize();
    }

}
