/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Dialogs.DialogResponse;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxListCell;
import static javafx.scene.input.KeyCode.N;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 *
 * @author paul
 */
public class SpiritualJournalApp extends Application {
    private Journal journal;
    private TextArea txtContent;
    private ListView listview;
    private ObservableList<Entry> data;
    private Threads thread;

    
    @Override
    public void start(final Stage primaryStage) {
        journal = new Journal();
        txtContent = new TextArea();
        txtContent.setWrapText(true);
        listview = new ListView();
        data = FXCollections.observableArrayList();
        
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        final TextField username = new TextField(); 
        username.setPromptText("Username");
        final PasswordField password = new PasswordField(); 
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);



        Callback myCallback = new Callback() {
            @Override
            public Object call(Object p) {
            String usernameResult = username.getText();
            String passwordResult = password.getText();
            return null;
            }
        };

        DialogResponse resp = Dialogs.showCustomDialog(primaryStage, grid, "Please log in to access your journal", "Spiritual Journal App Login", 
                DialogOptions.OK_CANCEL, myCallback);

        Dialogs.showInformationDialog(primaryStage, "To Start:\n" + "1. Click add entry\n" + 
                "OR\n" + "2. Open a saved journal and then click 'add entry'", 
                "Welcome, Paul!", "Spiritual Journal App");
        
    
        /*********************************************
        * MAIN BORDER PANE
        **********************************************/
        BorderPane border = new BorderPane();
        HBox hbox = new HBox();
        hbox.setPrefHeight(30);
        HBox hboxLeft = new HBox();
        hboxLeft.setPrefWidth(200);        
        
        // file menu
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = new Menu("File");
        
        HBox newEntryBox = new HBox();
        
        

        /*********************************************
        * TAB PANE
        **********************************************/
        /*TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab content_tab = new Tab("Entry");
        Tab scripture_tab = new Tab("Scriptures");
        Tab topic_tab = new Tab("Topics");
        
        content_tab.setContent(txtContent);
        
        tp.getTabs().addAll(content_tab, scripture_tab, topic_tab);
        
        
        // Scriptures Tab
        ListView scripturesTab = new ListView();
        scripture_tab.setContent(scripturesTab);
        scripturesTab.setId("scriptTab");
        
        
        // Topics Tab
        ListView topicsTab = new ListView();
        topic_tab.setContent(topicsTab);
        */
        
         /*********************************************
        * RIGHT PANE - Search Panel
        * Search text field
        **********************************************/
        HBox searchPanel = new HBox();
        BorderPane rightPane = new BorderPane();
        searchPanel.getChildren().add(rightPane);
        searchPanel.setId("searchPanel");
        searchPanel.setPadding(new Insets(20,20,20,20));
        searchPanel.setPrefWidth(250);
        final ListView searchResults = new ListView();
        rightPane.setLeft(searchResults);
        searchResults.setPrefHeight(50);
        searchResults.setPrefWidth(150);
        
        final TextField searchBar = new TextField();
        Button searchBtn = new Button("Search");
        Tooltip t = new Tooltip();
        t.setText("Search for scriptures, topics,\n" + "or dates in this journal");
        searchBar.setTooltip(t);
        searchBar.setPromptText("Search Journal..");
        rightPane.setTop(searchBar);
        searchPanel.getChildren().add(searchBtn);
        
        /*********************************************
        * SEARCH JOURNAL BUTTON - right pane
        **********************************************/
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                
                List<Entry>foundEntries = journal.searchEntries(searchBar.getText());
                ObservableList<Entry> data = FXCollections.observableArrayList();
                
                for (Entry entry : foundEntries){
                    data.add(entry);
                }
                
