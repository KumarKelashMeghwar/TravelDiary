package com.traveldiary.controllers;

import com.traveldiary.models.JournalEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableColumn<JournalEntry, Integer> SNo;

    @FXML
    private Button addJournalButton;

    @FXML
    private TableColumn<JournalEntry, LocalDate> date;

    @FXML
    private TableColumn<JournalEntry, String> description;

    @FXML
    private TableColumn<JournalEntry, String> expenses;

    @FXML
    private TableView<JournalEntry> journalEntriesTable;

    @FXML
    private TableColumn<JournalEntry, String> locations;

    @FXML
    private TableColumn<JournalEntry, Image> photos; // Change type to Image

    @FXML
    private TableColumn<JournalEntry, String> ratings;

    @FXML
    private TableColumn<JournalEntry, String> title;


    ObservableList<JournalEntry> list = FXCollections.observableArrayList(
            new JournalEntry(1, "Japan Tour", LocalDate.now(), "src/main/resources/images/photo1.jpg", "Japan", "adsadasd adsasdasd ads", "140", "5.0 stars"),
            new JournalEntry(2, "Afghanistan Tour", LocalDate.now(), "src/main/resources/images/photo2.jpg", "Afghanistan", "qeeqweqwedasd ads", "20", "4.0 stars"),
            new JournalEntry(1, "Japan Tour", LocalDate.now(), "src/main/resources/images/photo1.jpg", "Japan", "adsadasd adsasdasd ads", "140", "5.0 stars"),
            new JournalEntry(2, "Afghanistan Tour", LocalDate.now(), "src/main/resources/images/photo2.jpg", "Afghanistan", "qeeqweqwedasd ads", "20", "4.0 stars")


    );


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SNo.setCellValueFactory(new PropertyValueFactory<>("SNo"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        locations.setCellValueFactory(new PropertyValueFactory<>("location"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratings.setCellValueFactory(new PropertyValueFactory<>("ratings"));
        expenses.setCellValueFactory(new PropertyValueFactory<>("expenses"));

        photos.setCellValueFactory(new PropertyValueFactory<>("photos"));


        photos.setCellFactory(param -> new TableCellWithImage());

        description.setCellFactory(new Callback<>() {
            @Override
            public TableCell<JournalEntry, String> call(TableColumn<JournalEntry, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setWrapText(true);
                        }
                    }
                };
            }
        });

        journalEntriesTable.setItems(list);
    }

    // Custom TableCell class to display images
    private static class TableCellWithImage extends javafx.scene.control.TableCell<JournalEntry, Image> {
        private final ImageView imageView;

        public TableCellWithImage() {
            imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            setGraphic(imageView);
        }

        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                imageView.setImage(null);
            } else {
                imageView.setImage(item);
            }
        }
    }


}
