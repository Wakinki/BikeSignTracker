package com.example.bikesigntracker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final String PATH_TO_SIGNS_FILE =  ".." + File.separator + ".." + File.separator + "data" + File.separator + "signs.dat";

    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        SignTable.loadFromFile(PATH_TO_SIGNS_FILE);
        SignTable signs = SignTable.getInstance();
        welcomeText.setText(signs.toString());
        } catch (IOException e){
            System.out.println("Error occurred when trying to manipulate file");
            e.printStackTrace();
        } catch ( ClassNotFoundException e) {
            System.out.println("Error occurred when trying to load file");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onHelloButtonClick() {


        try {
            SignTable.loadFromFile(PATH_TO_SIGNS_FILE);
            SignTable signs = SignTable.getInstance();

            signs.addSign(new CycleSign(3, "A25" , false));
            signs.addSign(new CycleSign(6, "A159" , false));
            signs.addSign(new CycleSign(3, "A290" , false, ECycleSignType.LEFT));
            signs.addSign(new CycleSign(3, "A26" , true, ECycleSignType.RIGHT));
            welcomeText.setText(signs.toString());
            signs.saveToFile(PATH_TO_SIGNS_FILE);

        } catch (IOException e){
            System.out.println("Error occurred when trying to manipulate file");
            e.printStackTrace();
        } catch ( ClassNotFoundException e) {
            System.out.println("Error occurred when trying to load file");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddButtonClick() {
        SignTable signs = SignTable.getInstance();
        signs.addSign(new CycleSign(1, "A1", false, ECycleSignType.RIGHT));
    }

}