                searchResults.setItems(data);
                searchResults.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {

                    public ListCell<Entry> call(ListView<Entry> param){
                    ListCell<Entry> cell = new ListCell<Entry>(){
                        @Override
                        public void updateItem(Entry entry, boolean empty) {
                            super.updateItem(entry, empty);
                            if (entry != null){
                                    setText(entry.getDate());     
                                     
                            }
                        }
                    };   
                        return cell;
                    
                    };
                    
                    });
                
                    searchResults.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {

                    @Override
                    public void changed(ObservableValue<? extends Entry> ov, Entry old_val, Entry new_val) { 
                        txtContent.setText(new_val.getContent());
                        
                    }
                });
                    
                    
                    }
                });

        /*********************************************
        * NEW JOURNAL BUTTON - file menu
        **********************************************/
        MenuItem newJournal = new MenuItem("New Journal");  
        newJournal.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        
        
        /*********************************************
        * SAVE JOURNAL BUTTON - file menu
        **********************************************/
        MenuItem saveBtn = new MenuItem("Save Journal");
        final Text actiontarget = new Text();
        HBox eHandling = new HBox();
        final BorderPane bottomPane = new BorderPane();
        bottomPane.setRight(eHandling);
        eHandling.getChildren().add(actiontarget);
        
        
        
        saveBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);
                
                String userContent = txtContent.getText();

                try {
                    journal.findEntriesFromText(userContent);
                    Document doc = journal.buildXMLDocument(journal.getEntryList());
                    
                    journal.saveDocument(doc, file);
                    
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("       ...Entry saved!       ");
                } catch (ParserConfigurationException ex) {
                    System.out.println("Failed to Build XML Document");
                } catch (Exception ex) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("       Error! Failed to Save       ");
                }
            }
        });
        
        
        /*********************************************
        * OPEN JOURNAL - file menu
        **********************************************/
        final Text loadDisplay = new Text();
        loadDisplay.setId("displayText");
        //bottomPane.setLeft(loadDisplay);
        
        
        MenuItem openBtn = new MenuItem("Open Journal");
        openBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openBtn.setOnAction(new EventHandler<ActionEvent>() {
        
            @Override
            public void handle(ActionEvent t) {
                int counter = 1;
                int countEntries = 0;
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                
                try {
                    
                    journal.readXMLfile(file.toString());
                    //ObservableList<Entry> data = FXCollections.observableArrayList();
                

                   /* Threads th = new Threads(journal);
                    
                    
                    
                    Thread thread = new Thread(th);
                    thread.start();
                    
                    bottomPane.setRight(th.getEntriesLabel());
                    
                    
                    
                    try{
                        thread.join();
                    } catch (InterruptedException ex) {
                        
                    }
                    */
                    
                    /*
                    for (Entry entry : journal.getEntryList()){
                        countEntries++;
                        Thread.sleep(500);
                        loadDisplay.setText("Entries found: " + countEntries + "   ");
                        primaryStage.show();
                    }
                    //thread.run(journal);
                    
                    */
                    
                    data.clear();
                    for (Entry entry : journal.getEntryList()){
                    data.add(entry);
                    loadDisplay.setText("          Number of entries loaded: " + Integer.toString(counter));
                    primaryStage.show();
                    counter++;
                }

                
                listview.setItems(data);
                listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Don't know what this does
                listview.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {
                    
                    @Override
                    public ListCell<Entry> call(ListView<Entry> param){
                    ListCell<Entry> cell = new ListCell<Entry>(){
                        @Override
                        public void updateItem(Entry entry, boolean empty) {
                            super.updateItem(entry, empty);
                            if (entry != null){
                                    setText(entry.getDate());                                     
                            }
                        }
                    };   
                        return cell;
                    
                    };

                });
                
                listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {

                    @Override
                    public void changed(ObservableValue<? extends Entry> ov, Entry old_val, Entry new_val) { 
                        txtContent.setText(new_val.getContent());
                        
                    }
                });
                
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        });
        
        /********************************************
        * IMPORT TEXT
        *********************************************/
        MenuItem importText = new MenuItem("Import");
        importText.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                String fileName = file.toString();
                
                try {
                    try{
                    List<String> lines = journal.readTextFile(fileName);
                    journal.findEntries(lines);
        
                    Document doc = journal.buildXMLDocument(journal.getEntryList());
        
                    journal.saveDocument(doc, file);
                    
                    data.clear();
                    for (Entry entry : journal.getEntryList()){
                        data.add(entry);
                    }
                    
                    listview.setItems(data);
                    listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Don't know what this does
                    listview.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {
                    
                    public ListCell<Entry> call(ListView<Entry> param){
                    ListCell<Entry> cell = new ListCell<Entry>(){
                        @Override
                        public void updateItem(Entry entry, boolean empty) {
                            super.updateItem(entry, empty);
                            if (entry != null){
                                setText(entry.getDate());           
                            }
                        }
                    };   
                        return cell;
                    
                    };
   
                }); 
                listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {

                        @Override
                        public void changed(ObservableValue<? extends Entry> ov, Entry old_val, Entry new_val) {
                            txtContent.setText(new_val.getContent());
                        }
                    });
                
                } catch (Exception ex) {
                    Logger.getLogger(SpiritualJournalApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   catch (Exception ex) {
                    Logger.getLogger(SpiritualJournalApp.class.getName()).log(Level.SEVERE, null, ex);
                }
        }});
        
        
        /*************************************************
        * EXPORT JOURNAL TO TEXT FILE
        **************************************************/
        MenuItem export = new MenuItem("Export");
        export.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);
                String fileName = file.toString();
                
                try {
                    journal.writeToFile(fileName);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SpiritualJournalApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        /*********************************************
        * FILE MENU ITEMS
        **********************************************/
        fileMenu.getItems().add(newJournal);
        fileMenu.getItems().add(saveBtn);
        fileMenu.getItems().add(openBtn);
        fileMenu.getItems().add(importText); 
        fileMenu.getItems().add(export);
        fileMenu.getItems().add(new SeparatorMenuItem());  
        fileMenu.getItems().add(new MenuItem("Exit"));  
        menuBar.getMenus().add(fileMenu);
        
        
        
        // add the menu bar to the HBox
        hbox.getChildren().add(menuBar);
        
        
        /*********************************************
        * BOTTON PANE
        * HBox
        * new Entry Button
        **********************************************/
        newEntryBox.getChildren().add(bottomPane);
        newEntryBox.getChildren().add(actiontarget);
        bottomPane.setCenter(loadDisplay);
        actiontarget.setTextAlignment(TextAlignment.RIGHT);
        newEntryBox.setId("bottomHbox");
        newEntryBox.setPrefHeight(30);
        hbox.getChildren().add(newEntryBox);
        Button newEntryBtn = new Button("New Entry");
        bottomPane.setLeft(newEntryBtn);
        newEntryBox.setPadding(new Insets(10,10,10,10));
        
        // NEW ENTRY BUTTON
        newEntryBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                txtContent.setText("");
                Entry newEntry = new Entry();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
                newEntry.setDate(sdf.format(date));
                
                data.clear();
                journal.getEntryList().add(newEntry);
                //ObservableList<Entry> data = FXCollections.observableArrayList();
                
                for (Entry entry : journal.getEntryList()){
                    data.add(entry);
                }

                listview.setItems(data);
                listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Don't know what this does
                listview.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {
                    public ListCell<Entry> call(ListView<Entry> param){
                    ListCell<Entry> cell = new ListCell<Entry>(){
                        @Override
                        public void updateItem(Entry entry, boolean empty) {
                            super.updateItem(entry, empty);                           
                                if (!empty){
                                    setText(entry.getDate());
                                }
                        }
                    };
                        return cell;
                    };
                });
                
                
                // Select Item on List View and view content
                listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {

                    @Override
                    public void changed(ObservableValue<? extends Entry> ov, Entry old_val, Entry new_val) { 
                            txtContent.setText(new_val.getContent());
                    }
                });
 
            }
        });
        
        
       

        /*********************************************
        * LEFT PANE - List View
        **********************************************/
        hboxLeft.getChildren().add(listview);
        
        /*********************************************
        * MAIN BORDER PANE SET UP
        **********************************************/
        border.setTop(hbox);
        border.setLeft(hboxLeft);
        border.setRight(searchPanel);
        border.setCenter(txtContent);
        border.setBottom(newEntryBox);
        
        
        Scene scene = new Scene(border, 950, 650);
        
        primaryStage.setTitle("Spiritual Journal");
        scene.getStylesheets().add
        (SpiritualJournalApp.class.getResource("MainPage.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
