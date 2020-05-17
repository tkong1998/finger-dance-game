import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class of the game application
 * 
 * @author tkong(20379081)
 */
public class Main extends Application {
    private static Map<String, Scene> scenes = new HashMap<>(); // hashmap to stores all the scenes of the game

    private static Controller controller; // create the controller to control the game

    /**
     * Get the scenes of the application
     * 
     * @return the secnes map
     */
    public static Map<String, Scene> getScenes() {
        return Main.scenes;
    }

    /**
     * Get the controller of the game
     * 
     * @return the controller of the game
     */
    public static Controller getController() {
        return Main.controller;
    }

    /**
     * The start method of the application At the start of the game, it puts the
     * first scene in the hash map and loads it in the primary stage;
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.controller = new Controller(primaryStage);
        primaryStage.setScene(new View(primaryStage).mainScene(true, null));
        primaryStage.setTitle("ISOM3320 Take-home Quiz");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}