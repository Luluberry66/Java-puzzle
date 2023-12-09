package ca.bcit.comp2522.termproject.lyxz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The PuzzleGame class.
 * @version 2023
 * @author Lulu Dong
 */
public class PuzzleGame extends Application {
    /**
     * The width and height of the window.
     */
    public static final double WIDTH = 603;
    /**
     * The width and height of the window.
     */
    public static final double HEIGHT = 680;
    /**
     * Init method.
     */
    @Override
    public void init() {
        GameUtils.preloadSound("move.mp3");
        GameUtils.preloadSound("invalid.mp3");
        GameUtils.preloadSound("win.mp3");
    }

    /**
     * Start method.
     * @param primaryStage the primary stage
     */
    @Override
    public void start(final Stage primaryStage) {
        GameLogic gameLogic = new GameLogic();
        GameUI gameUI = new GameUI(gameLogic);

        gameLogic.initializeData();
        gameLogic.shuffleData();

        gameUI.updateGridPane();
        Scene scene = new Scene(gameUI.getRoot(), WIDTH, HEIGHT);
        primaryStage.setTitle("PuzzleQuest");
        primaryStage.setScene(scene);
        primaryStage.show();
        gameUI.setupUI(scene, primaryStage);
        gameUI.playRandomMusic();
    }

    /**
     * The main method.
     * @param args the arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
