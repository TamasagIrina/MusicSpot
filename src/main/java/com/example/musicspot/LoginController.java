package com.example.musicspot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Button cancelButton;

    @FXML
    private ImageView imgProfile, imgPassw, imgSound;

    @FXML
    private Label loginMassegLabel ;

    @FXML
    public TextField usernameTextFild;

    @FXML
    private PasswordField passwordField ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\round-account-button-with-user-inside.png");
        imgProfile.setImage(image);
        Image image2 = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\lock.png");
        imgPassw.setImage(image2);
    }

    @FXML
    private void signingButtonOnAction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e, "signin.fxml");
    }

    public void loginButtonOnAction(ActionEvent e) throws IOException {
       if(!usernameTextFild.getText().isBlank() && !passwordField.getText().isBlank()){
           validateLogin(e);
       }else {
           loginMassegLabel.setText("Please enter username and password");
       }
    }

    public void validateLogin(ActionEvent event ) throws IOException {

        DatabaseConection connectNow = new DatabaseConection();

        if(connectNow.exitInDatabase(usernameTextFild.getText(), passwordField.getText())){
            MusicController.setName(usernameTextFild.getText());
            loginMassegLabel.setText("Welcome!");
            SwitchTo s= new SwitchTo();
            s.switchToScreen(event,"music.fxml");

        }else {
            loginMassegLabel.setText("Invalid Login. Please Signin.");
        }
    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();

    }


}