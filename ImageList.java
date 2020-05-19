import java.util.*;
import java.io.FileInputStream;

import javafx.scene.image.*;
import javafx.util.*;

/**
 * The ImageList class which stores the 4 images
 * 
 * @author tkong(20379081)
 */
public class ImageList {

    private final String MUSIC_NOTE_PATH = "resource_pack/music_note.png"; // path to the music note
    private final String BG_NOTE_PATH = "resource_pack/bg_note.png"; // path to the bg note
    private ArrayList<Pair<Integer, Image>> list; // the list which stores the images
    private ArrayList<Pair<Integer, Image>> listAfterErased; // the list which stores 4 bg notes

    /**
     * The constructor of the class which loads the images from paths and store the
     * images into a list when the key is 0 -> indicating that it is a music image,
     * other means that they are others
     */
    public ImageList() {
        try {
            this.list = new ArrayList<Pair<Integer, Image>>();
            this.listAfterErased = new ArrayList<Pair<Integer, Image>>();
            Image musicImage = new Image(new FileInputStream(MUSIC_NOTE_PATH), 70, 70, true, true);
            Image bgNoteImage = new Image(new FileInputStream(BG_NOTE_PATH), 70, 70, true, true);

            this.list.add(new Pair<Integer, Image>(0, musicImage));

            // use a loop to add 3 bg note into the list
            for (int i = 0; i < 3; i++) {
                this.list.add(new Pair<Integer, Image>(i + 1, bgNoteImage));
            }

            for (int i = 0; i < 4; i++) {
                this.listAfterErased.add(new Pair<Integer, Image>(i, bgNoteImage));
            }
            
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * Randomly shuffle the list
     * 
     * @return
     */
    public ImageList shuffle() {
        Collections.shuffle(this.list);
        return this;
    }

    /**
     * Get the image list
     * 
     * @return the image list
     */
    public ArrayList<Pair<Integer, Image>> getList() {
        return this.list;
    }

    /**
     * Get the list with four bg notes
     * @return the image list
     */
    public ArrayList<Pair<Integer, Image>> getErasedList() {
        return this.listAfterErased;
    }
}