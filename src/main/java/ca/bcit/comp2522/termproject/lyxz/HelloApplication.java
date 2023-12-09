//package ca.bcit.comp2522.termproject.lyxz;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
///**
// * The main application class.
// * @version 2023
// * @author Lulu Dong
// */
//public class HelloApplication extends Application {
//    /**
//     * The width of the window.
//     */
//    public static final int WIDTH_1 = 320;
//    /**
//     * The height of the window.
//     */
//    public static final int HEIGHT_1 = 240;
//    /**
//     * Starts the application.
//     * @param stage the stage
//     * @throws IOException if the FXML file cannot be loaded
//     */
//    @Override
//    public void start(final Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), WIDTH_1, HEIGHT_1);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    /**
//     * Launches the application.
//     * @param args the command line arguments
//     */
//    public static void main(final String[] args) {
//        launch();
//    }
//}
