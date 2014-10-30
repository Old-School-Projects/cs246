/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

/*************************************************
 * Scripture Class: This class has everything to do with
 * a scripture in an entry that one needs.
 * @author paul
 ************************************************/
public class Scripture {
    private String book;
    private int chapter;
    private int verse;

    public Scripture(String book, int chapter, int verse) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
    }

    Scripture() {
        
    }
    
    /**************************************************
     * Getters and Setters for Book
     * @return book
     **************************************************/
    public String getBook(){
        return book;
    }
    public void setBook(String b){
        book = b;
    }
    
    /**************************************************
     * Getters and Setters for Chapter
     * @return chapter
     **************************************************/
    public int getChapter(){
        return chapter;
    }
    public void setChapter(int c){
        chapter = c;
    }
    
    /**************************************************
     * Getters and Setters for Verse
     * @return verse
     **************************************************/
    public int getVerse(){
        return verse;
    }
    public void setVerse(int v){
        verse = v;
    }
    
}
