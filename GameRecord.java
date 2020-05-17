import java.io.*;
import java.util.*;

/**
 * A game record class which stores the record information of the game
 * 
 * @author tkong(20379081)
 */
public class GameRecord {
    private ArrayList<Game> gameRecord;
    private final String PATH;

    /**
     * load the game record data from the csv file, if file not exists, then create
     * the file
     * 
     * @param level the difficulty level that the player selects
     */
    public GameRecord(String level) {
        gameRecord = new ArrayList<Game>();
        PATH = "data/" + level + ".csv";
        File file = new File(PATH);

        // if file don't exist, then create the file and return
        if (!file.exists()) {
            String firstLine = "name, score";
            try (FileOutputStream outputFile = new FileOutputStream(PATH)) {
                outputFile.write(firstLine.getBytes());
                outputFile.flush();
                outputFile.close();
            } catch (Exception exception) {
                exception.getStackTrace();
            }
            return;
        }

        // load the file into record
        try (Scanner input = new Scanner(file)) {
            String line, lineData[];
            for (int i = 0; input.hasNextLine(); i++) {
                line = input.nextLine();
                // skip the first line (header)
                if (i == 0) {
                    continue;
                }
                // skip the empty line
                if (line.trim().isEmpty()) {
                    continue;
                }
                lineData = line.split(",");
                String name = lineData[0].trim();
                int score = Integer.parseInt(lineData[1].trim());
                Game game = new Game(name, score, level);
                this.addGame(game);
            }
        } catch (Exception exception) {
            exception.getStackTrace();
        }
    }

    /**
     * add the game to the record
     * 
     * @param player the player to be added
     */
    public void addGame(Game game) {
        this.gameRecord.add(game);
    }

    /**
     * get top 3 players with the highest score
     * 
     * @return the player list with top 3 players with the highest score
     */
    public ArrayList<Game> getTop3() {
        Collections.sort(gameRecord, Collections.reverseOrder());
        if (gameRecord.size() >= 3) {
            return (new ArrayList<Game>(gameRecord.subList(0, 3)));
        }
        return gameRecord;
    }

    public void writeTop3() {
        ArrayList<Game> top = this.getTop3();
        try {
            FileWriter file = new FileWriter(PATH);
            file.write("name, score\n");
            for (Game game : top) {
                file.write(game.getPlayer().getName() + ", " + String.valueOf(game.getPlayer().getScore()));
                file.write("\n");
            }
            file.close();
            System.out.println("Write success");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}