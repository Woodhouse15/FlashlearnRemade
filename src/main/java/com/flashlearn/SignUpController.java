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
import java.util.regex.*;

public class SignUpController {
    @FXML
    public Button backButton;
    @FXML
    public Button signUpButton;
    @FXML
    public PasswordField passwordField;
    public TextField usernameField;
    @FXML
    public Text signUpGuide;
    @FXML
    public VBox vbox;

    @FXML
    public void initialize(){
        vbox.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                try {
                    signUp();
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
    protected void signUp() throws IOException {
        signUpGuide.setText("");
        String password = passwordField.getText();
        String username = usernameField.getText();
        if(username.length() < 1 || username.length() >64){
            passwordField.clear();
            usernameField.clear();
            signUpGuide.setText(signUpGuide.getText() + "Username must be between 1 and 64 characters\n");
        }else if(username.contains("$") || username.startsWith("system.")){
            passwordField.clear();
            usernameField.clear();
            signUpGuide.setText(signUpGuide.getText() + "Username must not contain the $ sign or have the 'system.' prefix\n");
        } else{
            String regex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";
            Pattern pattern = Pattern.compile(regex);
            if(password.length() < 1){
                passwordField.clear();
                usernameField.clear();
                signUpGuide.setText(signUpGuide.getText() + "Password cannot be empty\n");
                return;
            }
            Matcher matcher = pattern.matcher(password);
            if(matcher.matches()){
                if(!UsersDatabase.checkUserExists(username)){
                    UsersDatabase.addUser(username,password);
                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-page-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    stage.setTitle("Flashlearn");
                    stage.setScene(scene);
                    stage.show();
                }else{
                    passwordField.clear();
                    usernameField.clear();
                    signUpGuide.setText(signUpGuide.getText() + "Username taken\n");
                }
            }else{
                signUpGuide.setText(signUpGuide.getText() + "Password must contain:\n 8-20 characters\n1 digit\n1 uppercase\n1 lowercase\na special character\nno white space\n");
            }
        }
    }
}
