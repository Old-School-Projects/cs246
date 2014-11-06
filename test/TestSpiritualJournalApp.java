/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import spiritualjournalapp.Entry;
import spiritualjournalapp.Journal;
import spiritualjournalapp.Scripture;

/**
 *
 * @author paul
 */
public class TestSpiritualJournalApp {
    
    public TestSpiritualJournalApp() {
    }

    @Test 
    public void bookTester(){
        Journal j = new Journal();
        Entry e = new Entry(" ", "I found Job 2:4 and Alma 4:5 to be cool");
        j.getEntryList().add(e);
        j.findEntriesFromText(e.getContent());
        
        Assert.assertNotEquals(j.getEntryList().get(0).getScripList().get(0).getBook(), "Job");
        Assert.assertEquals(j.getEntryList().get(0).getScripList().get(1).getBook(), "Alma");
    }
    
    @Test
    public void verseTester(){
        Journal j = new Journal();
        Entry e = new Entry(" ", "I found Job 2:4 and Alma 4:5 to be cool");
        
        j.getEntryList().add(e);
        j.findEntriesFromText(e.getContent());
        
        Assert.assertNotEquals(j.getEntryList().get(0).getScripList().get(0).getVerse(), 4);
        Assert.assertEquals(j.getEntryList().get(0).getScripList().get(1).getVerse(), 5);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
