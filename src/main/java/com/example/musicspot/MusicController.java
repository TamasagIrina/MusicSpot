package com.example.musicspot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MusicController implements Initializable {

    @FXML
    private Label songLabel, songLabel1, warningLabel, findLabel, profilName, progresTime, endTime;

    @FXML
    private ComboBox<String> speedBox;

    @FXML
    private Circle circle;

    @FXML
    private ProgressBar songProgresBar;

    @FXML
    private ImageView songImage, loopImg;
    @FXML
    private Button cancelButton;

    private File diectory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumbre;
    private double[] speeds={0.25, 0.50, 0.75, 1.00, 1.25, 1.75, 2.00};

    private Timer timer;
    private TimerTask task;
    private boolean running;

    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private ListView<String> musicList;
    @FXML
    private TextField musicField;
    private static String name;
    private String musicName;
    private String musicArtist;
    private String musicImg;
    public ArrayList<String> songsList;

    public static void setName(String name){
        MusicController.name = name;
    }

    public void backButtonAction(ActionEvent e) throws IOException {
        mediaPlayer.stop();
        if (running){
            cancelTimer();
        }
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e, "login.fxml");

    }

    public void addMusicToDatabase(String musicTitle) throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();
        int id= connectNow.getID(name);
        String addIn="INSERT INTO playlist(music_name, idUserAccounts) \n" +
                "VALUES (\""+musicTitle+"\","+id+");";
        Statement statement= connectDB.createStatement();
        statement.execute(addIn);
    }

    public void verifyIfMusicIsInPlaylist() throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();

        ;
        int id= connectNow.getID(name);

        String verifyExist ="SELECT count(1) FROM playlist WHERE music_name ="+ "\"" + songs.get(songNumbre).getName() +  "\"" + " AND idUserAccounts="+id+" ;";
        try {

            Statement statement= connectDB.createStatement();
            ResultSet queryResut = statement.executeQuery(verifyExist);

            while (queryResut.next()){
                if(queryResut.getInt(1)==1){

                    warningLabel.setText("You already have this song in your playlist");
                    System.out.println("Melodia e deja in playlist");

                }else {
                    addMusicToDatabase(songs.get(songNumbre).getName());
                    warningLabel.setText("You added this song to your playlist");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songProgresBar.setStyle("-fx-accent:  #7193ab;");

        Image img = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\round-account-button-with-user-inside.png");
        circle.setFill(new ImagePattern(img));

        profilName.setText(name);
        YourSongs.setName(name);
        AddAFriend.setName(name);
        YourFirends.setName(name);

        songs= new ArrayList<File>();
        diectory= new File("music");

        files= diectory.listFiles();

        if(files!=null){
            for(File file : files){
                songs.add(file);
            }
        }
        ArrayList<String>songs_file=new ArrayList<>();
        for(int i=0;i<songs.size();i++){
            songs_file.add(songs.get(i).getName());
        }
        DatabaseConection c=new DatabaseConection();
        songsList= c.getListOfSongs(songs_file);
        for (String song:songsList){
            musicList.getItems().add(song);
        }
        musicList.setOnMouseClicked(mouseEvent -> {
            findLabel.setText("");
            warningLabel.setText("");
            if (running) {
                mediaPlayer.pause();
            }
            String selected = musicList.getSelectionModel().getSelectedItem();
            String file = c.getSongFile(selected);

            for (int i = 0; i < songs.size(); i++) {
                if (file.equals(songs.get(i).getName())) {
                    songNumbre = i;
                }
            }


            media = new Media(songs.get(songNumbre).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            try {
                getMusicIT();
            } catch (SQLException e) {
                System.out.println("something wrong with the setting image, title or artist name");
            }
        });

        media = new Media(songs.get(songNumbre).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        try {
            getMusicIT();
        }catch (SQLException e){
            System.out.println("Something wrong with the setting image, title or artist name");
        }

        for (double speed : speeds) {
            speedBox.getItems().add(Double.toString(speed));
        }

        speedBox.setOnAction(this::changeSpeed);


    }

    @FXML
    public void onScreenAction() {
        musicList.getSelectionModel().clearSelection();
        findLabel.setText("");
        warningLabel.setText("");
    }

    public void friendsButtonOnAction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e,"friends.fxml");
        pauseMedia();
    }
    public void addFriendsButtonOnAction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e,"addAFriend.fxml");
        pauseMedia();
    }
    public void playlistButtonOnAction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e,"yoursongs.fxml");
        pauseMedia();
    }

    public void clickOnField(){
        EventHandler<KeyEvent> eventHandlerTextField = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                findLabel.setText("");
                warningLabel.setText("");
                musicList.getSelectionModel().clearSelection();
            }
        };
        musicField.addEventHandler(KeyEvent.KEY_TYPED, eventHandlerTextField);
    }

    public void findButtonOnAction(){
        boolean find= false;
        DatabaseConection c=new DatabaseConection();
        if(!musicField.getText().isBlank()){
            String field= musicField.getText();
            for (String song:songsList){
                 if(musicField.getText().equals(song)){
                     find=true;
                 }
            }
            if(find){
                for(int i=0;i<songs.size();i++){
                    if(c.getSongFile(musicField.getText()).equals(songs.get(i).getName())){
                        songNumbre=i;
                    }
                }
                if (running){
                    mediaPlayer.stop();
                }
                media = new Media(songs.get(songNumbre).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                try {
                    getMusicIT();
                }catch (SQLException e){
                    System.out.println("Something wrong with the setting image, title or artist name");
                }
            }else{
                findLabel.setText("The song in not available");
            }
        }else {
            findLabel.setText("Please insert a song");
        }
    }

    public void getMusicIT() throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();

        String musicFile=songs.get(songNumbre).getName();


        String getSongs="SELECT music_name, music_artist , music_img FROM musiclist WHERE music_file=\""+musicFile+"\";";

        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery(getSongs);
        if(rs.next()){
            do{
                musicName=rs.getString(1);
                musicArtist=rs.getString(2);
                musicImg=rs.getString(3);
               

            }while(rs.next());
        }
        else{
            System.out.println("Record Not Found...");
        }
        songLabel.setText(musicName);
        songLabel1.setText(musicArtist);
        Image image = new Image(musicImg);
        songImage.setImage(image);

    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
        if(running){
            cancelTimer();
            mediaPlayer.pause();
        }
    }

    public void pauseMedia(){
        if (running){
            mediaPlayer.pause();
            cancelTimer();
        }
    }

    public void playMedia(){
       beginTimer();
       changeSpeed(null);
       mediaPlayer.play();
    }

    public void nextMedia(){
        if (running){
            cancelTimer();
            mediaPlayer.stop();
        }
        if (songNumbre<songs.size()-1){
            songNumbre++;

            media = new Media(songs.get(songNumbre).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            try {
                getMusicIT();
            }catch (SQLException e){
                System.out.println("something wrong with the setting image, title or artist name");
            }
            songProgresBar.setProgress(0);
            playMedia();
        }else {
            songNumbre=0;
            media = new Media(songs.get(songNumbre).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            try {
                getMusicIT();
            }catch (SQLException e){
                System.out.println("something wrong with the setting image, title or artist name");
            }
            songProgresBar.setProgress(0);
            playMedia();
        }
        warningLabel.setText("");
    }

    public void resetMedia(){
        songProgresBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void priviesMedia(){
        if (running){
            cancelTimer();
            mediaPlayer.stop();
        }
        if (songNumbre > 0){
            songNumbre--;
            media = new Media(songs.get(songNumbre).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            try {
                getMusicIT();
            }catch (SQLException e){
                System.out.println("something wrong with the setting image, title or artist name");
            }
            songProgresBar.setProgress(0);
            playMedia();
        }else {
            songNumbre=songs.size()-1;
            media = new Media(songs.get(songNumbre).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            try {
                getMusicIT();
            }catch (SQLException e){
                System.out.println("something wrong with the setting image, title or artist name");
            }
            songProgresBar.setProgress(0);
            playMedia();
        }
       warningLabel.setText("");
    }

    public void changeSpeed(ActionEvent event){
        if (speedBox.getValue()==null){
            mediaPlayer.setRate(1);
        }else {
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));
        }
    }

    public void beginTimer(){
        timer = new Timer();

        task= new TimerTask() {

            @Override
            public void run() {
                running =true;
                double current;
                if(mediaPlayer!=null)  {
                    try {
                        Thread.sleep(500);
                    }catch (Exception e){
                        System.out.println("Sleep Error");
                    }
                    try {

                        current = mediaPlayer.getCurrentTime().toSeconds();
                        double end = media.getDuration().toSeconds();
                        songProgresBar.setProgress(current/end);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                String progress = String.format("%.2f", current/end);
                                progresTime.setText(progress);
                                String endT = String.format("%.2f", end*1/100);
                                endTime.setText(endT);
                            }
                        });

                        if (current/end==1){
                            cancelTimer();
                        }
                    }catch (Exception e){

                    }

                }

            }
        };
        timer.scheduleAtFixedRate(task, 1050, 100);
    }

    public void cancelTimer(){
        running= false;
        timer.cancel();

    }

}
