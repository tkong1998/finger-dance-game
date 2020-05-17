import javafx.application.Platform;

/**
 * This class is for playing the Teletubbies theme song
 */
public class Teletubbies {
    private final String[] TELETUBBIES_MUSIT_SHEET = {"G","G","E","G","A","F","G","E","D","G","G","E","C","A","A","F","A","G","D","C","G","B","C\'"};
    private final long[] BEAT = {374,126,250,1250,500,1500,500,1500,2000,374,126,250,1250,374,126,250,1250,1000,1000,2000,1000,1000,2000};
    Thread teletubbiesThread;
    
    public void TeletubbiesThread() {
        for (int i = 0; i < TELETUBBIES_MUSIT_SHEET.length; i++) {
            Note note = new Note(TELETUBBIES_MUSIT_SHEET[i]);
            Platform.runLater(() -> {
                note.playNote();
            });
            try {
                Thread.sleep( BEAT[i]);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
    public void playMusic(){
        teletubbiesThread = new Thread(this::TeletubbiesThread);
        teletubbiesThread.start();
    }

    public void stopMusic(){
        if (teletubbiesThread == null){
            return;
        }
        if (teletubbiesThread.getState() == Thread.State.TIMED_WAITING){
            teletubbiesThread.stop();
        }
    }
}