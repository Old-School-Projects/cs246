/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

import java.util.ArrayList;
import java.util.List;

/***************************************************
 * ENTRY CLASS: This class is everything to do with an
 * entry in a file
 * @author paul
 **************************************************/
public class Entry {
    private String date;
    private String content;
    private List<Scripture> scriptures;
    private List<String> topics;

    /**************************************************
     * CONSTRUCTOR
     * @param date
     * @param content
     **************************************************/
    public Entry(String date, String content) {
        this.date = date;
        this.content = content;
    }
    
    /***************************************************
    * CONSTRUCTOR
    ***************************************************/
    public Entry(){
        scriptures = new ArrayList<>();
        topics = new ArrayList<>();
    }
    
    
    
    /***************************************************
    * ADD SCRIPTURE TO SCRIPTURE LIST
    * @param s
    ***************************************************/
    public void addScripture(Scripture s){
        scriptures.add(s); 
    }
    
    public void addTopic(String t){
        topics.add(t);
    }
    
    public void addContent(String c){
        content = c;
    }
    
    /***************************************************
    * DISPLAY SCRIPTURE LIST
    ***************************************************/
    public void displayScripList(){
        for (Scripture scripture : scriptures) {
            System.out.println(scripture.getBook());
        }
    }
    
    /***************************************************
    * GET SCRIPTURE LIST
     * @return scriptures
    ***************************************************/
    public List<Scripture> getScripList() {
        return scriptures;
    }
    
    /**************************************************
    * GET DATE
     * @return date
    **************************************************/
    public String getDate(){
        return date;
    }
    
    /***************************************************
    * SET DATE
     * @param aDate
    ***************************************************/
    public void setDate(String aDate){
        date = aDate;
    }
    
    /***************************************************
     * GET TOPICS
     * @return list of Topics
     **************************************************/
    public List<String> getTopics(){
        return topics;
    }
    
    /**************************************************
    * GET CONTENT
    * @return content
    **************************************************/
    public String getContent(){
        return content;
    }
    
    /**************************************************
    * SET CONTENT
    * @param t
    **************************************************/
    public void setContent(String t){
        content = t;
    }
    
}

