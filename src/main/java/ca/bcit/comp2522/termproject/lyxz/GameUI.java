package ca.bcit.comp2522.termproject.lyxz;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * This class contains methods that are used in multiple classes.
 * @version 2023
 * @author Lulu Dong
 */
public class GameUI implements IGameUI {
    /**
     * The size of the image.
     */
    public static final int IMAGE_SIZE = 420;
    /**
     * The unit tick.
     */
    public static final double UNIT_TICK = 0.1;
    /**
     * The initial volume.
     */
    public static final double INITIAL_VOLUME = 0.7;
    /**
     * The move count font size.
     */
    public static final int MOVE_COUNT_FONT_SIZE = 24;
    /**
     * The timer font size.
     */
    public static final int TIMER_FONT_SIZE = 24;
    /**
     * The status layout spacing.
     */
    public static final int STATUS_LAYOUT_SPACING = 120;
    /**
     * The theme number.
     */
    public static final int THEME_NUMBER = 8;
    /**
     * The feedback font size.
     */
    public static final int FEEDBACK_FONT_SIZE = 48;
    /**
     * The feedback label padding top.
     */
    public static final int FEEDBACK_LABEL_PADDING_TOP = 10;
    /**
     * The feedback label padding right.
     */
    public static final int FEEDBACK_LABEL_PADDING_RIGHT = 20;
    /**
     * The feedback label padding bottom.
     */
    public static final int FEEDBACK_LABEL_PADDING_BOTTOM = 10;
    /**
     * The feedback label padding left.
     */
    public static final int FEEDBACK_LABEL_PADDING_LEFT = 20;
    /**
     * The feedback label corner radius.
     */
    public static final int FEED_BACK_LABEL_CORNER_RADIUS = 5;
    private static Label moveCounterLabel;
    private static Label feedbackLabel;
    private static int moveCount;
    private final IGameLogic gameLogic;
    private final ImageView previewImageView;
    private String currentTheme;
    private final Label timerLabel;
    private Timeline timeline;
    private Integer timeSeconds;
    private final GridPane gridPane;
    private final VBox root;
    private MediaPlayer mediaPlayer;
    /**
     * This method is the constructor of the GameUI class.
     * @param gameLogic the game logic
     */
    public GameUI(final IGameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.root = new VBox();
        StackPane rootPane = new StackPane();
        this.timerLabel = new Label("Time: 0");
        moveCounterLabel = new Label("Moves: 0");
        feedbackLabel = new Label();
        feedbackLabel.setFont(new Font("Arial", FEEDBACK_FONT_SIZE));
        feedbackLabel.setTextFill(Color.RED);
        feedbackLabel.setAlignment(Pos.CENTER);
        feedbackLabel.setVisible(false);
        feedbackLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,
                new CornerRadii(FEED_BACK_LABEL_CORNER_RADIUS), Insets.EMPTY)));
        feedbackLabel.setPadding(new Insets(FEEDBACK_LABEL_PADDING_TOP, FEEDBACK_LABEL_PADDING_LEFT,
                    FEEDBACK_LABEL_PADDING_BOTTOM, FEEDBACK_LABEL_PADDING_RIGHT));
        feedbackLabel.toFront();
        this.currentTheme = "Food1";
        this.gridPane = new GridPane();
        rootPane.getChildren().add(gridPane);
        VBox.setVgrow(rootPane, Priority.ALWAYS);

        root.getChildren().addAll(createMenuBar(), setupStatusLayout(), rootPane);

        root.setBackground(GameUtils.getRandomBackground());

        Image previewImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Food1/original.jpg")));
        previewImageView = new ImageView(previewImage);
        previewImageView.setFitWidth(IMAGE_SIZE);
        previewImageView.setFitHeight(IMAGE_SIZE);
        previewImageView.setVisible(false);

        rootPane.getChildren().add(previewImageView);
        rootPane.getChildren().add(feedbackLabel);
        startTimer();
    }
    /**
     * This method returns the root.
     * @return the root
     */
    public VBox getRoot() {
        return root;
    }
    /**
     * This method sets up the UI.
     * @param scene the scene
     * @param stage the stage
     */
    @Override
    public void setupUI(final Scene scene, final Stage stage) {
        setupGridPane();
        setupSceneEventHandlers(scene);
    }
    /**
     * This method updates the grid pane.
     */
    @Override
    public void updateGridPane() {
        gridPane.getChildren().clear();
        for (int i = 0; i < GameLogic.SIZE; i++) {
            for (int j = 0; j < GameLogic.SIZE; j++) {
                int tileValue = gameLogic.getTileValue(i, j);
                if (tileValue == 0) {
                    continue;
                }
                ImageView tileImageView = createTileImageView(tileValue);
                gridPane.add(tileImageView, j, i);
            }
        }
    }
    /**
     * This method changes the theme.
     * @param theme the theme
     */
    @Override
    public void changeTheme(final String theme) {
        this.currentTheme = theme;

        root.setBackground(GameUtils.getRandomBackground());
        updateGridPane();
        resetTimer();
        resetMoveCount();
        playRandomMusic();
        if (previewImageView.isVisible()) {
            previewImage();
        }
    }
    /**
     * This method restarts the game.
     */
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
    /**
     * This method shows the about info.
     */
    @Override
    public void showAboutInfo() {
        Alert aboutAlert = new Alert(AlertType.INFORMATION);
        aboutAlert.setTitle("About PuzzleQuest");
        aboutAlert.setHeaderText("PuzzleQuest v.1.0");
        aboutAlert.setContentText("This is a fun and engaging puzzle game.\nDeveloped by: Lulu Dong");
        aboutAlert.showAndWait();
    }
    /**
     * This method starts the timer.
     */
    @Override
    public void startTimer() {
        timeSeconds = 0;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    /**
     * This method updates the timer.
     */
    @Override
    public void updateTimer() {
        timeSeconds++;
        timerLabel.setText("Time: " + timeSeconds);
    }
    /**
     * This method resets the timer.
     */
    private void resetTimer() {
        stopTimer();
        startTimer();
    }
    /**
     * This method stops the timer.
     */
    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
    /**
     * This method creates a tile image view.
     * @param tileValue the tile value
     * @return the image view
     */
    private ImageView createTileImageView(final int tileValue) {
        String imagePath = "/" + currentTheme + "/" + tileValue + ".jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameLogic.PIECE_SIZE);
        imageView.setFitHeight(GameLogic.PIECE_SIZE);
        return imageView;
    }
    /**
     * This method creates a menu bar.
     * @return the menu bar
     */
    private MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();

        Menu optionsMenu = createOptionsMenu();
        Menu aboutMenu = createAboutMenu();

        menuBar.getMenus().addAll(optionsMenu, aboutMenu);

        return menuBar;
    }
    /**
     * This method creates an options menu.
     * @return the options menu
     */
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

        optionsMenu.getItems().addAll(restartItem, changeThemeMenu, volumeMenu, new SeparatorMenuItem(), quitItem);

        return optionsMenu;
    }
    /**
     * This method populates the theme menu.
     * @param changeThemeMenu the change theme menu
     */
    private void populateThemeMenu(final Menu changeThemeMenu) {
        Menu christmasMenu = new Menu("Christmas");
        Menu animalsMenu = new Menu("Animals");
        Menu foodMenu = new Menu("Food");
        for (int i = 1; i <= THEME_NUMBER; i++) {
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
    /**
     * This method creates an about menu.
     * @return the about menu
     */
    private Menu createAboutMenu() {
        Menu aboutMenu = new Menu("About");

        MenuItem aboutItem = new MenuItem("About PuzzleQuest");
        aboutItem.setOnAction(e -> showAboutInfo());
        aboutMenu.getItems().add(aboutItem);

        return aboutMenu;
    }
    /**
     * This method sets up the grid pane.
     */
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
    /**
     * This method creates a volume slider.
     * @return the volume slider
     */
    private Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, INITIAL_VOLUME);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(UNIT_TICK);
        volumeSlider.setBlockIncrement(UNIT_TICK);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> GameUtils.setVolume(newVal.doubleValue()));

        return volumeSlider;
    }
    /**
     * This method sets up the status layout.
     * @return the status layout
     */
    private HBox setupStatusLayout() {
        moveCounterLabel.setFont(new Font("Arial", MOVE_COUNT_FONT_SIZE));
        timerLabel.setFont(new Font("Arial", TIMER_FONT_SIZE));

        HBox statusLayout = new HBox(STATUS_LAYOUT_SPACING);
        statusLayout.setAlignment(Pos.CENTER);

        statusLayout.getChildren().addAll(moveCounterLabel, timerLabel);

        return statusLayout;
    }
    /**
     * This method sets up the scene event handlers.
     * @param scene the scene
     */
    private void setupSceneEventHandlers(final Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                previewImage();
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
                } updateGridPane();
            }
            checkGameState();
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.P) {
                previewImageView.setVisible(false);
            }
        });
    }
    /**
     * This method previews the image.
     */
    private void previewImage() {
        String imagePath = "/" + currentTheme + "/original.jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        previewImageView.setImage(image);

        previewImageView.setVisible(!previewImageView.isVisible());
        previewImageView.toFront();
    }
    /**
     * This method checks the game state.
     */
    public void checkGameState() {
        if (gameLogic.isSolved()) {
            stopTimer();
            GameUtils.playSound("win.mp3");
            showWinMessage();
        }
    }
    /**
     * This method resets the move count.
     */
    private void resetMoveCount() {
        moveCount = 0;
        moveCounterLabel.setText("Moves: " + moveCount);
    }
    /**
     * This method shows the win message.
     */
    private void showWinMessage() {
        Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
        winAlert.setTitle("Congratulations! You won!");
        winAlert.setHeaderText(null);
        winAlert.setContentText("You've solved the puzzle in " + moveCount + " moves and " + timeSeconds + " seconds!");
        winAlert.showAndWait();
    }
    /**
     * This method increments the move count.
     */
    public static void incrementMoveCount() {
        moveCount++;
        moveCounterLabel.setText("Moves: " + moveCount);
    }
    /**
     * This method shows the feedback for invalid move.
     */
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
    /**
     * This method plays random music.
     */
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
