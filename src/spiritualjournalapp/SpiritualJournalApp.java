/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournalapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    
    
    @Override
    public void start(final Stage primaryStage) {
        journal = new Journal();
        txtContent = new TextArea();
        txtContent.setWrapText(true);
        listview = new ListView();
        
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
        * RIGHT PANE - Search Panel
        * Search text field
        **********************************************/
        HBox searchPanel = new HBox();
        BorderPane rightPane = new BorderPane();
        
        searchPanel.getChildren().add(rightPane);
        searchPanel.setId("searchPanel");
        searchPanel.setPadding(new Insets(30,30,30,30));
        searchPanel.setPrefWidth(250);
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Journal..");
        rightPane.setTop(searchBar);
        
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
        BorderPane bottomPane = new BorderPane();
        bottomPane.setRight(actiontarget);
        
        
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
                    actiontarget.setText("...Entry saved!       ");
                } catch (ParserConfigurationException ex) {
                    System.out.println("Failed to Build XML Document");
                } catch (Exception ex) {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Error! Failed to Save       ");
                }
            }
        });
        
        
        /*********************************************
        * OPEN JOURNAL - file menu
        **********************************************/
        final Text loadDisplay = new Text();
        loadDisplay.setId("displayText");
        rightPane.setCenter(loadDisplay);
        
        MenuItem openBtn = new MenuItem("Open Journal");
        openBtn.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openBtn.setOnAction(new EventHandler<ActionEvent>() {
        int counter = 1;
            @Override
            public void handle(ActionEvent t) {
                
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                
                try {
                    
                    journal.readXMLfile(file.toString());
                    ObservableList<Entry> data = FXCollections.observableArrayList();
                
                for (Entry entry : journal.getEntryList()){
                    data.add(entry);
                    loadDisplay.setText("Number of entries loaded: " + Integer.toString(counter));
                    Thread.sleep(500);
                    primaryStage.show();
                    counter++;
                }

                
                listview.setItems(data);
                listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Don't know what this does
                listview.setCellFactory(new Callback<ListView<Entry>, ListCell<Entry>>() {
                    
                    public ListCell<Entry> call(ListView<Entry> param){
                        counter++;
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
        
        
        /*********************************************
        * FILE MENU ITEMS
        **********************************************/
        fileMenu.getItems().add(newJournal);
        fileMenu.getItems().add(saveBtn);
        fileMenu.getItems().add(openBtn);
        fileMenu.getItems().add(new MenuItem("Export"));  
        fileMenu.getItems().add(new SeparatorMenuItem());  
        fileMenu.getItems().add(new MenuItem("Exit"));  
        menuBar.getMenus().add(fileMenu);
        
        
        /*********************************************
        * EDIT MENU ITEMS
        **********************************************/
        final Menu editMenu = new Menu("Edit");
        editMenu.getItems().add(new MenuItem("Copy"));
        editMenu.getItems().add(new MenuItem("Cut"));
        editMenu.getItems().add(new MenuItem("Paste"));
        menuBar.getMenus().add(editMenu);
        
        // add the menu bar to the HBox
        hbox.getChildren().add(menuBar);
        
        
        
        
        /*********************************************
        * BOTTON PANE
        * HBox
        * new Entry Button
        **********************************************/
        newEntryBox.getChildren().add(bottomPane);
        bottomPane.setRight(actiontarget);
        newEntryBox.setId("bottomHbox");
        newEntryBox.setPrefHeight(30);
        hbox.getChildren().add(newEntryBox);
        Button newEntryBtn = new Button("New Entry");
        newEntryBox.getChildren().add(newEntryBtn);
        newEntryBox.setAlignment(Pos.CENTER_RIGHT);
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

                journal.getEntryList().add(newEntry);
                ObservableList<Entry> data = FXCollections.observableArrayList();
                
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
        
        
        Scene scene = new Scene(border, 950, 550);
        
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
