package com.flashlearn;

import javafx.event.ActionEvent;
import javafx.scene.Node;

public class LearnModeController {


    public void answer(ActionEvent actionEvent) {
        String val = ((Node) actionEvent.getSource()).getId();
        switch (val){
            case "easy":
                break;
            case "medium":
                break;
            case "hard":
                break;
            case "again":
                break;
        }
    }
}
