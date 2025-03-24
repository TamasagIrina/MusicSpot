package com.example.musicspot;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchTo {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScreen(ActionEvent event, String screen) throws IOException {
        FXMLLoader loder =  new FXMLLoader(getClass().getResource(screen));
        root=loder.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
