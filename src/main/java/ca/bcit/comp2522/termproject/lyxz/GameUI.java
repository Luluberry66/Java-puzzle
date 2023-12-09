package ca.bcit.comp2522.termproject.lyxz;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.util.Objects;

public class GameUI implements IGameUI{
    private IGameLogic gameLogic;
    private StackPane rootPane;
    private ImageView previewImageView;
    private String currentTheme;
    private static Label moveCounterLabel;
    private Label timerLabel;
    private static Label feedbackLabel;
    private Timeline timeline;
    private static int moveCount;
    private Integer timeSeconds;
    private GridPane gridPane;
    private VBox root;
    private MediaPlayer mediaPlayer;

    public GameUI(IGameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.root = new VBox();
        this.rootPane = new StackPane();
        this.timerLabel = new Label("Time: 0");
        this.moveCounterLabel = new Label("Moves: 0");
        feedbackLabel = new Label();
        feedbackLabel.setFont(new Font("Arial", 48));
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setAlignment(Pos.CENTER);
        feedbackLabel.setVisible(false);
        feedbackLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
        feedbackLabel.setPadding(new Insets(10, 20, 10, 20));
        feedbackLabel.toFront();
        this.currentTheme = "Food1";
        this.gridPane = new GridPane();
        rootPane.getChildren().add(gridPane);
        VBox.setVgrow(rootPane, Priority.ALWAYS);

        root.getChildren().addAll(createMenuBar(),setupStatusLayout(),rootPane);

        root.setBackground(GameUtils.getRandomBackground());

        Image previewImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Food1/original.jpg")));
        previewImageView = new ImageView(previewImage);
        previewImageView.setFitWidth(420);
        previewImageView.setFitHeight(420);
        previewImageView.setVisible(false);

        rootPane.getChildren().add(previewImageView);
        rootPane.getChildren().add(feedbackLabel);
        startTimer();
    }

    public VBox getRoot() {
        return root;
    }

    @Override
    public void setupUI(Scene scene, Stage stage) {
        setupGridPane();
        setupSceneEventHandlers(scene);
    }

    @Override
    public void updateGridPane() {
        gridPane.getChildren().clear();
        for (int i = 0; i < GameLogic.SIZE; i++) {
            for (int j = 0; j < GameLogic.SIZE; j++) {
                int tileValue = gameLogic.getTileValue(i, j);
                if(tileValue == 0) continue;
                ImageView tileImageView = createTileImageView(tileValue);
                gridPane.add(tileImageView, j, i);
            }
        }
    }

    @Override
    public void changeTheme(String theme) {
        this.currentTheme = theme;

        root.setBackground(GameUtils.getRandomBackground());
        updateGridPane();
        resetTimer();
        resetMoveCount();
        playRandomMusic();
        if(previewImageView.isVisible()) {
            PreviewImage();
        }
    }

    @Override
    public void restartGame() {
        gameLogic.initializeData();
        gameLogic.shuffleData();
        root.setBackground(GameUtils.getRandomBackground());
        updateGridPane();
        resetMoveCount();
        resetTimer();
        playRandomMusic();
    }

    @Override
    public void showAboutInfo() {
        Alert aboutAlert = new Alert(AlertType.INFORMATION);
        aboutAlert.setTitle("About PuzzleQuest");
        aboutAlert.setHeaderText("PuzzleQuest v.1.0");
        aboutAlert.setContentText("This is a fun and engaging puzzle game.\nDeveloped by: Lulu Dong");
        aboutAlert.showAndWait();
    }

    @Override
    public void startTimer() {
        timeSeconds = 0;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void updateTimer() {
        timeSeconds++;
        timerLabel.setText("Time: " + timeSeconds);
    }

    private void resetTimer() {
        stopTimer();
        startTimer();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    private ImageView createTileImageView(int tileValue) {
        String imagePath = "/" + currentTheme + "/" + tileValue + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameLogic.PIECE_SIZE);
        imageView.setFitHeight(GameLogic.PIECE_SIZE);
        return imageView;
    }

    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();

        Menu optionsMenu = createOptionsMenu();
        Menu aboutMenu = createAboutMenu();

        menuBar.getMenus().addAll(optionsMenu, aboutMenu);

        return menuBar;
    }

