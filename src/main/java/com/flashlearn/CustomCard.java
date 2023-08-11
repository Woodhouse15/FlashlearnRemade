package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class CustomCard extends GridPane {
    @FXML
    public TextArea termField;
    @FXML
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
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        definitionField.autosize();
    }

    public void enableText(boolean edit){
        termField.setEditable(edit);
        definitionField.setEditable(edit);
    }

}
