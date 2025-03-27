package com.example.musicspot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class YourFirends implements Initializable {
    private static String name;
    @FXML
    private Circle circle;
    @FXML
    private Button cancelButton;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<String> listVirwYourRequest;
    @FXML
    private ListView<String> listVirwYourFriends;

    public static void setName(String name) {
        YourFirends.name = name;
    }

    public void cancelButtonOnAction() {
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void backButtonOnaction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e, "music.fxml");

    }
    public void onScreenAction() {
        listVirwYourRequest.getSelectionModel().clearSelection();
        listVirwYourFriends.getSelectionModel().clearSelection();

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\round-account-button-with-user-inside.png");
        circle.setFill(new ImagePattern(img));
        nameLabel.setText(name);
        DatabaseConection conection= new DatabaseConection();


        try {
            int id_account= conection.getID(name);
            ArrayList<Integer>idRequestFriend=getFriendsFromDatabase(name);
            ArrayList<String> friends= conection.getFriendsFromDatabase(id_account, "friends");

            for (String friend : friends) {
                listVirwYourFriends.getItems().add(friend);
            }
            for (Integer idFriend : idRequestFriend) {
                String name = conection.getname(idFriend);
                listVirwYourRequest.getItems().add(name);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public ArrayList<Integer> getFriendsFromDatabase( String name){
        DatabaseConection conection= new DatabaseConection();
        Connection connectDB= conection.getConnection();
        ArrayList<Integer> ids =new ArrayList<>();

        String segment="SELECT idUserAccounts FROM friendsrequest WHERE friend_name='"+name+"';";
        try {

            Statement statement= connectDB.createStatement();
            ResultSet queryResut = statement.executeQuery(segment);


            while (queryResut.next()){
                ids.add((int) queryResut.getLong(1));
            }


        }catch (Exception e){
            e.printStackTrace();

        }
        return ids;
    }
    public void alert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setContentText("Select username");
        alert.showAndWait();
    }

    public void acceptButtonOnAction() throws SQLException {

        String selected= listVirwYourRequest.getSelectionModel().getSelectedItem();

        if(selected!=null){
            DatabaseConection connectNow = new DatabaseConection();
            int id = connectNow.getID(selected);

            if(connectNow.verifyIsAccountExistDataBase(name, "friendsrequest","friend_name", id)){
                connectNow.addInDatabase(selected,connectNow.getID(name),"friends");
                connectNow.addInDatabase(name,connectNow.getID(selected),"friends");

                declineButtonOnAction();

                listVirwYourFriends.getItems().add(selected);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Expired");
                alert.setContentText("The request expired. ");
                alert.showAndWait();
                listVirwYourRequest.getItems().remove(selected);
            }
        }else{
            alert();
        }
    }


    public void declineButtonOnAction() throws SQLException {

        String selected= listVirwYourRequest.getSelectionModel().getSelectedItem();

        if(selected != null){
            DatabaseConection connectNow = new DatabaseConection();
            Connection connectDB= connectNow.getConnection();

            String delete="DELETE FROM friendsrequest WHERE idUserAccounts="+connectNow.getID(selected)+";";
            Statement statement= connectDB.createStatement();
            statement.execute(delete);
            listVirwYourRequest.getItems().remove(selected);

        }else {
            alert();
        }
    }
    public void deleteButtonOnAction() throws SQLException {
        String selected= listVirwYourFriends.getSelectionModel().getSelectedItem();

        if(selected != null){
            DatabaseConection connectNow = new DatabaseConection();
            if(connectNow.verifyIsAccountExistDataBase(name, "friends","friend_name", connectNow.getID(selected))) {
                connectNow.delete("friends", selected, connectNow.getID(name));
                connectNow.delete("friends", name, connectNow.getID(selected));
                listVirwYourFriends.getItems().remove(selected);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deleted");
                alert.setContentText("The account is deleted.");
                alert.showAndWait();
                listVirwYourFriends.getItems().remove(selected);
            }
        }else {
            alert();
        }

    }

    public void seePlaylistButtonOnAction(ActionEvent e) throws IOException, SQLException {

        if(listVirwYourFriends.getSelectionModel().getSelectedItem()!=null){
            DatabaseConection connectNow = new DatabaseConection();
            if(connectNow.verifyIsAccountExistDataBase(name, "friends","friend_name", connectNow.getID(listVirwYourFriends.getSelectionModel().getSelectedItem()))) {
                YourSongs.setName(listVirwYourFriends.getSelectionModel().getSelectedItem());
                SwitchTo s = new SwitchTo();
                s.switchToScreen(e, "yoursongs.fxml");
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deleted");
                alert.setContentText("The account is deleted. ");
                alert.showAndWait();
                listVirwYourFriends.getItems().remove(listVirwYourFriends.getSelectionModel().getSelectedItem());
            }
        }else {
            alert();
        }

    }


}
