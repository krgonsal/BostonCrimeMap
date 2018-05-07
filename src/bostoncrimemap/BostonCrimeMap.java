package bostoncrimemap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BostonCrimeMap extends Application {

    final String KEY = "AIzaSyCG-qguELJDVr026HcguKNGIVNdVP0GZ8s";
    final String DATE_TO_READ = "1-1-2018 12:00:00";
    ImageView crimeMap = new ImageView();   // For  crime map PNG file
    BorderPane root = new BorderPane(); 
    // Search and Filter Nodes
    BorderPane topPane = new BorderPane();
    VBox vbSearch = new VBox();     // VBox to hold all search and filter nodes
    HBox searchBox = new HBox();    // HBox to hold txtSearch and btnSearch
    ComboBox<String> cbCrime;       // ComboBox for crime type filtering, placed in vbSearch below searchBox
    TextField txtSearch = new TextField();      // Text field for street searches
    Button btnSearch = new Button("Search");    // Button for street search
    // Table Nodes
    TableView<Crime> crimeTable;
    TableColumn<Crime, String> pinColumn = new TableColumn("Pin");  // String value is the name of the column, displayed at the top
    TableColumn<Crime, String> offenseColumn = new TableColumn("Offense");
    TableColumn<Crime, String> descriptionColumn = new TableColumn("Description");
    TableColumn<Crime, String> streetColumn = new TableColumn("Street");
    TableColumn<Crime, String> dateColumn = new TableColumn("Date");
    TableColumn<Crime, Integer> codeColumn = new TableColumn("Offense Code");
    
    
    @Override
    public void start(Stage primaryStage) {
        ObservableList<Crime> crimeList = FXCollections.observableArrayList();
        try {
           // String sourcePath = "Resources/crime.csv";
            File sourceFile = new File("Resources/crime.csv");
            
            System.out.println(sourceFile.exists());
            
            URL sourceUrl = new URL("https://data.boston.gov/dataset/6220d948-eae2-4e4b-8723-2dc8e67722a3/resource/12cb3883-56f5-47de-afa5-3b1cf61b257b/download/crime.csv");
            Scanner input = new Scanner(sourceUrl.openStream());
            input.nextLine();
            Image banner = new Image("Image/Banner.png");   
            ImageView bannerView = new ImageView(banner);
            
            Crime currentCrime;
            // Get Data
            for (int i = 0; i < 1000; i++) {
            //do{
                String line = input.nextLine();
                ArrayList<String> crimeinput = new ArrayList<>(Arrays.asList(line.split(",")));
                currentCrime = new Crime(crimeinput);
                crimeList.add(currentCrime);
                
                // input.close();
            }
            //while(!(currentCrime.getDate().equals(DATE_TO_READ)));

            // Generate url from 10 most current crimes
            ObservableList<Crime> currentCrimes = FXCollections.observableArrayList();
            for (int i = 0; i < 10; i++) {
                if (isValid(crimeList.get(i))) {
                    currentCrimes.add(crimeList.get(i));
                    //System.out.println(crimeList.get(i).getDate());
                } else {
                    crimeList.remove(i);
                }
                
            }
            displayCrimes(currentCrimes);
           
            // Set up search bar
            txtSearch.setPromptText("Search by Street");
            
            searchBox.setSpacing(5);
            searchBox.getChildren().addAll(txtSearch, btnSearch);
            vbSearch.getChildren().add(searchBox);
            vbSearch.setSpacing(5);
            vbSearch.setPadding(new Insets(5,5,5,5));
            topPane.setRight(vbSearch);
            topPane.setLeft(bannerView);
            root.setTop(topPane);
            btnSearch.setOnAction(e -> {
                searchCrimes(crimeList);
            });
             // Set up ComboBox
            ObservableList<String> cbCrimeList = FXCollections.observableArrayList();
            cbCrimeList.addAll("Homicide", "Robbery", "Assault & Battery", "Burglary/B&E", "Larceny", "Auto Theft", "Arson", "Drugs", "Financial Crimes");
            cbCrime = new ComboBox<>();
            cbCrime.setPromptText("Search by Crime Type");
            cbCrime.getItems().addAll(cbCrimeList);
            cbCrime.setOnAction(e -> {
            searchCrimeType(cbCrimeList.indexOf(cbCrime.getValue()), crimeList);
            
            });
            
            
            vbSearch.getChildren().add(cbCrime);
            vbSearch.setAlignment(Pos.BOTTOM_RIGHT);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        root.setLeft(crimeMap);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Boston Crime Map");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void searchCrimes(ObservableList<Crime> crimeList) {
        String street = txtSearch.getText().trim();
        street = street.toUpperCase();
        ObservableList<Crime> searchResults = FXCollections.observableArrayList();
        for (int i = 0; i < crimeList.size() && searchResults.size() < 10; i++) {
            if (crimeList.get(i).getStreet().equals(street)) {
                if (isValid(crimeList.get(i))) {
                    searchResults.add(crimeList.get(i));
                }
            }
        }
        if (searchResults.size() > 1) 
            displayCrimes(searchResults);
        else
            AlertBox.diplay("Street not found", "The street " + street + " was not found.\nEither the spelling was wrong or there is no data for " + street + " in the data set." );
        
        
    }

    public void searchCrimeType(int index, ObservableList<Crime> list) {
        final int MURDER_LOWER = 100,
                MURDER_UPPER = 125,
                ROBBERY_LOWER = 301,
                ROBBERY_UPPER = 381,
                AB_LOWER = 401,
                AB_UPPER = 433,
                BURGLARY_LOWER = 510,
                BURGLARY_UPPER = 562,
                LARCENY_LOWER = 611,
                LARCENY_UPPER = 649,
                AUTO_LOWER = 701,
                AUTO_UPPER = 790,
                ARSON_LOWER = 901,
                ARSON_UPPER = 930,
                DRUG_LOWER = 1805,
                DRUG_UPPER = 1875,
                FINANCIAL_LOWER = 1001,
                FINANCIAL_UPPER = 1201;
        int lowerLimit = 0, upperLimit = 0;
        switch (index) {
            case 0:
                lowerLimit = MURDER_LOWER;
                upperLimit = MURDER_UPPER;
                break;
            case 1:
                lowerLimit = ROBBERY_LOWER;
                upperLimit = ROBBERY_UPPER;
                break;
            case 2:
                lowerLimit = AB_LOWER;
                upperLimit = AB_UPPER;
                break;
            case 3:
                lowerLimit = BURGLARY_LOWER;
                upperLimit = BURGLARY_UPPER;
                break;
            case 4:
                lowerLimit = LARCENY_LOWER;
                upperLimit = LARCENY_UPPER;
                break;
            case 5:
                lowerLimit = AUTO_LOWER;
                upperLimit = AUTO_UPPER;
                break;
            case 6:
                lowerLimit = ARSON_LOWER;
                upperLimit = ARSON_UPPER;
                break;
            case 7:
                lowerLimit = DRUG_LOWER;
                upperLimit = DRUG_UPPER;
                break;
            case 8:
                lowerLimit = FINANCIAL_LOWER;
                upperLimit = FINANCIAL_UPPER;
                break;
        }
        ObservableList<Crime> searchResults = FXCollections.observableArrayList();
        for (int i = 0; i < list.size() && searchResults.size() < 10; i++) {
            if (list.get(i).getOffenseCode() <= upperLimit && list.get(i).getOffenseCode() >= lowerLimit && isValid(list.get(i))) {
                searchResults.add(list.get(i));
            }
        }

        if (searchResults.size() > 0) {
            displayCrimes(searchResults);
        }
        else
            AlertBox.diplay("No Crimes Found", "The are no instances of the crime selected found");
    }
    
    public void displayCrimes(ObservableList<Crime> list) {
        final String KEY = "AIzaSyCG-qguELJDVr026HcguKNGIVNdVP0GZ8s";
        String domain = "https://maps.googleapis.com/maps/api/staticmap?size=450x500";
        String address = domain + "&key=" + KEY;

        for (int i = 0; i < list.size() && address.length() < 8100; i++) {
            address += list.get(i).getMarker(i);
        }

        Image mapImage = new Image(address);
        crimeMap.setImage(mapImage);

        pinColumn.setMinWidth(50);      // Formatting 
        pinColumn.setCellValueFactory(new PropertyValueFactory<>("initial"));   // Sets the value of each cell by calling appropriate getter method

        offenseColumn.setMinWidth(200);
        offenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));

        descriptionColumn.setMinWidth(300);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        streetColumn.setMinWidth(150);
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));

        dateColumn.setMinWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        codeColumn.setMinWidth(50);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("offenseCode"));

        crimeTable = new TableView<>();     // Construct TableView (Constructor is here so the Table will refresh each time method is called)
        crimeTable.setItems(list);
        crimeTable.getColumns().addAll(pinColumn, codeColumn, offenseColumn, descriptionColumn, streetColumn, dateColumn);
        root.setRight(crimeTable);

    }

    public boolean isValid(Crime c) {
        return (isDouble(c.getLatitude()) && isDouble(c.getLatitude()) && c.getDate().length() > 1);
    }

    public boolean isDouble(String s) {
        boolean result = false;
        try {
            if (Double.parseDouble(s) > 0) {
                result = true;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return result;
    }
}