    private Menu createOptionsMenu() {
        Menu optionsMenu = new Menu("Options");

        MenuItem restartItem = new MenuItem("Restart");
        restartItem.setOnAction(e -> restartGame());

        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(e -> Platform.exit());

        Menu changeThemeMenu = new Menu("Change Theme");

        Menu volumeMenu = new Menu("Volume");

        CustomMenuItem volumeMenuItem = new CustomMenuItem(createVolumeSlider());
        volumeMenuItem.setHideOnClick(false);
        volumeMenu.getItems().add(volumeMenuItem);

        populateThemeMenu(changeThemeMenu);

        optionsMenu.getItems().addAll(restartItem, changeThemeMenu,volumeMenu, new SeparatorMenuItem(), quitItem);

        return optionsMenu;
    }

    private void populateThemeMenu(Menu changeThemeMenu) {
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

    }


    private Menu createAboutMenu() {
        Menu aboutMenu = new Menu("About");

        MenuItem aboutItem = new MenuItem("About PuzzleQuest");
        aboutItem.setOnAction(e -> showAboutInfo());
        aboutMenu.getItems().add(aboutItem);

        return aboutMenu;
    }

    private void setupGridPane() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(2);
        gridPane.setHgap(2);

        gridPane.setPrefSize(GameLogic.SIZE * GameLogic.PIECE_SIZE, GameLogic.SIZE * GameLogic.PIECE_SIZE);

        for (int i = 0; i < GameLogic.SIZE; i++) {
            for (int j = 0; j < GameLogic.SIZE; j++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(GameLogic.PIECE_SIZE);
                imageView.setFitHeight(GameLogic.PIECE_SIZE);
                gridPane.add(imageView, j, i);
            }
        }
    }
    private Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.1);
        volumeSlider.setBlockIncrement(0.1);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            GameUtils.setVolume(newVal.doubleValue());
        });

        return volumeSlider;
    }

    private HBox setupStatusLayout() {
        moveCounterLabel.setFont(new Font("Arial", 24));
        timerLabel.setFont(new Font("Arial", 24));

        HBox statusLayout = new HBox(120);
        statusLayout.setAlignment(Pos.CENTER);

        statusLayout.getChildren().addAll(moveCounterLabel, timerLabel);

        return statusLayout;
    }

    private void setupSceneEventHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                PreviewImage();
            } else {
                switch (event.getCode()) {
                    case UP:
                        gameLogic.moveTiles(Direction.UP);
                        break;
                    case DOWN:
                        gameLogic.moveTiles(Direction.DOWN);
                        break;
                    case LEFT:
                        gameLogic.moveTiles(Direction.LEFT);
                        break;
                    case RIGHT:
                        gameLogic.moveTiles(Direction.RIGHT);
                        break;
                    default:
                        break;
                }
                updateGridPane();
            }
            checkGameState();
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.P) {
                previewImageView.setVisible(false);
            }
        });
    }

    private void PreviewImage() {
        String imagePath = "/" + currentTheme + "/original.jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        previewImageView.setImage(image);

        previewImageView.setVisible(!previewImageView.isVisible());
        previewImageView.toFront();
    }


    public void checkGameState() {
        if (gameLogic.isSolved()) {
            stopTimer();
            GameUtils.playSound("win.mp3");
            showWinMessage();
        }
    }
    private void resetMoveCount() {
        moveCount = 0;
        moveCounterLabel.setText("Moves: " + moveCount);
    }


    private void showWinMessage() {
        Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
        winAlert.setTitle("Congratulations! You won!");
        winAlert.setHeaderText(null);
        winAlert.setContentText("You've solved the puzzle in " + moveCount + " moves and " + timeSeconds + " seconds!");
        winAlert.showAndWait();
    }
    public static void incrementMoveCount() {
        moveCount++;
        moveCounterLabel.setText("Moves: " + moveCount);
    }
    public static void feedbackInvalidMove() {
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
    public void playRandomMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        mediaPlayer = GameUtils.getRandomMediaPlayer();
        GameUtils.setBackgroundMediaPlayer(mediaPlayer);
        mediaPlayer.setVolume(GameUtils.getVolume());
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
}
