package ca.bcit.comp2522.termproject.lyxz;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Interface for the GameUI class.
 * @version 2023
 * @author Lulu Dong
 */
public interface IGameUI {
    /**
     * Sets up the UI.
     * @param scene the scene
     * @param stage the stage
     */
    void setupUI(Scene scene, Stage stage);

    /**
     * Updates the grid pane.
     */
    void updateGridPane();

    /**
     * Changes the theme.
     * @param theme the theme
     */
    void changeTheme(String theme);

    /**
     * Restarts the game.
     */
    void restartGame();

    /**
     * Shows the about info.
     */
    void showAboutInfo();

    /**
     * Shows the help info.
     */
    void startTimer();

    /**
     * Updates the timer.
     */
    void updateTimer();
}
