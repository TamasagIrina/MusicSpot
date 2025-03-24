package com.example.musicspot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
       try {
           FXMLLoader root =   new FXMLLoader(getClass().getResource("login.fxml"));

           Scene scene = new Scene(root.load(), 800, 500);
           scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
           stage.initStyle(StageStyle.UNDECORATED);
           stage.setScene(scene);

           stage.show();
           
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    public static void main(String[] args) {
        launch(args);
    }
}