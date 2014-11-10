/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/************************************************
 * PROPERTIES CLASS
 * @author paul
 ***********************************************/
public class PropertiesClass {
    
    String propertiesFile = "/resources/prop.properties";
    
    /***********************************************
    * GET TOPIC FILE
     * @return 
    ************************************************/
    public String getTopicFile(){
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream(propertiesFile));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        String topicFile = prop.getProperty("terms");
        return topicFile;
    }
    
    /***********************************************
    * GET SCRIPTURE FILE
     * @return 
    ************************************************/
    public String getScriptureFile(){
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream(propertiesFile));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        String scripFile = prop.getProperty("scriptures");
        return scripFile;
    }
    
    
    
}
