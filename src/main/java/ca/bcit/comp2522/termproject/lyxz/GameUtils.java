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

/**
 * This class contains methods that are used in multiple classes.
 * @version 2023
 * @author Lulu Dong
 */
public class GameUtils {

    private static final Map<String, MediaPlayer> SOUNDMAP = new HashMap<>();
    private static final int NUMBER_OF_BACKGROUND_IMAGES = 20;
    private static final int NUMBER_OF_MUSIC_TRACKS = 16;
    private static double volume = 0.5;
    private static MediaPlayer backgroundMediaPlayer;

    /**
     * This method returns a random background image.
     * @return a random background image.
     */
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

    /**
     * This method returns a random media player.
     * @return a random media player.
     * @throws RuntimeException if the resource is not found.
     */
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

    /**
     * This method preloads a sound file.
     * @param soundFileName the name of the sound file.
     * @throws RuntimeException if the resource is not found
     */
    public static void preloadSound(final String soundFileName) {
        URL soundResource = GameUtils.class.getResource("/SoundEffects/" + soundFileName);
        if (soundResource == null) {
            throw new RuntimeException("Resource not found: /SoundEffects/" + soundFileName);
        }
        Media sound = new Media(soundResource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        SOUNDMAP.put(soundFileName, mediaPlayer);
    }

    /**
     * This method plays a sound file.
     * @param soundFileName the name of the sound file.
     */
    public static void playSound(final String soundFileName) {
        MediaPlayer mediaPlayer = SOUNDMAP.get(soundFileName);
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
            }
            mediaPlayer.play();
        }
    }

    /**
     * This method stops a sound file.
     * @param mediaPlayer the media player.
     */
    public static void setBackgroundMediaPlayer(final MediaPlayer mediaPlayer) {
        backgroundMediaPlayer = mediaPlayer;
    }

    /**
     * This method returns the volume.
     * @return the volume.
     */
    public static double getVolume() {
        return volume;
    }

    /**
     * This method sets the volume.
     * @param value the volume.
     */
    public static void setVolume(final double value) {
        volume = value;
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setVolume(value);
        }
        SOUNDMAP.values().forEach(mediaPlayer -> mediaPlayer.setVolume(value));
    }

}
