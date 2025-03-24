module com.example.musicspot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.j;
    requires javafx.media;


    opens com.example.musicspot to javafx.fxml;
    exports com.example.musicspot;
}