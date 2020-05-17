/**
 * A player class which stores the information of the player for every game
 * 
 * @author tkong(20379081)
 */
public class Player {
    private final String name;
    private int score;

    /**
     * The constructor of the class. When constructed, the score of that particular
     * play will be initialized as 0
     * 
     * @param name the name of the player, it will be inputed when the player types
     *             the name in the TextField
     */
    public Player(final String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * The constructor of the class. Called when reading from the csv file
     * 
     * @param name  the name of the player, it will be inputed when the player types
     *              the name in the TextField
     * @param score the score of that player
     */
    public Player(final String name, final int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * The the name of the particular player
     * 
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * The the score of the particular player
     * 
     * @return the score of the player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * When the player hit the note, the score will increment by one.
     */
    public void incrementScore() {
        this.score++;
    }
}