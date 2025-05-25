package com.example.bikesigntracker;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HelloController implements Initializable {

    private final String DATA_DIRECTORY = "data";
    private final String SIGNS_FILENAME = "signs";
    private final String filePath;


    @FXML
    private TableView<Sign> cycleSignTable;
    @FXML
    private TableColumn<Sign, Boolean> selectColumn;
    @FXML
    private TableColumn<CycleSign, ECycleSignType> typeColumn;
    @FXML
    private TableColumn<CycleSign, String> nameColumn;
    @FXML
    private TableColumn<CycleSign, Integer> amountColumn;
    @FXML
    private TableColumn<CycleSign, Boolean> isPrintedColumn;

    @FXML
    private Button deleteSelectedButton;

    private ObservableList<Sign> signsData = FXCollections.observableArrayList();

    private ObservableSet<Integer> selectedIndices = FXCollections.observableSet();

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

        cycleSignTable.setEditable(true);

        selectColumn.setCellValueFactory(cellData -> {
            Sign sign = cellData.getValue();
            int index = signsData.indexOf(sign);
            SimpleBooleanProperty property = new SimpleBooleanProperty(selectedIndices.contains(index));

            property.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedIndices.add(index);
                } else {
                    selectedIndices.remove(index);
                }

            });
            return property;
        });
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);


        BooleanBinding isEmpty = Bindings.createBooleanBinding(
                () -> selectedIndices.isEmpty() ,
                selectedIndices
        );

        deleteSelectedButton.visibleProperty().bind(isEmpty.not());
        deleteSelectedButton.managedProperty().bind(deleteSelectedButton.visibleProperty());


        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        amountColumn.setEditable(true);
        isPrintedColumn.setCellValueFactory(new PropertyValueFactory<>("isPrinted"));

        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        amountColumn.setOnEditCommit(event -> {
            CycleSign sign = event.getRowValue();
            sign.setCount(event.getNewValue());
        });

        typeColumn.setText("Směr");
        nameColumn.setText("Název");
        amountColumn.setText("Počet");
        isPrintedColumn.setText("Je vytisklá");

        SignTable signs = SignTable.getInstance();

        try {
            SignTable.loadFromFile(filePath);
        } catch (IOException e){
            System.out.println("Error occurred when trying to manipulate file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error occurred when trying to load file");
            e.printStackTrace();
        }



        signsData = signs.getSigns();
        cycleSignTable.setItems(signsData);

        signsData.addListener((javafx.collections.ListChangeListener<Sign>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    // Clear selection when items are removed to avoid index issues
                    selectedIndices.clear();
                    cycleSignTable.refresh();
                }
            }
        });
    }

    @FXML
    protected void onRemoveSelectedButtonClick() {
        if (selectedIndices.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Žádné značky nevybrány");
            alert.setContentText("Prosím vyberte značky, které chcete odstranit.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);

        confirmAlert.setTitle("Potvrzení odstranění");
        confirmAlert.setContentText("Opravdu chcete odstranit " + selectedIndices.size() + " vybraných značek?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SignTable signs = SignTable.getInstance();
            ArrayList<Sign> signsToRemove = new ArrayList<>();


            for (Integer index : selectedIndices) {
                if (index < signsData.size()) {
                    signsToRemove.add(signsData.get(index));
                }
            }


            signs.removeSigns(signsToRemove);


            selectedIndices.clear();


            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Úspěch");
            successAlert.setContentText("Úspěšně odstraněno " + signsToRemove.size() + " značek.");
            successAlert.showAndWait();
        }
    }

    @FXML
    protected void onSelectAllButtonClick() {
        selectedIndices.clear();
        for (int i = 0; i < signsData.size(); i++) {
            selectedIndices.add(i);
        }
        cycleSignTable.refresh();
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

        Dialog addDialog = new Dialog();
        addDialog.setTitle("Přidejte značku");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField countField = new TextField();
        CheckBox printedCheckBox = new CheckBox();
        ComboBox<ECycleSignType> typeCombo = new ComboBox<>();

        typeCombo.getItems().addAll(ECycleSignType.values());
        typeCombo.setValue(ECycleSignType.FORWARD);

        grid.add(new Label("Název:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Počet:"), 0, 1);
        grid.add(countField, 1, 1);
        grid.add(new Label("Je vytisknutá:"), 0,2);
        grid.add(printedCheckBox, 1, 2);
        grid.add(new Label("Typ:"), 0,3);
        grid.add(typeCombo, 1, 3);

        addDialog.getDialogPane().setContent(grid);
        addDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = addDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                String name = nameField.getText();
                int count = Integer.parseInt(countField.getText());
                boolean isPrinted = printedCheckBox.isSelected();
                ECycleSignType type = typeCombo.getValue();

                if(name.isEmpty() || nameField.getText().isEmpty() ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Prosím vyplňte všechna pole");
                    alert.showAndWait();
                }
                signs.addSign(new CycleSign(count, name, isPrinted, type));

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Počet musí být číslo");
                alert.showAndWait();
            }
        }
    }

//    private void updateRemoveButtonVisibility() {
//        boolean hasSelection = !selectedIndices.isEmpty();
//        deleteSelectedButton.setVisible(hasSelection);
//        deleteSelectedButton.setManaged(hasSelection);
//    }
}