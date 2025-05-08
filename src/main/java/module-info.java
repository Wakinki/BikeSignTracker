module com.example.bikesigntracker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.bikesigntracker to javafx.fxml;
    exports com.example.bikesigntracker;
}