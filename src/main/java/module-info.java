module com.traveldiary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.traveldiary.application;
    exports com.traveldiary.models;

    opens com.traveldiary.controllers to javafx.fxml;
}