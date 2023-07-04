package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    public Button exitButton;
    @FXML
    public Button logInButton;
    @FXML
    public Button signUpButton;
    @FXML
    protected void logInClick(){

    }

    @FXML
    protected void signUpClick() throws IOException {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sign-up-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}