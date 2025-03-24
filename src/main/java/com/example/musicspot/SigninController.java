package com.example.musicspot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SigninController {


    @FXML
    private Label signinMassegLabel ;

    @FXML
    private TextField firstNameTextFild;

    @FXML
    private TextField lastNameTextFild;

    @FXML
    private TextField usernameTextFild;

    @FXML
    private PasswordField passwordField ;

    @FXML
    private void signinButtonOnAction(ActionEvent event) throws  IOException {
        DatabaseConection connectNow = new DatabaseConection();
        try {
            if(!usernameTextFild.getText().isBlank() && !passwordField.getText().isBlank() && !firstNameTextFild.getText().isBlank() && !lastNameTextFild.getText().isBlank()) {
                if(connectNow.exitInDatabase(usernameTextFild.getText(), passwordField.getText())) {
                    signinMassegLabel.setText("The account already exist. Go to login");
                }else {
                    intertInDataBase(event);
                    SwitchTo s= new SwitchTo();
                    s.switchToScreen(event, "music.fxml");
                }
            }else {
                signinMassegLabel.setText("Please enter first name, last name, username and password!");
            }
        }catch (SQLException e){
            signinMassegLabel.setText("Something went wrong. The username or password are already use. Please try again.");
        }
    }

    @FXML
    private  void backButtonOnAction(ActionEvent event) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(event, "login.fxml");
    }

    @FXML
    private void intertInDataBase(ActionEvent e) throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB=connectNow.getConnection();

        String insert = "INSERT INTO useraccounts (FirstName, LastName, UserName, Password) VALUES ('" + firstNameTextFild.getText() + "', '" + lastNameTextFild.getText() + "', '" + usernameTextFild.getText() + "', '" + passwordField.getText() + "');";

        System.out.println(insert);
        Statement statement= connectDB.createStatement();
        statement.execute(insert);
    }
}
