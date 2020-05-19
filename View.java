import java.util.*;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;

/**
 * The view class will generate the GUI components of different scenes
 * 
 * @author tkong(20379081)
 */
public class View {
    private Controller controller; // the controller of the game

    public View(Stage stage) {
        this.controller = Main.getController();
    }

    /**
     * Create the main scene of the game where the player can select difficulity
     * level
     * 
     * @param start  boolean, whether this is the player first try or try again
     * @param player the player
     * @return the scene
     */
    public Scene mainScene(boolean start, Player player) {
        Label title = new Label("ISOM3320 Take-home Quiz");
        title.setId("title-text");
        Label level = new Label("finger-dance");
        level.setId("finger_dance");
        TextField inputField = new TextField();
        // if start == true -> this requires the players to first input his/her name
        if (start) {
            inputField.setPromptText("Please enter your name");
            // if start == false -> the player chooses to retry the game, and hence no need
            // to re-enter the name
        } else {
            inputField.setText(player.getName());
            inputField.setEditable(false);
        }
        inputField.setFocusTraversable(false);
        // warning message
        Text warning = new Text();
        warning.setId("warning");

        Text option = new Text("Select difficultity:");

        // Buttons to select the difficulty
        Button easyBtn = new Button("Easy");
        easyBtn.setOnMouseClicked(e -> controller.selectLevel(e, inputField, "Easy", warning));
        Button medBtn = new Button("Medium");
        medBtn.setOnMouseClicked(e -> controller.selectLevel(e, inputField, "Medium", warning));
        Button hardBtn = new Button("Hard");
        hardBtn.setOnMouseClicked(e -> controller.selectLevel(e, inputField, "Hard", warning));
        HBox btnContainer = new HBox(20);
        btnContainer.getChildren().addAll(easyBtn, medBtn, hardBtn);

        Text disclaimer = new Text("Created by Tung KONG (20379081)");

        // Usa a vertical box to store all the GUI components
        VBox root = new VBox(10);
        root.getChildren().addAll(title, level, inputField, warning, option, btnContainer, disclaimer);

        Scene mainScene = new Scene(root, 320, 300);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        return mainScene;
    }

    /**
     * Crete the game scene where the player plays the game
     * 
     * @param game the game
     * @return the scene
     */
    public Scene gameScene(Game game) {
        // create a 2*2 grid to store the game info for each round
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(10, 10, 10, 10));
        infoGrid.setAlignment(Pos.CENTER);

        // create the labels to show difficulty level
        Label levelLabel = new Label("Level:");
        Label level = new Label(game.getLevel());
        HBox levelBox = new HBox(5);
        levelBox.getChildren().addAll(levelLabel, level);

        // create the labels for player
        Label playerLabel = new Label("Player:");
        Label playerName = new Label(game.getPlayer().getName());
        HBox playerBox = new HBox(5);
        playerBox.getChildren().addAll(playerLabel, playerName);

        // create the labels for scores
        Label scoreLabel = new Label("Score:");
        Label score = new Label(String.valueOf(game.getPlayer().getScore()));
        HBox scoreBox = new HBox(5);
        scoreBox.getChildren().addAll(scoreLabel, score);

        // create the labels for round
        Label roundLabel = new Label("Round ");
        Label round = new Label(String.valueOf(1 + game.getCurrentRound()));
        HBox roundBox = new HBox(5);
        roundBox.getChildren().addAll(roundLabel, round);

        // add all the info HBox into the info grid
        infoGrid.add(levelBox, 0, 0);
        infoGrid.add(playerBox, 1, 0);
        infoGrid.add(scoreBox, 0, 1);
        infoGrid.add(roundBox, 1, 1);

        // create a grid to place the note images
        GridPane gameGrid = new GridPane();
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);
        gameGrid.setPadding(new Insets(10, 10, 10, 10));
        gameGrid.setAlignment(Pos.CENTER);
        // use the helper function to draw the image
        gameGrid.getChildren().setAll(controller.drawGameGrid(game));

        // create the timeline at whose end the game will go to the next round if the
        // player misses
        Timeline timeline = new Timeline(); 
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(1000), nextEvent -> controller.eraseMusicNote(nextEvent,gameGrid));
        KeyFrame keyFrame2 = new KeyFrame(game.generateTime(),
                nextEvent -> controller.updateGame(nextEvent, score, round, gameGrid, timeline));
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.play();

        // create a vertical box to store all the components groups
        VBox root = new VBox(10);
        root.getChildren().addAll(infoGrid, gameGrid);
        Scene gameScene = new Scene(root, 320, 300);

        // if the player press a key, when the key is released, it will triggers the
        // update game function
        // which will check whether the player hits or not and go to the next round no
        // matter the result
        gameScene.setOnKeyReleased(e -> controller.updateGame(e, score, round, gameGrid, timeline));
        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        return gameScene;
    }

    /**
     * The last scene where displays the player's score, the top 3 players' scores
     * and allows the player to try again
     * 
     * @param records the game records which store the top 3 players' scores
     * @param game    the game that the player just finished
     * @return the scene
     */
    public Scene scoreScene(GameRecord records, Game game) {
        Label titleMessage = new Label("Game Over!");
        Label score = new Label("Your Score is " + String.valueOf(game.getPlayer().getScore()) + "!");
        Label top3 = new Label("Top 3 players with the higest scores:");
        top3.setId("top3");
        // create a grid to display the top 3 scores
        GridPane scoreGrid = new GridPane();
        scoreGrid.setHgap(20);
        scoreGrid.setVgap(5);

        // add the top3 score records into the grid dynamically
        ArrayList<Game> top3Record = records.getTop3();
        int i = 0;
        for (Game record : top3Record) {
            scoreGrid.add(new Text(record.getPlayer().getName()), 0, i);
            scoreGrid.add(new Text(String.valueOf(record.getPlayer().getScore())), 1, i);
            i++;
        }

        // the try again button to allow player to try again
        Button tryBtn = new Button("Try Again!");
        tryBtn.setOnMouseClicked(e -> controller.playAgain(e));

        Button playBtn = new Button("Play Teletubbies Theme Song");
        playBtn.setOnMouseClicked(e -> controller.playTeletubbies());

        // create a horizontal box to store all buttons
        HBox btnBox = new HBox(20);
        btnBox.getChildren().addAll(tryBtn,playBtn);

        VBox root = new VBox(10);
        root.getChildren().addAll(titleMessage, score, top3, scoreGrid, btnBox);
        Scene scoreScene = new Scene(root, 320, 300);
        scoreScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        return scoreScene;
    }
}