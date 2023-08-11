package com.flashlearn;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    public Button backButton;
    @FXML
    public Button loginButton;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    public Text loginHelp;
    @FXML
    public VBox vbox;

    @FXML
    public void initialize(){
        vbox.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try {
                    login();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    protected void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void login() throws IOException {
        loginHelp.setText("");
        String password = passwordField.getText();
        String username = usernameField.getText();
        if(UsersDatabase.checkDetails(username,password)){
            UsersDatabase.setUser(username);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-page-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Flashlearn");
            stage.setScene(scene);
            stage.show();
        }else{
            loginHelp.setText("Username or password incorrect");
        }
    }
}
