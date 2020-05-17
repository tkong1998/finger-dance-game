import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * The class is a class which loads the music note .wav file
 * 
 * @author tkong(20379081)
 */
public class Note {
    private File file;
    private Clip clip;

    /**
     * Constructor of the Note
     * 
     * @param c the file name string of the note
     */
    public Note(String c) {
        super();
        String path = "resource_pack/note_" + c.toUpperCase() + ".wav";
        this.file = new File(path);
        try {
            this.clip = AudioSystem.getClip();
            this.clip.open(AudioSystem.getAudioInputStream(this.file));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * called to play a note
     */
    public void playNote() {
        clip.start();
    }
}