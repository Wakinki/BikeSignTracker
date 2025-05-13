package com.example.bikesigntracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private final String DATA_DIRECTORY = "data";
    private final String SIGNS_FILENAME = "signs";
    private final String filePath;

    @FXML
    private Label welcomeText;
    @FXML
    private TableView<Sign> cycleSignTable;
    @FXML
    private TableColumn<CycleSign, ECycleSignType> typeColumn;
    @FXML
    private TableColumn<CycleSign, String> nameColumn;
    @FXML
    private TableColumn<CycleSign, Integer> amountColumn;
    @FXML
    private TableColumn<CycleSign, Boolean> isPrintedColumn;

    private ObservableList<Sign> signsData = FXCollections.observableArrayList();

    public HelloController() {

        String userHome = System.getProperty("user.home");
        Path dataPath = Paths.get(userHome, "BikeSignTracker", DATA_DIRECTORY);


        try {
            Files.createDirectories(dataPath);
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }

        filePath = dataPath.resolve(SIGNS_FILENAME).toString();
        System.out.println("Data file path: " + filePath);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        isPrintedColumn.setCellValueFactory(new PropertyValueFactory<>("isPrinted"));

        cycleSignTable.setItems(signsData);

        try {
            SignTable signs = SignTable.getInstance();
            SignTable.loadFromFile(filePath);

            signsData.addAll(signs.getSigns());


        } catch (IOException e){
            System.out.println("Error occurred when trying to manipulate file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error occurred when trying to load file");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSaveButtonClick() {
        try {
            SignTable signs = SignTable.getInstance();
            signs.saveToFile(filePath);
        } catch (IOException e){
            System.out.println("Error occurred when trying to save file");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddButtonClick() {

            SignTable signs = SignTable.getInstance();
            signs.addSign(new CycleSign(1, "A1", false, ECycleSignType.RIGHT));
            welcomeText.setText(signs.toString());


    }
}