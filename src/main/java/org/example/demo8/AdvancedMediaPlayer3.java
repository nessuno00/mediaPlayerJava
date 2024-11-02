package org.example.demo8;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class AdvancedMediaPlayer3 extends Application {

    private MediaView mediaView;

    private MediaPlayer mediaPlayer;
    private Slider progressBar;
    private Slider volumeControl;
    private ComboBox<Double> speedControl;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Barra di avanzamento
        progressBar = new Slider();
        progressBar.setOnMousePressed(e -> {
        });

        // Controllo del volume
        volumeControl = new Slider(0, 1, 0.5);
        volumeControl.setValue(0.5);
        volumeControl.valueProperty().addListener((obs,oldVal, newVal) -> mediaPlayer.setVolume(newVal.doubleValue()));

        Button  volumeUp =  new Button("+");

        volumeUp.setOnAction( e -> mediaPlayer.setVolume(Math.min(1.0, mediaPlayer.getVolume()) + 0.1));

        Button volumeDown = new Button("-");

        volumeDown.setOnAction(e -> mediaPlayer.setVolume(Math.max(0.0, mediaPlayer.getVolume()) - 0.1));

        speedControl = new ComboBox<>();

        speedControl.getItems().addAll(0.5,1.0,1.5,2.0,2.5,3.0);
        speedControl.setValue(0.5);
        speedControl.setOnAction( actionEvent ->  mediaPlayer.setRate(speedControl.getValue()));
        // Menu per caricare file
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openFile = new MenuItem("Open Media File");
        openFile.setOnAction(e -> openMediaFile(primaryStage));
        fileMenu.getItems().add(openFile);
        menuBar.getMenus().add(fileMenu);
        root.setTop(menuBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Advanced Media Player");
        primaryStage.show();
    }

    private void openMediaFile(Stage ownerStage) {
        // Apri il file video
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(ownerStage);
        if (file != null) {

            Stage videoStage = new Stage();
            try {
                Media media = new Media(file.toURI().toASCIIString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);


                BorderPane videoRoot = new BorderPane();
                videoRoot.setCenter(mediaView);

                // Barra di avanzamento
                Slider progressBar = new Slider();
                progressBar.setOnMousePressed(e -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));
                videoRoot.setBottom(progressBar);

                // Controlli di riproduzione
                Button playButton = new Button("Play");
                playButton.setOnAction(e -> mediaPlayer.play());
                Button pauseButton = new Button("Pause");
                pauseButton.setOnAction(e -> mediaPlayer.pause());
                Button stopButton = new Button("Stop");
                stopButton.setOnAction(e -> mediaPlayer.stop());

                HBox controlBox = new HBox(10, playButton, pauseButton, stopButton,volumeControl);
                videoRoot.setTop(controlBox);

                // Imposta la scena e mostra il nuovo stage
                Scene videoScene = new Scene(videoRoot, 800, 600);
                videoStage.setScene(videoScene);
                videoStage.setTitle("Video: " + file.getName());
                videoStage.show();

                // Associa la barra di avanzamento al tempo del video
                mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    progressBar.setValue(newTime.toSeconds());
                });
                mediaPlayer.setOnReady(() -> {
                    progressBar.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
                    mediaPlayer.play(); // Avvia automaticamente la riproduzione
                });

            } catch (Exception e) {
                System.out.println("Errore durante il caricamento del file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
