package com.flashlearn;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
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
    protected void back() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Flashlearn");
        stage.setScene(scene);
        stage.show();
    }



    private void addUser(String username, String password){

    }

    @FXML
    protected void signUp(){
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
                if(!MainMenu.checkForDatabase(username)){
                    System.out.println("WE DO BE VIBING");
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
