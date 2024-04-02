module com.traveldiary {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.traveldiary.application;

    opens com.traveldiary.controllers to javafx.fxml;
}