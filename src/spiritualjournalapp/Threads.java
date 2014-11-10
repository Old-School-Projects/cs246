/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;


import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Text;

/*****************************************
 * THREADS CLASS
 * @author paul
 ****************************************/
public class Threads implements Runnable{
    private Text entriesLabel;
    private Text scripturesLabel;
    private Text topicsLabel;
    
    private int countEntries;
    private int countScrips;
    private int countTopics;
    
    private final Journal journal;
    
    public Threads(Journal j) {    
    entriesLabel = new Text();
    scripturesLabel = new Text();
    topicsLabel = new Text();
    journal = j;
    
    entriesLabel.setId("displayText");
    scripturesLabel.setId("displayText");
    topicsLabel.setId("displayText");
    
    }
    
    /****************************************
    * GET ENTRY LABEL
     * @return 
    ****************************************/
    public Text getEntriesLabel() {
        return entriesLabel;
    }

    /****************************************
    *
    * @param entriesLabel
    ****************************************/
    public void setEntriesLabel(Text entriesLabel) {
        this.entriesLabel = entriesLabel;
    }

    /****************************************
    *
     * @return 
    ****************************************/
    public Text getScripturesLabel() {
        return scripturesLabel;
    }

    /****************************************
    *
     * @param scripturesLabel
    ****************************************/
    public void setScripturesLabel(Text scripturesLabel) {
        this.scripturesLabel = scripturesLabel;
    }

    /****************************************
    *
     * @return 
    ****************************************/
    public Text getTopicsLabel() {
        return topicsLabel;
    }

    /****************************************
    *
     * @param topicsLabel
    ****************************************/
    public void setTopicsLabel(Text topicsLabel) {
        this.topicsLabel = topicsLabel;
    }

    /****************************************
    *
     * @return 
    ****************************************/
    public int getCountEntries() {
        return countEntries;
    }

    /****************************************
    *
     * @param countEntries
    ****************************************/
    public void setCountEntries(int countEntries) {
        this.countEntries = countEntries;
    }

    /****************************************
    *
     * @return 
    ****************************************/
    public int getCountScrips() {
        return countScrips;
    }

    /****************************************
    *
     * @param countScrips
    ****************************************/
    public void setCountScrips(int countScrips) {
        this.countScrips = countScrips;
    }

    /****************************************
    *
     * @return 
    ****************************************/
    public int getCountTopics() {
        return countTopics;
    }

    /****************************************
    *
     * @param countTopics
    ****************************************/
    public void setCountTopics(int countTopics) {
        this.countTopics = countTopics;
    }
    
    
    
    /*****************************************
     * RUN
     ****************************************/
    @Override
    public void run(){
        
        for (Entry entry : journal.getEntryList()){
            try {
                Thread.sleep(500);
                countEntries++;
                update();
            } catch (InterruptedException ex) {
                Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        
    }
    
    /****************************************
    * UPDATE
    ****************************************/
    public void update(){
        entriesLabel.setText("Entries found: " + countEntries + "   ");
        scripturesLabel.setText("Scriptures found: " + countScrips + "   ");
        topicsLabel.setText("Topics found: " + countTopics + "   ");
    }


}
