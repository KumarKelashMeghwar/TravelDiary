module com.traveldiary {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.traveldiary.application;
    exports com.traveldiary.models;

    opens com.traveldiary.controllers to javafx.fxml;
}