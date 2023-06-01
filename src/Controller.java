import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser;


public class Controller implements Initializable {
   
    @FXML
    private BorderPane rootBox;

    @FXML
    private HBox settingsBar;

    @FXML
    private Label openFileLable;

    @FXML
    private HBox timerBox;

    @FXML
    private Label currentTimeLable;

    @FXML
    private Label totalTimeLable;

    @FXML
    private Slider timeSlider;

    @FXML
    private HBox controlsBox;

    @FXML
    private Button pausePlayButton;

    @FXML
    private Button backwardButton;

    @FXML
    private Button forwardButton;

 
    @FXML
    private Label speedLable;

    @FXML
    private Label fullScreenLabel;

    @FXML
    private HBox volumeBox;

    @FXML
    private Label volumeLable;

    @FXML
    private Slider volumeSlider;

    @FXML
    private MediaView MVField;
    private MediaPlayer mediaPlayer ;
    private Media media;
    private boolean isPlaying = false;
    private boolean isMuted = false;
    private boolean isFullScreen = false;
    private double [] availableSpeeds = {1,1.25,1.5,1.75,2};
    private int speedIndex = 1;

    private ImageView playIV;
    private ImageView pauseIV;
    private ImageView forwardIV;
    private ImageView backwordIV;
    private ImageView volumeIV;
    private ImageView muteIV;
    private ImageView expandIV;
    private ImageView compressIV;
    
    private static final String SLIDER_STYLE_FORMAT = 
    "-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
            + "-slider-filled-track-color %1$f%%, -fx-base %1$f%%, -fx-base 100%%);";

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

        Image playIcon = new Image(new File("icons\\play.png").toURI().toString());
        playIV = new ImageView(playIcon);
        playIV.setFitHeight(25);
        playIV.setFitWidth(25);
        pausePlayButton.setGraphic(playIV);
        pausePlayButton.setContentDisplay(ContentDisplay.CENTER);
        pausePlayButton.setPrefHeight(50);
        pausePlayButton.setPrefWidth(50);

        Image pauseIcon = new Image(new File("icons\\pause.png").toURI().toString());
        pauseIV = new ImageView(pauseIcon);
        pauseIV.setFitHeight(25);
        pauseIV.setFitWidth(25);


        Image forwardIcon = new Image(new File("icons\\forward.png").toURI().toString());
        forwardIV = new ImageView(forwardIcon);
        forwardIV.setFitHeight(25);
        forwardIV.setFitWidth(25);
        forwardButton.setGraphic(forwardIV);
        forwardButton.setContentDisplay(ContentDisplay.CENTER);

        Image backwordIcon = new Image(new File("icons\\backward.png").toURI().toString());
        backwordIV = new ImageView(backwordIcon);
        backwordIV.setFitHeight(25);
        backwordIV.setFitWidth(25);
        backwardButton.setGraphic(backwordIV);
        backwardButton.setContentDisplay(ContentDisplay.CENTER);

        Image volumeIcon = new Image(new File("icons\\high-volume.png").toURI().toString());
        volumeIV = new ImageView(volumeIcon);
        volumeIV.setFitHeight(25);
        volumeIV.setFitWidth(25);
        volumeLable.setGraphic(volumeIV);


        Image fullScreenIcon = new Image(new File("icons\\expand.png").toURI().toString());
        expandIV = new ImageView(fullScreenIcon);
        expandIV.setFitHeight(25);
        expandIV.setFitWidth(25);
        fullScreenLabel.setGraphic(expandIV);

        Image exitFullScreenIcon = new Image(new File("icons\\compress.png").toURI().toString());
        compressIV = new ImageView(exitFullScreenIcon);
        compressIV.setFitHeight(25);
        compressIV.setFitWidth(25);
        
        Image muteIcon = new Image(new File("icons\\mute.png").toURI().toString());
        muteIV = new ImageView(muteIcon);
        muteIV.setFitHeight(25);
        muteIV.setFitWidth(25);

        adjustFocus();
       
        volumeLable.setOnMouseClicked(e -> {
            if (isMuted) {
                mediaPlayer.setMute(false);
                volumeLable.setGraphic(volumeIV);
                isMuted = false;
                volumeBox.getChildren().add(volumeSlider);
            } else {
                mediaPlayer.setMute(true);
                volumeLable.setGraphic(muteIV);
                isMuted = true;
                volumeBox.getChildren().remove(volumeSlider);
            }
            rootBox.requestFocus();
        });

        pausePlayButton.setOnAction(e -> {
            if (isPlaying) {
                mediaPlayer.pause();
                pausePlayButton.setGraphic(playIV);
                isPlaying = false;
            } else {
                mediaPlayer.play();
                pausePlayButton.setGraphic(pauseIV);
                isPlaying = true;
            }
            
            rootBox.requestFocus();
        });
        
