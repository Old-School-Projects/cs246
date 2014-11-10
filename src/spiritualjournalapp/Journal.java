/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***********************************************
 * JOURNAL CLASS
 * @author paul
 **********************************************/
public class Journal {
    private List<Entry> entryList;
    private List<String> validBookList;
    private List<String> validTopicList;
    private List<String> topicList;
    private HashMap<String, String> mapOfTopics;
    
    /*************************************************
    * CONSTRUCTOR: create arrayList
    *************************************************/
    public Journal(){
        entryList = new ArrayList<>();
        validBookList = new ArrayList<>();
        validTopicList = new ArrayList<>();
        mapOfTopics = new HashMap<>();
        topicList = new ArrayList<>();
    }
    
    /***************************************************
    * GET VALID LIST
     * @return validBookList
    **************************************************/
    public List<String> getValidBookList(){
        return validBookList;
    }
    
    /***************************************************
    * GET VALID TOPIC LIST
     * @return validTopicList
    **************************************************/
    public List<String> getValidTopicList(){
        return validTopicList;
    }
    
    /**************************************************
    * SPLIT CONTENT INTO LIST OF STRINGS
     * @param content
     * @return 
    **************************************************/
    public List<String> split(String content){
        List<String>lines = new ArrayList<>();
        
        String line = "";
        for (char letter : content.toCharArray()){
            if (letter != '\n'){
                line += letter;
            } else {
                lines.add(line);
                line = "";
            }
        }
        
        return lines;
    }
    
    /**************************************************
    * GET TOPIC LIST
     * @return 
    **************************************************/
    public List<String> getTopicList(){
        return topicList;
    }   
    
    /*************************************************
    * GET ENTRY LIST
     * @return 
    *************************************************/
    public List<Entry> getEntryList(){
        return entryList;
    }
    
