package ca.bcit.comp2522.termproject.lyxz;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * PuzzleGame, an introduction to animation (stop, seriously?!).
 */
public class PuzzleGame extends Application {
    private static final int SIZE = 4;
    private static final int PIECE_SIZE = 105;
    private final int[][] data = new int[SIZE][SIZE];
    private GridPane gridPane;
    private Label timerLabel;
    private Label moveCounterLabel;
    private int moveCount = 0;
    private Timeline timeline;
    private Integer timeSeconds = 0;

    /**
     * The start method is public and it returns nothing (void). We
     * create our JavaFX application here.
     */

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(1);
        gridPane.setHgap(1);

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(gridPane);

        initializeData();
        shuffleData();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != 0) {
                    String imagePath = getClass().getResource("/food1/" + data[i][j] + ".jpg").toExternalForm();
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(PIECE_SIZE);
                    imageView.setFitHeight(PIECE_SIZE);
                    gridPane.add(imageView, j, i);
                }
            }
        }

        MenuItem restartItem = new MenuItem("Restart");

        VBox root = new VBox();
        VBox.setVgrow(rootPane, Priority.ALWAYS);
        root.getChildren().add(rootPane);

        Scene scene = new Scene(root, 603, 680);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    moveTiles(Direction.UP);
                    break;
                case DOWN:
                    moveTiles(Direction.DOWN);
                    break;
                case LEFT:
                    moveTiles(Direction.LEFT);
                    break;
                case RIGHT:
                    moveTiles(Direction.RIGHT);
                    break;
                default:
                    break;
            }
            updateGridPane(gridPane);
        });

        primaryStage.setTitle("PuzzleQuest");
        primaryStage.setScene(scene);
        primaryStage.show();

        moveCounterLabel = new Label("Moves: 0");
        moveCounterLabel.setFont(new Font("Arial", 20));

        timerLabel = new Label("Time: 0");
        timerLabel.setFont(new Font("Arial", 20));

        HBox statusLayout = new HBox(100);
        statusLayout.setAlignment(Pos.TOP_CENTER);
        statusLayout.getChildren().addAll(timerLabel, moveCounterLabel);

        rootPane.getChildren().add(1, statusLayout);

        startTimer();
    }

    /**
     * Moves the tiles in the specified direction.
     */
    private void moveTiles(Direction direction) {
        int emptyX = -1, emptyY = -1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] == 0) {
                    emptyX = j;
                    emptyY = i;
                    break;
                }
            }
        }
        int targetX = emptyX, targetY = emptyY;
        switch (direction) {
            case UP:
                targetY++;
                break;
            case DOWN:
                targetY--;
                break;
            case LEFT:
                targetX++;
                break;
            case RIGHT:
                targetX--;
                break;
        }
        if (targetX >= 0 && targetX < SIZE && targetY >= 0 && targetY < SIZE) {
            data[emptyY][emptyX] = data[targetY][targetX];
            data[targetY][targetX] = 0;
            moveCount++;
            moveCounterLabel.setText("Moves: " + moveCount);
            if (isSolved()) {
                if (timeline != null) {
                    timeline.stop();
                }
                showSolvedFeedback();
            }
        } else {
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1)
            );
            visiblePause.play();
        }
    }

    /**
     * Represents directions in which a tile can be moved.
     */
    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Initializes the data by filling it with ordered numbers.
     */
    private void initializeData() {
        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                data[i][j] = count;
                count++;
            }
        }
        data[SIZE - 1][SIZE - 1] = 0;
    }

    /**
     * Shuffles the data in the array by performing multiple tile moves.
     */
    private void shuffleData() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            list.add(i + 1);
        }
        list.set(SIZE * SIZE - 1, 0);

        Collections.shuffle(list);

        for (int i = 0; i < SIZE * SIZE; i++) {
            data[i / SIZE][i % SIZE] = list.get(i);
        }
    }

    /**
     * Updates the grid pane by clearing it and adding the tiles from the data.
     */
    private void updateGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != 0) {
                    String imagePath = getClass().getResource("/food1/" + data[i][j] + ".jpg").toExternalForm();
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(PIECE_SIZE);
                    imageView.setFitHeight(PIECE_SIZE);
                    gridPane.add(imageView, j, i);
                }
            }
        }
    }

    /**
     * Starts the timer.
     */
    private void startTimer() {
        timeSeconds = 0;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the timer label every second.
     */
    private void updateTimer() {
        timeSeconds++;
        timerLabel.setText("Time: " + timeSeconds);
    }

    /**
     * Checks if the puzzle is solved.
     */
    private boolean isSolved() {
        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != count) {
                    if (!(i == SIZE - 1 && j == SIZE - 1 && data[i][j] == 0)) {
                        return false;
                    }
                }
                if (count < SIZE * SIZE) {
                    count++;
                }
            }
        }
        return true;
    }

    /**
     * Shows a dialog with the specified message.
     */
    private void showSolvedFeedback() {
        Alert solvedAlert = new Alert(Alert.AlertType.INFORMATION);
        solvedAlert.setTitle("Congratulations! You won");
        solvedAlert.setHeaderText(null);
        solvedAlert.setContentText("You've solved the puzzle in " + moveCount + " moves and " + timeSeconds + " seconds.");

        solvedAlert.showAndWait();
    }

    /**
     * The main method is needed for the IDE to be able to run the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}