        forwardButton.setOnAction(e -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
            rootBox.requestFocus();
        });

        backwardButton.setOnAction(e -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
            rootBox.requestFocus();
        });

        rootBox.setOnKeyPressed(e->{
            switch (e.getCode()) {
                case SPACE:
                    if(isPlaying){
                        mediaPlayer.pause();
                        pausePlayButton.setGraphic(playIV);
                        isPlaying = false;
                    }else{
                        mediaPlayer.play();
                        pausePlayButton.setGraphic(pauseIV);
                        isPlaying = true;
                    }
                    break;
                case ESCAPE:
                    if(isFullScreen){
                        Stage stage = (Stage) MVField.getScene().getWindow();
                        stage.setFullScreen(false);
                        fullScreenLabel.setGraphic(expandIV);
                        rootBox.getChildren().add(settingsBar);
                        isFullScreen = false;
                    }
                    break;
                case LEFT:
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
                    break;
                case RIGHT:
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
                    break;
                default:
                    break;
            }
        });

        fullScreenLabel.setOnMouseClicked(e ->{
            Stage stage = (Stage) MVField.getScene().getWindow();
            if(!isFullScreen){
                isFullScreen = true;
                stage.setFullScreen(true);
                fullScreenLabel.setGraphic(compressIV);
                rootBox.getChildren().remove(settingsBar);
            }
            else{
                isFullScreen = false;
                stage.setFullScreen(false);
                fullScreenLabel.setGraphic(expandIV);
                rootBox.getChildren().add(settingsBar);
            }
            rootBox.requestFocus();

        });

        speedLable.setOnMouseClicked(e->{
            speedLable.setText(String.valueOf(availableSpeeds[speedIndex] + "X"));
            mediaPlayer.setRate(availableSpeeds[speedIndex++]);
            if(speedIndex == availableSpeeds.length){
                speedIndex = 0;
            }
            rootBox.requestFocus();
        });

        MVField.setOnMouseClicked(e->{
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                Stage stage = (Stage) MVField.getScene().getWindow();
                if(!isFullScreen){
                    isFullScreen = true;
                    stage.setFullScreen(true);
                    fullScreenLabel.setGraphic(compressIV);
                }
                else{
                    isFullScreen = false;
                    stage.setFullScreen(false);
                    fullScreenLabel.setGraphic(expandIV);
                }
            }
        });
        
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
               
                mediaPlayer.setVolume(volumeSlider.getValue());
                
                if(mediaPlayer.getVolume() == 0){
                    volumeLable.setGraphic(muteIV);
                    isMuted = true;
                    volumeBox.getChildren().remove(volumeSlider);
                }
                else{
                    volumeLable.setGraphic(volumeIV);
                    isMuted = false;
                    volumeBox.getChildren().add(volumeSlider);
                }
            }
        });

        timeSlider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (timeSlider.getValue() - timeSlider.getMin()) / (timeSlider.getMax() - timeSlider.getMin()) * 100.0 ;
            return String.format(SLIDER_STYLE_FORMAT, percentage);
        }, timeSlider.valueProperty(), timeSlider.minProperty(), timeSlider.maxProperty()));
        timeSlider.setDisable(true);

        openFileLable.setOnMouseClicked(e-> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a Video");
            Stage primaryStage = (Stage) MVField.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            
            if(selectedFile != null){
                String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
                if(!fileExtension.equals("mp4"))
                    return;

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    if(isPlaying == true){
                        pausePlayButton.setGraphic(playIV);
                        isPlaying = false;
                    } 
        
                    if(isMuted == true){
                        volumeLable.setGraphic(volumeIV);
                        volumeBox.getChildren().add(volumeSlider);
                        isMuted = false;

                    }

                    if(speedIndex != 0){
                        speedIndex = 0;
                        speedLable.setText(String.valueOf(availableSpeeds[speedIndex] + "X"));
                        mediaPlayer.setRate(availableSpeeds[speedIndex++]);
                    }
                }

                createNewMedia(selectedFile.toURI().toString());
                bindTimeSlider();
            }
        });

        rootBox.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> arg0, Scene oldScene, Scene newScene) {
                if(oldScene == null && newScene != null){
                    MVField.fitHeightProperty().bind(newScene.heightProperty().subtract(timerBox.heightProperty()).subtract(controlsBox.heightProperty()).subtract(settingsBar.heightProperty()));
                    MVField.fitWidthProperty().bind(newScene.widthProperty());
                }   
            }
        }); 
    }

    public void adjustFocus(){
        pausePlayButton.setFocusTraversable(false);
        forwardButton.setFocusTraversable(false);
        backwardButton.setFocusTraversable(false);
        volumeSlider.setFocusTraversable(false);
        timeSlider.setFocusTraversable(false);
        Platform.runLater(() -> rootBox.requestFocus());
    }

    public void createNewMedia(String path){
        media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        MVField.setMediaPlayer(mediaPlayer);
        mediaPlayer.setVolume(volumeSlider.getValue());
    }

    public void bindTimeSlider(){
        timeSlider.setDisable(false);

        mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                timeSlider.setMax(newValue.toSeconds());
                totalTimeLable.setText(getFormatedTime(newValue));
             }
             
        });

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(!timeSlider.isValueChanging() && newValue != null){
                   timeSlider.setValue(newValue.toSeconds());
                }
                currentTimeLable.setText(getFormatedTime(newValue));
            }
        });

        timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!timeSlider.isValueChanging() ) {
                   mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            }
            
        });

         timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }

                if(timeSlider.getValue() == timeSlider.getMax()){
                    mediaPlayer.pause();
                    pausePlayButton.setGraphic(playIV);
                    isPlaying = false;
                }
                else{
                    mediaPlayer.play();
                    pausePlayButton.setGraphic(pauseIV);
                    isPlaying = true;
                }
            }
         });

         mediaPlayer.setOnEndOfMedia(()->{                   
            timeSlider.setValue(timeSlider.getMax());
        });
    }

    public String getFormatedTime(Duration duration){
        int hours = (int) duration.toHours();
        int minutes = (int) duration.toMinutes() % 60;
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
