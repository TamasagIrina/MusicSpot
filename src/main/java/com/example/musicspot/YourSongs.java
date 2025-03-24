package com.example.musicspot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
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

public class YourSongs implements Initializable {
    @FXML
    private Circle circle;
    @FXML
    private Label nameLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Label songLabel, progresTime, endTime;
    @FXML
    private Label songLabel1;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgresBar;
    @FXML
    private ImageView songImage;

    private File diectory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumbre;
    private double[] speeds={0.25, 0.50, 0.75, 1.00, 1.25, 1.75, 2.00};
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private String musicName;
    private String musicArtist;
    private String musicImg;

    @FXML
    private ListView<String> listViewSongs;

    private Media media;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> name_songs=new ArrayList<>();

    public static String name;
    public static void setName(String name) {
        YourSongs.name = name;
    }

    public void cancelButtonOnAction() {
        Stage stage=(Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void backButtonOnaction(ActionEvent e) throws IOException {
        SwitchTo s= new SwitchTo();
        s.switchToScreen(e, "music.fxml");
        pauseMedia();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image("C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\round-account-button-with-user-inside.png");
        circle.setFill(new ImagePattern(img));

        nameLabel.setText(name+"'s");

        ArrayList<String> songAdded;

        try {
           songAdded= getAddedSongsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        songs= new ArrayList<File>();
        diectory= new File("music");

        files= diectory.listFiles();
        boolean exist=false;
        if(files!=null){

            for(File file : files){
                for(String s : songAdded){
                    if(file.getName().equals(s)){
                        exist=true;
                        songs.add(file);
                    }
                }
            }
        }
        if(exist) {
            DatabaseConection c = new DatabaseConection();
            ArrayList<String> songsList = c.getListOfSongs(songAdded);
            for (String songs : songsList) {
                listViewSongs.getItems().add(songs);
            }
            listViewSongs.setOnMouseClicked(mouseEvent -> {

                if (running) {
                    mediaPlayer.stop();
                }
                String selected = listViewSongs.getSelectionModel().getSelectedItem();
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

            if (running) {
                mediaPlayer.stop();
            }

            try {
                getMusicIT();
            } catch (SQLException e) {
                System.out.println("something wrong with the setting image, title or artist name");
            }


            for (double speed : speeds) {

                speedBox.getItems().add(Double.toString(speed));
            }

            speedBox.setOnAction(this::changeSpeed);


        }else {
            songLabel.setText("PLAYLIST EMPTY");
        }

    }

    private ArrayList<String> getAddedSongsFromDatabase() throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();

        DatabaseConection conection =new DatabaseConection();
        int id = conection.getID(name);

        String getSongs="SELECT music_name FROM playlist where idUserAccounts="+id+";";


        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery(getSongs);
        ArrayList<String> playListSongs= new ArrayList<>();
        if(rs.next()){
            do{
                playListSongs.add(rs.getString(1));

            }while(rs.next());
        }
        else{
            System.out.println("Record Not Found...");
        }
        return playListSongs;
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
        name_songs.add(musicName);

    }

    public void onScreenAction() {
        listViewSongs.getSelectionModel().clearSelection();
    }

    public void pauseMedia(){
        if(running){
            mediaPlayer.pause();
            running=false;
        }
    }

    public void playMedia(){
        beginTimer();
        changeSpeed(null);
        running=true;
        mediaPlayer.play();
    }

    public void nextMedia(){
        if (songNumbre<songs.size()-1){
            songNumbre++;
            if(running){
                cancelTimer();
                mediaPlayer.pause();
            }
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
            if(running){
                cancelTimer();
                mediaPlayer.pause();}

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

    }

    public void resetMedia(){
        songProgresBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void priviesMedia(){
        if (songNumbre > 0){
            songNumbre--;
            if(running){
                cancelTimer();
                mediaPlayer.pause();
            }
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
            if(running){
                cancelTimer();
                mediaPlayer.pause();
            }
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

    }

    public void changeSpeed(ActionEvent event){
        if (speedBox.getValue()==null){
            mediaPlayer.setRate(1);
        }else {
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue()));}
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

