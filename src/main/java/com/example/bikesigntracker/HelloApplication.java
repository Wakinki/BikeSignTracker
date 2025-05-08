package com.example.bikesigntracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");

        stage.setScene(scene);
        SignTable signs = SignTable.getInstance();
        signs.addSign(new CycleSign(10, "A28", false, ECycleSignType.LEFT));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}