    /**********************************************
    * READ TITLE
     * @param file
     * @return 
     * @throws java.lang.Exception
    **********************************************/
    public List<String> readTextFile(String file) throws Exception{
        
        String line;
        List <String> lists = new ArrayList();
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        while ((line = reader.readLine()) != null){
            lists.add(line);
        }

        return lists;
    }
    
    
    /*********************************************
     * SEARCH ENTRIES
     * @param item
     * @return 
     **********************************************/
    public List<Entry> searchEntries(String item){
        List<Entry> list = new ArrayList<>();
        for (Entry entry : entryList){
            for (Scripture scrip : entry.getScripList()){
                if (scrip.getBook().equals(item)){ 
                    list.add(entry);
                }
                
            }
            
            for (String topic : entry.getTopics()){
                if (topic.equals(item)){
                    list.add(entry);
                }
            }
            
            if (entry.getDate().equals(item)){
                list.add(entry);
            }
            
        }

        return list;
    }
    /*******************************************
    * FILL HASH MAP
    *******************************************/
    public void fillHashMap(){
        // fill HashMap with correct values
        String fileName = "/Users/paul/ScripFinderTestApp/src/spiritualjournalapp/topics.txt";
        
        String scripFile = "";
        String line = "";
        
        PropertiesClass p = new PropertiesClass();
        scripFile = p.getTopicFile();
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader(scripFile));
            while ((line = reader.readLine()) != null){
               
                String [] parts = null;
                String [] parts2 = null;
                parts = line.split(":");
                String value = parts[0];
                parts2 = parts[1].split(",");
                
                
                for (String term : parts2){
                    mapOfTopics.put(term, value);  
                }
                
            }
        } catch (IOException e){
            System.out.println("Error reading list from file");
        }
        
        
        
        /*try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((fileLine = reader.readLine()) != null){
               
                String [] parts = null;
                String [] parts2 = null;
                parts = fileLine.split(":");
                String value = parts[0];
                parts2 = parts[1].split(",");
                
                
                for (String term : parts2){
                    mapOfTopics.put(term, value);  
                }
                
            }
        } catch (IOException e){
            System.out.println("Error reading list from file");
        }*/
        
    }
    
    
    /***********************************************
    * FIND TOPICS: this will add all the topics found 
    * and return a list of them
    ***********************************************/
    public void findTopics(){
        
        fillHashMap();
        
        for (String term : mapOfTopics.keySet()){
            term = term.toLowerCase();
        
       
        for (Entry entry : entryList){
            if (entry.getContent() != null){
                String content = entry.getContent();
            content = content.toLowerCase();
            
            
            if (content.contains(term)){
                if (!(entry.getTopics().contains(mapOfTopics.get(term)))){
                    entry.addTopic(mapOfTopics.get(term));          
                }
            }
            }
            
        }

        }
    }
    
    
    /************************************************
    * FIND ENTRIES IN TEXT FILE
     * @param userContent
    ************************************************/
    public void findEntriesFromText(String userContent){
        String pat_B_num_c_num = "(\\w*)(\\s)(\\d+)(\\W*)(\\d+)";
        String pat_num_B_num_c_num = "(\\d)(\\s)(\\w*)(\\s)(\\d+)(\\W*)(\\d+)";
        
        Pattern p1 = Pattern.compile(pat_B_num_c_num);
        Pattern p2 = Pattern.compile(pat_num_B_num_c_num);
        
        Entry entry = new Entry();
        
        String content = "";
        boolean newEntry = false;
        
        List<String>lists = split(userContent);
        
        for (int i = 0; i < lists.size(); i++){
            
            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
            entry.setDate(sdf.format(date));
            
            // find the scriptures
            Matcher m1 = p1.matcher(lists.get(i));                                            
            Matcher m2 = p2.matcher(lists.get(i));

            String scripture = "";
            
            while (m1.find()){
                scripture = m1.group();
                Scripture scrip = new Scripture();
                
                String scriptParts[] = null;
                String scriptBook[] = null;
                if (scripture.split(" ").length == 2){
                    scriptBook = scripture.split(" ");
                    scrip.setBook(scriptBook[0]);
                    scriptParts = scriptBook[1].split(":");
                    scrip.setChapter(Integer.parseInt(scriptParts[0]));
                    scrip.setVerse(Integer.parseInt(scriptParts[1]));
                    entry.addScripture(scrip);
                    
                }  
            
            }
            
             while (m2.find()){
                scripture = m2.group();
                
                Scripture scrip = new Scripture();
                
                String scriptParts[] = null;
                String scriptBook[] = null;
                
                if(scripture.split(" ").length == 3){
                    scriptBook = scripture.split(" ");
                    scrip.setBook(scriptBook[0] + " " + scriptBook[1]);
                    scriptParts = scriptBook[2].split(":");
                    scrip.setChapter(Integer.parseInt(scriptParts[0]));
                    scrip.setVerse(Integer.parseInt(scriptParts[1]));
                    entry.addScripture(scrip);  
                }         
            }
            
             // find the topics
             findTopics();
            
            content = content + lists.get(i);
        }
        entry.setContent(content); // add it one more time for the last entry
        findTopics(); // same here
        entryList.add(entry);   
    
    }
    
    /***********************************************
    * FIND ENTRY
     * @param lists
    ***********************************************/
    public void findEntries(List<String>lists){
        
        String pat_B_num_c_num = "(\\w*)(\\s)(\\d+)(\\W*)(\\d+)";
        String pat_num_B_num_c_num = "(\\d)(\\s)(\\w*)(\\s)(\\d+)(\\W*)(\\d+)";
        
        Pattern p1 = Pattern.compile(pat_B_num_c_num);
        Pattern p2 = Pattern.compile(pat_num_B_num_c_num);
        
        Entry entry = new Entry();
        
        String content = "";
        boolean newEntry = false;
        
        for (int i = 0; i < lists.size(); i++){
            
            
            if (lists.get(i).equals("-----")){
                if (entry != null){
                    entry.setContent(content);
                    content = "";
                    newEntry = true;
                }
                
                entry = new Entry();
                entry.setDate(lists.get(i+1));
                 
            }
            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
            entry.setDate(sdf.format(date));

            
            // find the scriptures
            Matcher m1 = p1.matcher(lists.get(i));                                            
            Matcher m2 = p2.matcher(lists.get(i));

            String scripture = "";
            
            while (m1.find()){
                scripture = m1.group();
                Scripture scrip = new Scripture();
                
                String scriptParts[] = null;
                String scriptBook[] = null;
                if (scripture.split(" ").length == 2){
                    scriptBook = scripture.split(" ");
                    scrip.setBook(scriptBook[0]);
                    scriptParts = scriptBook[1].split(":");
                    scrip.setChapter(Integer.parseInt(scriptParts[0]));
                    scrip.setVerse(Integer.parseInt(scriptParts[1]));
                    entry.addScripture(scrip);
                    
                }  
            
            }
            
             while (m2.find()){
                scripture = m2.group();
                
                Scripture scrip = new Scripture();
                
                String scriptParts[] = null;
                String scriptBook[] = null;
                
                if(scripture.split(" ").length == 3){
                    scriptBook = scripture.split(" ");
                    scrip.setBook(scriptBook[0] + " " + scriptBook[1]);
                    scriptParts = scriptBook[2].split(":");
                    scrip.setChapter(Integer.parseInt(scriptParts[0]));
                    scrip.setVerse(Integer.parseInt(scriptParts[1]));
                    entry.addScripture(scrip);  
                }         
            }
            
             // find the topics
             findTopics();
  
             
            // find all the content
            if (lists.get(i).equals("-----") && newEntry == true){
                entry.setContent(content);
                entryList.add(entry);
            }
            else{
                if (!(lists.get(i).equals(entry.getDate())))
                content = content + lists.get(i);
            }
            
            
            content = content + lists.get(i);
        }
        
        entry.setContent(content); // add it one more time for the last entry
        findTopics(); // same here
        entryList.add(entry);
        
    }
    
    /**********************************************
    * TEST DISPLAY FOR ENTRY LIST
    ***********************************************/
    public void displayEntryList(){

        for (Entry e : entryList){
            System.out.println("-----");
            System.out.println(e.getDate());
            System.out.println(e.getContent().trim());
        }
    }
    
    /*************************************************
     * BUILD XML FILE
     * @param entries
     * @return doc
     * @throws ParserConfigurationException 
     *************************************************/
    public Document buildXMLDocument(List<Entry>entries) throws ParserConfigurationException{
        System.out.println("Building document...");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.newDocument();
        
        Element rootElement = doc.createElement("journal");
        doc.appendChild(rootElement);
        
        for (Entry entry : entries){
            if (entry.getContent() != null){
            Element e = doc.createElement("entry");
            rootElement.appendChild(e);
            e.setAttribute("date", entry.getDate());
            
            for (Scripture s : entry.getScripList()){
                Element scripture = doc.createElement("scripture");
                e.appendChild(scripture);
                String chapter = Integer.toString(s.getChapter());
                String verse = Integer.toString(s.getVerse());
                scripture.setAttribute("book", s.getBook());
                scripture.setAttribute("chapter", chapter);
                scripture.setAttribute("verse", verse);
                
            }
            
            for (String t : entry.getTopics()){
                Element topic = doc.createElement("topic");
                e.appendChild(topic);
                topic.appendChild(doc.createTextNode(t)); 
                
            }
            
            Element content = doc.createElement("content");
            e.appendChild(content);
            content.appendChild(doc.createTextNode(entry.getContent()));
            }
        }
        
        return doc;
    }
    
    /***********************************************
    * SAVE DOCUMENT
     * @param doc
     * @param file
     * @throws java.lang.Exception
    ***********************************************/
    public void saveDocument (Document doc, File file) throws Exception {
        
        Transformer t = TransformerFactory.newInstance().newTransformer();
        
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);
        
        StreamResult result = new StreamResult(file);
        
         t.transform(source, result);
        
        System.out.println("File saved!");
        
        
    }
    
    /*****************************************
    * WRITE TO FILE: this function will write
    * to a file from an XML document
     * @param fileName 
     * @throws java.io.FileNotFoundException 
    *****************************************/
    public void writeToFile(String fileName) throws FileNotFoundException {
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (Entry entry : entryList){
                writer.println("-----");
                writer.println(entry.getDate());
                writer.println(entry.getContent());
            }
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Written to txt file...");
    }
    
    /*****************************************
    * READ XML FILE
    * This function will read in an XML file.
    * @author paul
    * @param fileName
    * @throws java.lang.Exception
    ****************************************/
    public void readXMLfile(String fileName) throws Exception {
        
        //fileName = "/Users/paul/ScripFinderTestApp/src/spiritualjournalapp/xmlFile.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileName);
        
        System.out.println("Loading " + document.getDocumentURI());
        
        document.normalize();
        
        // Iterating throught the nodes and extracting the data
        NodeList nodeList = document.getElementsByTagName("entry");
         
        String date;
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element){
                
                Entry entry = new Entry();
                
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element aElement = (Element) node;
                    date = aElement.getAttribute("date");
                    
                    entry.setDate(date);
                    
                    NodeList scripList = aElement.getElementsByTagName("scripture");
                    
                    for (int it = 0;it < scripList.getLength(); it++){
                        Scripture s = new Scripture();
                        Node sNode = scripList.item(it);
                        Element sElement = (Element)sNode;
                        s.setBook(sElement.getAttribute("book"));
                        
                        entry.addScripture(s);
                        
                          
                    }
                    
                    NodeList topicList = aElement.getElementsByTagName("topic");
                    
                    for (int j = 0;j < topicList.getLength(); j++){
                        String t;
                        Node tNode = topicList.item(j);
                        Element tElement = (Element)tNode;
                        t = tElement.getTextContent();
                        
                        entry.addTopic(t);
                    }
                    
                    NodeList contentList = aElement.getElementsByTagName("content");
                    
                    for (int q = 0; q < contentList.getLength(); q++){
                        String content;
                        Node cNode = contentList.item(q);
                        Element cElement = (Element) cNode;
                        content = cElement.getTextContent();
                        
                        entry.addContent(content);
                    }
                    
                    
                }
                
                
                entryList.add(entry);
            }
        
        }
    }
    
}

