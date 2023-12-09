package ca.bcit.comp2522.termproject.lyxz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PuzzleGame extends Application {

    private GameUI gameUI;
    private GameLogic gameLogic;

    @Override
    public void init() {
        GameUtils.preloadSound("move.mp3");
        GameUtils.preloadSound("invalid.mp3");
        GameUtils.preloadSound("win.mp3");
    }

    @Override
    public void start(Stage primaryStage) {
        gameLogic = new GameLogic();
        gameUI = new GameUI(gameLogic);

        gameLogic.initializeData();
        gameLogic.shuffleData();

        gameUI.updateGridPane();
        Scene scene = new Scene(gameUI.getRoot(), 603, 680);
        primaryStage.setTitle("PuzzleQuest");
        primaryStage.setScene(scene);
        primaryStage.show();
        gameUI.setupUI(scene, primaryStage);
        gameUI.playRandomMusic();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
