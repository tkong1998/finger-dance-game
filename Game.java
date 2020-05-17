import javafx.util.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;

/**
 * The game class where stores all the info of each game and have all the
 * methods of the game
 * 
 * @author tkong(20379081)
 */
public class Game implements Comparable<Game> {

    public static final int TOTAL_ROUND = 25; // The maximal round of the game
    public static final String[] MUSIC_SHEET = { "C", "C", "D", "C", "F", "E", "C", "C", "D", "C", "G", "F", "C", "C",
            "C\'", "A", "F", "E", "D", "B", "B", "A", "F", "G", "F" }; // Happy Birthday Music Sheet
    public static final int[] NOTE_PLAY_TIME = { 400, 400, 400, 400, 450, 500, 400, 400, 400, 400, 450, 500, 400, 400,
            400, 400, 400, 400, 500, 400, 400, 400, 500, 600, 700 };
    public static final String[] MOVEMENT = { "UP", "LEFT", "DOWN", "RIGHT" }; // The movement of the game

    private Player player; // the player of the game
    private ImageList imageList; // an image list to store the 3 bg notes and music note
    private String level; // the difficulty level of the game
    private int currentRound; // the current round of the game
    Thread musicThread; // the thread that plays the song

    /**
     * Game constructor, called when the game begins
     * 
     * @param name  the name of the player
     * @param level the difficulty level selected by the player
     */
    public Game(String name, String level) {
        this.player = new Player(name);
        this.imageList = new ImageList();
        shuffleImage();
        this.level = level;
        this.currentRound = 0;
    }

    /**
     * Game constructor, this one is called when reading the game record file
     * 
     * @param name  the name of the player
     * @param score the score of the player
     * @param level the difficulty level of that game
     */
    public Game(String name, int score, String level) {
        this.player = new Player(name, score);
        this.level = level;
        this.currentRound = Game.TOTAL_ROUND;
    }

    /**
     * The function create a thread and play the song
     */
    public void MusicThread() {
        for (int i = 0; i < Game.TOTAL_ROUND; i++) {
            Note note = new Note(MUSIC_SHEET[i]);
            Platform.runLater(() -> {
                note.playNote();
            });
            try {
                Thread.sleep(NOTE_PLAY_TIME[i]);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    /**
     * Get the player of this game
     * 
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the image list (i.e. an array list to store 3 bg note and a music note)
     * 
     * @return the image list
     */
    public ImageList getImageList() {
        return this.imageList;
    }

    /**
     * Get game level
     * 
     * @return the game level (Easy, Medium, or Hard)
     */
    public String getLevel() {
        return this.level;
    }

    /**
     * Get the current round
     * 
     * @return the current round
     */
    public int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Increment the game round
     */
    public void incrementRound() {
        this.currentRound++;
    }

    /**
     * Shuffle the image list so that in each round of game, the music note image
     * will be randomly put into the grid
     */
    public void shuffleImage() {
        this.imageList = imageList.shuffle();
    }

    /**
     * Find out inside the randomly allocated image list, which image is the music
     * note image and its location
     * 
     * @return the location and the corresponding movement of the music note image
     */
    public String getTarget() {
        // if target == 0 -> UP; 1 -> LEFT; 2 -> DOWN; 3 -> RIGHT
        for (int i = 0; i < this.imageList.getList().size(); i++) {
            if (this.imageList.getList().get(i).getKey() == 0) {
                return Game.MOVEMENT[i];
            }
        }
        return "NOT FOUND";
    }

    /**
     * Generate a time interval to corresponding the difficulty level randomly Using
     * the Math.random()*((max-min)+1)+min to generate the random number within the
     * range
     * 
     * @return a Duration
     */
    public Duration generateTime() {
        Duration duration = Duration.ZERO;
        if (this.level.equals("Easy")) {
            duration = Duration.millis((Math.random() * (3 - 1 + 1) + 1) * 1000);
        } else if (this.level.equals("Medium")) {
            duration = Duration.millis((Math.random() * (2 - 0.5 + 1) + 0.5) * 1000);
        } else if (this.level.equals("Hard")) {
            duration = Duration.millis((Math.random() * (1 - 0.3 + 1) + 0.3) * 1000);
        }
        return duration;
    }

    /**
     * Check whether the key pressed the same as the target note
     * 
     * @param code the KeyCode of the key being pressed
     * @return whether is a hit (true) or a miss (false)
     */
    public boolean isHit(KeyCode code) {
        if (String.valueOf(code).equals(this.getTarget())) {
            return true;
        }
        return false;
    }

    /**
     * To check whether the game has next round (i.e. whether it reaches 25 rounds
     * or not) If the game has next round, then increment the round number and
     * randomly put the music note
     * 
     * @return true if has next round, false if it reaches 25 rounds
     */
    public boolean nextRound() {
        incrementRound();
        shuffleImage();
        if (getCurrentRound() >= 25) {
            return false;
        }
        return true;
    }

    /**
     * Play the music note of the corresponding location (follow the Happy Birthday
     * song)
     */
    public void playNote() {
        Note note = new Note(MUSIC_SHEET[currentRound]);
        note.playNote();
    }

    /**
     * Play the Happy Birthday Music when the player gets 25 hits
     */
    public void playMusic() {
        if (player.getScore() == 25) {
            musicThread = new Thread(this::MusicThread);
            musicThread.start();
        }
    }

    /**
     * Stop the music by stopping the musicThread when the player clicks "Play
     * Again"
     */
    public void stopMusic() {
        // if the music thread is null, then no need to stop the music
        if (musicThread == null){
            return;
        }
        if (musicThread.getState() == Thread.State.TIMED_WAITING) {
            musicThread.stop();
        }
    }

    /**
     * Compare this game to other game to find out which has higher score
     */
    @Override
    public int compareTo(Game otherGame) {
        return Double.compare(this.getPlayer().getScore(), otherGame.getPlayer().getScore());
    }
}