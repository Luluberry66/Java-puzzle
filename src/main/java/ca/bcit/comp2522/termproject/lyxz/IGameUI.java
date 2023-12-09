package ca.bcit.comp2522.termproject.lyxz;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface IGameUI {
    void setupUI(Scene scene, Stage stage);
    void updateGridPane();
    void changeTheme(String theme);
    void restartGame();
    void showAboutInfo();
    void startTimer();
    void updateTimer();
}
