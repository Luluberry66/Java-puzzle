package ca.bcit.comp2522.termproject.lyxz;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
public class PuzzleGame extends Application {
    private static final int SIZE = 4;
    private static final int PIECE_SIZE = 105;
    private int[][] data = new int[SIZE][SIZE];

    private StackPane rootPane;
    private ImageView previewImageView;
    private GridPane gridPane;

    private Label timerLabel;
    private Label moveCounterLabel;
    private int moveCount = 0;
    private Timeline timeline;
    private Integer timeSeconds = 0;

    private String currentTheme = "Food1";
    private Label feedbackLabel;
    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(1);
        gridPane.setHgap(1);

        rootPane = new StackPane();
        rootPane.getChildren().add(gridPane);

        initializeData();
        shuffleData();

        setRandomBackground();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != 0) {
                    String imagePath = getClass().getResource("/Food1/" + data[i][j] + ".jpg").toExternalForm();
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(PIECE_SIZE);
                    imageView.setFitHeight(PIECE_SIZE);
                    gridPane.add(imageView, j, i);
                }
            }
        }
        MenuBar menuBar = new MenuBar();

        Menu optionsMenu = new Menu("Options");
        Menu aboutMenu = new Menu("About");
        Menu changeThemeMenu = new Menu("Change Theme");


        Menu christmasMenu = new Menu("Christmas");
        Menu animalsMenu = new Menu("Animals");
        Menu foodMenu = new Menu("Food");

        for (int i = 1; i <= 3; i++) {
            String themeNumber = String.valueOf(i);

            MenuItem christmasItem = new MenuItem("Christmas " + themeNumber);
            christmasItem.setOnAction(e -> changeTheme("Christmas" + themeNumber));
            christmasMenu.getItems().add(christmasItem);

            MenuItem animalsItem = new MenuItem("Animals " + themeNumber);
            animalsItem.setOnAction(e -> changeTheme("Animal" + themeNumber));
            animalsMenu.getItems().add(animalsItem);

            MenuItem foodItem = new MenuItem("Food " + themeNumber);
            foodItem.setOnAction(e -> changeTheme("Food" + themeNumber));
            foodMenu.getItems().add(foodItem);
        }

        changeThemeMenu.getItems().addAll(christmasMenu, animalsMenu, foodMenu);

        MenuItem restartItem = new MenuItem("Restart");
        restartItem.setOnAction(e -> restartGame());

        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> Platform.exit());

        optionsMenu.getItems().addAll(restartItem, changeThemeMenu, new SeparatorMenuItem(), quitItem);

        MenuItem aboutItem = new MenuItem("About PuzzleQuest");
        aboutItem.setOnAction(e -> showAboutInfo());
        aboutMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(optionsMenu, aboutMenu);

        VBox root = new VBox();
        root.getChildren().add(menuBar);
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
        Image previewImage = new Image(getClass().getResource("/Food1/original.jpg").toExternalForm());
        previewImageView = new ImageView(previewImage);
        previewImageView.setFitWidth(420);
        previewImageView.setFitHeight(420);
        previewImageView.setVisible(false);
        rootPane.getChildren().add(previewImageView);
        StackPane.setAlignment(previewImageView, Pos.CENTER);

        primaryStage.setTitle("PuzzleQuest");
        primaryStage.setScene(scene);
        primaryStage.show();

        feedbackLabel = new Label();
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setFont(new Font("Arial", 48));
        feedbackLabel.setAlignment(Pos.CENTER);
        feedbackLabel.setVisible(false);
        feedbackLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));

        feedbackLabel.setPadding(new Insets(10, 20, 10, 20));

        feedbackLabel.toFront();

        rootPane.getChildren().add(feedbackLabel);

        moveCounterLabel = new Label("Moves: 0");
        moveCounterLabel.setFont(new Font("Arial", 30));

        timerLabel = new Label("Time: 0");
        timerLabel.setFont(new Font("Arial", 30));

        HBox statusLayout = new HBox(100);
        statusLayout.setAlignment(Pos.CENTER);
        statusLayout.getChildren().addAll(timerLabel, moveCounterLabel);

        root.getChildren().add(1, statusLayout);

        startTimer();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                previewImageView.setVisible(true);
            } else {
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
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.P) {
                previewImageView.setVisible(false);
            }
        });

    }
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
            feedbackLabel.setVisible(false);
            if (isSolved()) {
                if (timeline != null) {
                    timeline.stop();
                }
                showSolvedFeedback();
            }
        } else {
            feedbackLabel.setText("Invalid move!");
            feedbackLabel.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(1)
            );
            visiblePause.setOnFinished(
                    event -> feedbackLabel.setVisible(false)
            );
            visiblePause.play();
        }

    }
    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

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
    private void updateGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != 0) {
                    String imagePath = getClass().getResource("/" + currentTheme + "/" + data[i][j] + ".jpg").toExternalForm();
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(PIECE_SIZE);
                    imageView.setFitHeight(PIECE_SIZE);
                    gridPane.add(imageView, j, i);
                }
            }
        }
    }
    private void restartGame() {
        initializeData();
        shuffleData();
        updateGridPane(gridPane);
        moveCount = 0;
        moveCounterLabel.setText("Moves: " + moveCount);
        if (timeline != null) {
            timeline.stop();
        }
        startTimer();
        setRandomBackground();
    }

    private void changeTheme(String theme) {
        currentTheme = theme;

        String previewImagePath = getClass().getResource("/" + currentTheme + "/original.jpg").toExternalForm();
        Image previewImage = new Image(previewImagePath);
        previewImageView.setImage(previewImage);

        gridPane.getChildren().clear();

        initializeData();
        shuffleData();
        updateGridPane(gridPane);
        setRandomBackground();
    }


    private void showAboutInfo() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About PuzzleQuest");
        aboutAlert.setHeaderText("PuzzleQuest v.1.0");
        aboutAlert.setContentText("This is a fun and engaging puzzle game.\nDeveloped by: Lulu Dong");

        aboutAlert.showAndWait();
    }

    private void startTimer() {
        timeSeconds = 0;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void updateTimer() {
        timeSeconds++;
        timerLabel.setText("Time: " + timeSeconds);
    }

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
    private void showSolvedFeedback() {
        Alert solvedAlert = new Alert(Alert.AlertType.INFORMATION);
        solvedAlert.setTitle("Congratulations! You won");
        solvedAlert.setHeaderText(null);
        solvedAlert.setContentText("You've solved the puzzle in " + moveCount + " moves and " + timeSeconds + " seconds.");

        solvedAlert.showAndWait();
    }

    private void setRandomBackground() {
        int numberOfBackgroundImages = 3;
        Random random = new Random();
        int bgImageNumber = random.nextInt(numberOfBackgroundImages) + 1;

        String bgImagePath = getClass().getResource("/Background/" + bgImageNumber + ".jpg").toExternalForm();
        Image bgImage = new Image(bgImagePath);
        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true));
        rootPane.setBackground(new Background(backgroundImage));
    }
    public static void main(String[] args) {
        launch(args);
    }
}
