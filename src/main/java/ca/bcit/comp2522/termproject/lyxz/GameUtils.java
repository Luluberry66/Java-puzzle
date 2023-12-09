package ca.bcit.comp2522.termproject.lyxz;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.util.Random;

public class GameUtils {

    private static final Map<String, MediaPlayer> soundMap = new HashMap<>();
    private static MediaPlayer backgroundMediaPlayer;
    private static final int NUMBER_OF_BACKGROUND_IMAGES = 20;
    private static final int NUMBER_OF_MUSIC_TRACKS = 16;
    private static double volume = 0.5;
    public static Background getRandomBackground() {
        Random random = new Random();
        int imageIndex = random.nextInt(NUMBER_OF_BACKGROUND_IMAGES) + 1;
        String imagePath = "/Background/" + imageIndex + ".jpg";
        Image bgImage = new Image(GameUtils.class.getResourceAsStream(imagePath));

        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));


        return new Background(backgroundImage);
    }
    public static MediaPlayer getRandomMediaPlayer() {
        Random random = new Random();
        int musicIndex = random.nextInt(NUMBER_OF_MUSIC_TRACKS) + 1;

        URL musicResource = GameUtils.class.getResource("/Music/" + musicIndex + ".mp3");
        if (musicResource == null) {
            throw new RuntimeException("Resource not found: /Music/" + musicIndex + ".mp3");
        }

        Media media = new Media(musicResource.toString());
        return new MediaPlayer(media);
    }

    public static void preloadSound(String soundFileName) {
        URL soundResource = GameUtils.class.getResource("/SoundEffects/" + soundFileName);
        if (soundResource == null) {
            throw new RuntimeException("Resource not found: /SoundEffects/" + soundFileName);
        }
        Media sound = new Media(soundResource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        soundMap.put(soundFileName, mediaPlayer);
    }
    public static void playSound(String soundFileName) {
        MediaPlayer mediaPlayer = soundMap.get(soundFileName);
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
            }
            mediaPlayer.play();
        }
    }
    public static void setBackgroundMediaPlayer(MediaPlayer mediaPlayer) {
        backgroundMediaPlayer = mediaPlayer;
    }
    public static double getVolume() {
        return volume;
    }

    public static void setVolume(double value) {
        volume = value;
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setVolume(value);
        }
        soundMap.values().forEach(mediaPlayer -> mediaPlayer.setVolume(value));
    }

}
