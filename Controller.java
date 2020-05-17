import java.util.*;

import javafx.animation.*;
import javafx.event.*;
import javafx.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * A controller class of the game
 * 
 * @author tkong(20379081)
 */
public class Controller {
    private Stage stage;
    private GameRecord gameRecord;
    private Game game;
    Teletubbies teletubbiesThread;

    /**
     * The constructor of the class
     * 
     * @param stage
     */
    public Controller(Stage stage) {
        this.stage = stage;
        this.game = null;
        this.gameRecord = null;
        this.teletubbiesThread = new Teletubbies();
    }

    /**
     * Called when the player clicks the select difficulty button The players must
     * enter a name to play the game
     * 
     * @param e
     * @param nameField a textfiled where the player inputs his/her name
     * @param level     the difficulty level selected by the user
     * @param warning   the warning message, will show message when the player does
     *                  not input name
     */
    public void selectLevel(MouseEvent e, TextField nameField, String level, Text warning) {
        String username = nameField.getText();
        // The player needs to enter a name
        if (username.isEmpty()) {
            warning.setText("Please enter a name");
            return;
        }
        // initialize a game record with corresponding game level
        this.gameRecord = new GameRecord(level);
        // start a game will this player and difficulty level
        this.game = new Game(username, level);

        warning.setText("");
        playGame(level);
    }

    /**
     * Play the game at the difficulty level that the player select
     * 
     * @param level
     */
    public void playGame(String level) {
        stage.setScene(new View(stage).gameScene(this.game));
    }

    /**
     * To put the music note image randomly at every round of the game
     * 
     * @param game the game
     * @return the drawn game grid
     */
    public GridPane drawGameGrid(Game game) {
        // create a grid to place the note images
        GridPane gameGrid = new GridPane();
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        // load the images from the arraylist
        ArrayList<Pair<Integer, Image>> imageList = game.getImageList().getList();
        ImageView image0 = new ImageView(imageList.get(0).getValue());
        Rectangle rec0 = new Rectangle(0, 0, 70, 70);
        rec0.getStyleClass().add("rectangle");
        Group g0 = new Group();
        g0.getChildren().addAll(image0, rec0);
        ImageView image1 = new ImageView(imageList.get(1).getValue());
        Rectangle rec1 = new Rectangle(0, 0, 70, 70);
        rec1.getStyleClass().add("rectangle");
        Group g1 = new Group();
        g1.getChildren().addAll(image1, rec1);
        ImageView image2 = new ImageView(imageList.get(2).getValue());
        Rectangle rec2 = new Rectangle(0, 0, 70, 70);
        rec2.getStyleClass().add("rectangle");
        Group g2 = new Group();
        g2.getChildren().addAll(image2, rec2);
        ImageView image3 = new ImageView(imageList.get(3).getValue());
        Rectangle rec3 = new Rectangle(0, 0, 70, 70);
        rec3.getStyleClass().add("rectangle");
        Group g3 = new Group();
        g3.getChildren().addAll(image3, rec3);

        // add the imageview group into the grid
        gameGrid.add(g0, 1, 0);
        gameGrid.add(g1, 0, 1);
        gameGrid.add(g2, 1, 1);
        gameGrid.add(g3, 2, 1);
        return gameGrid;
    }

    /**
     * Update the game when the timeline reaches the end, it will update the
     * player's score, round number randomly place the music note in the game grid,
     * and update the timeline with new KeyFrame. It will go to the next round if
     * the game does not end and go to display the score if the game finishes 25
     * rounds
     * 
     * @param e        the event
     * @param score    the score of the player
     * @param round    the round number of the game
     * @param gameGrid the gameGrid
     * @param timeline the timeline
     */
    public void updateGame(ActionEvent e, Label score, Label round, GridPane gameGrid, Timeline timeline) {
        timeline.stop();
        // play this note if the player misses
        new Note("X").playNote();
        if (this.game.nextRound()) {
            KeyFrame keyFrame = new KeyFrame(this.game.generateTime(),
                    nextEvent -> updateGame(nextEvent, score, round, gameGrid, timeline));
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
            score.setText(String.valueOf(this.game.getPlayer().getScore()));
            round.setText(String.valueOf(1 + this.game.getCurrentRound()));
            gameGrid.getChildren().clear();
            gameGrid.getChildren().setAll(drawGameGrid(game));
        } else {
            displayScore();
        }
    }

    /**
     * Update the game when the player hits a key. It will first confirm whether
     * hit. If hit, then it will play the sound of the note It will update the
     * player's score, round number, randomly place the music note in the game grid,
     * and update the timeline with new KeyFrame. It will go to the next round if
     * the game does not end and go to display the score if the game finishes 25
     * rounds
     * 
     * @param e        the key press event
     * @param score    the score of the player to be updated
     * @param round    the round number of the game to be update
     * @param gameGrid the gameGrid of the game to be update
     * @param timeline the timeline of the game to be update
     */
    public void updateGame(KeyEvent e, Label score, Label round, GridPane gameGrid, Timeline timeline) {
        timeline.stop();
        if (this.game.isHit(e.getCode())) {
            this.game.playNote();
            this.game.getPlayer().incrementScore();
        } else {
            // if the player miss the hit or wrong, play this note
            new Note("X").playNote();
        }
        if (this.game.nextRound()) {
            KeyFrame keyFrame = new KeyFrame(this.game.generateTime(),
                    nextEvent -> updateGame(nextEvent, score, round, gameGrid, timeline));
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
            score.setText(String.valueOf(this.game.getPlayer().getScore()));
            round.setText(String.valueOf(1 + this.game.getCurrentRound()));
            gameGrid.getChildren().clear();
            gameGrid.getChildren().setAll(drawGameGrid(game));
        } else {
            displayScore();
        }
    }

    /**
     * When game is finished, go to scene 3 to display the score and the top 3
     * players
     */
    public void displayScore() {
        game.playMusic();
        gameRecord.addGame(this.game);
        gameRecord.writeTop3();
        stage.setScene(new View(stage).scoreScene(this.gameRecord, this.game));
    }

    /**
     * To try the game again, reset all the game and game record, but the player
     * will be the same
     * 
     * @param e
     */
    public void playAgain(MouseEvent e) {
        game.stopMusic();
        teletubbiesThread.stopMusic();
        stage.setScene(new View(stage).mainScene(false, game.getPlayer()));
        game = null;
        gameRecord = null;
    }

	public void playTeletubbies() {
        game.stopMusic();
        teletubbiesThread.playMusic();
	}
}