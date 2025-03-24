package com.example.musicspot;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAFriend implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Circle circle;
    @FXML
    private Label accontWLable , accontGLable, nameLabel;
    @FXML
    private TextField nameAccont;
    @FXML
    private ListView<String> listViewRequest;
    static String name;

    public static void setName(String name) {
        AddAFriend.name = name;
    }

    public void cancelButtonOnAction() {
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void backButtonOnaction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e, "music.fxml");
    }

    public void alert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setContentText("Select username");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\round-account-button-with-user-inside.png");
        circle.setFill(new ImagePattern(img));
        nameLabel.setText(name);
        DatabaseConection connectNow = new DatabaseConection();
        try {
            int id_account= connectNow.getID(name);
            ArrayList<String> names;
            names=connectNow.getFriendsFromDatabase(id_account,"friendsrequest");

            for(String name : names){
                listViewRequest.getItems().add(name);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onScreenAction() {
        listViewRequest.getSelectionModel().clearSelection();
    }

    @FXML
    public void deleteButtonOnAction() throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();
        String selected= listViewRequest.getSelectionModel().getSelectedItem();
        if (selected!=null) {
            String delete = "DELETE FROM friendsrequest WHERE friend_name=\"" + selected + "\";";
            Statement statement = connectDB.createStatement();
            statement.execute(delete);
            listViewRequest.getItems().remove(selected);
        }else {
            alert();
        }
    }

    @FXML
    public void clickOnFiled(){
        EventHandler<KeyEvent> eventHandlerTextField = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                accontGLable.setText(null);
                accontWLable.setText(null);
            }
        };
        nameAccont.addEventHandler(KeyEvent.KEY_TYPED, eventHandlerTextField);
    }

    @FXML
    public void requestButton() throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();

        String name_friend= nameAccont.getText();
        int id_account= connectNow.getID(name);
        System.out.println(name);
        int id_friend= connectNow.getID(name_friend);

        if(!name_friend.equals(name)) {
            if (connectNow.verifyIsAccountExistDataBase(name_friend, "useraccounts", "UserName", id_friend) && !connectNow.verifyIsAccountExistDataBase(name_friend, "friends", "friend_name", id_account) &&
                    !connectNow.verifyIsAccountExistDataBase(name_friend, "friendsrequest", "friend_name", id_account)) {
                accontGLable.setText("The request is sent.");
                connectNow.addInDatabase(name_friend, id_account,"friendsrequest");
                listViewRequest.getItems().add(name_friend);
            } else if (!connectNow.verifyIsAccountExistDataBase(name_friend, "useraccounts", "UserName", id_friend)) {
                accontWLable.setText("The account do not exist. \nPlies insert a valid name.");
            } else if (connectNow.verifyIsAccountExistDataBase(name_friend, "friends", "friend_name", id_account)) {
                accontGLable.setText("You already have this friend.");
            } else if (connectNow.verifyIsAccountExistDataBase(name_friend, "friendsrequest", "friend_name", id_account)) {
                accontGLable.setText("The request has already been sent.");
            }
        }
    }

}
