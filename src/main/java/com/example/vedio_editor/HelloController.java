package com.example.vedio_editor;
import com.xuggle.xuggler.Xuggler;
import com.xuggle.xuggler.demos.DecodeAndCaptureFrames;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;

public class HelloController {

    private String path;
    private MediaPlayer mediaPlayer;
    private boolean cutOrNot  =false;



    @FXML
    private MediaView mediaView;

    @FXML
    private Button openFile;
    @FXML
    private TextField startCut,endCut;


    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider speedSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private Label label;

    @FXML
    private StackPane pane;

    @FXML
    private Label welcomeText;
    @FXML
    private Slider videoTimeLine;

    @FXML
    private Button startDelete;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    public void  init(){
        //video TimeLine suits the Video duration

    }

    @FXML
    protected void stopVideo(){
        mediaPlayer.stop();
    }

    @FXML
    protected void pauseVideo(){
        int width = mediaPlayer.getMedia().getWidth();
        int height = mediaPlayer.getMedia().getHeight();
        WritableImage wim = new WritableImage(width, height);
        MediaView mv = new MediaView();
        mv.setFitWidth(width);
        mv.setFitHeight(height);
        mv.setMediaPlayer(mediaPlayer);
        mv.snapshot(null, wim);
        try {
            ImageIO.write((RenderedImage) wim, "png", new File("/test.png"));
        } catch (Exception s) {
            System.out.println(s);
        }
    mediaPlayer.pause();
    }

    @FXML
    protected void playVideo(){
        mediaPlayer.play();

    }

    @FXML
    protected void OpenFileMethod(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a .mp4 file", ".mp4");
//        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if(path != null){
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //creating bindings
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();

            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue()/100));

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                                                              @Override
                                                              public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                                                                  progressBar.setValue(newValue.toSeconds());
                                                              }
                                                          }
            );

            progressBar.setOnMousePressed(event1 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            progressBar.setOnMouseDragged(event12 -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    javafx.util.Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });

            mediaPlayer.play();
        }

        progressBar.setMax(mediaPlayer.getMedia().getDuration().toSeconds());


        startCut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                startCut.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        endCut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
              endCut.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        VideoToFrames.getFrames(file.getAbsolutePath());
        System.out.println(VideoToFrames.images);

    }

    @FXML
    protected  void  seekVideoWithTimeLine(){
        //Todo::Seek Video With this Slider
        videoTimeLine.getValue();
    }

    @FXML
    public void StartDelete(ActionEvent actionEvent) {
        startCut.getText();
        endCut.getText();
        //Then Cut The Video;
        mediaPlayer.getMedia();

    }
}