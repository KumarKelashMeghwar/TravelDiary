package com.traveldiary.controllers;

import com.traveldiary.datamanagement.Session;
import com.traveldiary.models.JournalEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    public Button addJournalButton;

    @FXML
    private TableColumn<JournalEntry, Void> editColumn;

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
    private TableColumn<JournalEntry, Image> photos;

    @FXML
    private TableColumn<JournalEntry, String> ratings;

    @FXML
    private TableColumn<JournalEntry, String> title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        locations.setCellValueFactory(new PropertyValueFactory<>("location"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratings.setCellValueFactory(new PropertyValueFactory<>("ratings"));
        expenses.setCellValueFactory(new PropertyValueFactory<>("expenses"));
        photos.setCellValueFactory(new PropertyValueFactory<>("photos"));


        photos.setCellFactory(param -> new TableCellWithImage());

        description.setCellFactory(param -> new TableCellWithWrap());

        loadEntriesForUser(Session.getUsername());

        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit Entry");

            {
                editButton.setOnAction(event -> {
                    JournalEntry entry = getTableView().getItems().get(getIndex());
                    editEntry(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    ObservableList<JournalEntry> list = FXCollections.observableArrayList();
    private void loadEntriesForUser(String username) {

        String entriesDirectory = "Entries";

        try {
            Files.walk(Paths.get(entriesDirectory)).filter(Files::isRegularFile).forEach(filePath -> {
                try {
                    JournalEntry entry = readEntryFromFile(filePath.toFile(), username);
                    if (entry != null) {
                        list.add(entry);
                    }
                } catch (IOException e) {
                    throw new Error(e);
                }
            });
        } catch (IOException e) {
            throw new Error(e);
        }

        journalEntriesTable.setItems(list);
    }

    private JournalEntry readEntryFromFile(File file, String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            JournalEntry entry = new JournalEntry();
            boolean isEntryForUser = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "ID":
                            entry.setId(parts[1]);
                            break;
                        case "Username":
                            if (parts[1].equals(username)) {
                                isEntryForUser = true;
                            }
                            break;
                        case "Title":
                            entry.setTitle(parts[1]);
                            break;
                        case "Date":
                            entry.setDate(LocalDate.parse(parts[1]));
                            break;
                        case "Image":
                            entry.setPhotos(new Image(new File(parts[1]).toURI().toString()));
                            break;
                        case "Location":
                            entry.setLocation(parts[1]);
                            break;
                        case "Description":
                            entry.setDescription(parts[1]);
                            break;
                        case "Expenses":
                            entry.setExpenses(parts[1]);
                            break;
                        case "Ratings":
                            entry.setRatings(parts[1]);
                            break;
                    }
                }
            }
            if (isEntryForUser) {
                return entry;
            } else {
                return null;
            }
        }
    }

    private void editEntry(JournalEntry entry) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditJournalEntry.fxml"));
        try {
            Parent root = loader.load();
            EditJournalEntryController controller = loader.getController();
            controller.populateFields(entry); // Pass the selected entry to the controller
            Stage stage = (Stage) journalEntriesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Entry");
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void removeBtnClicked() {
        JournalEntry selectedItem = journalEntriesTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Entry");
            alert.setContentText("Are you sure you want to delete this entry?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    deleteEntry(selectedItem);
                }
            });
        } else {
            showAlert(Alert.AlertType.ERROR, "Removal Error", "No item selected for removal.");
        }
    }

    private void deleteEntry(JournalEntry entry) {
        list.remove(entry);

        String entryFilePath = "Entries/Entries" + entry.getId() + ".txt";
        String imagePath = entry.getPhotos().getUrl().substring("file:/".length());

        File entryFile = new File(entryFilePath);
        if (entryFile.exists()) {
            if (!entryFile.delete()) {
                System.out.println("Failed to delete entry file: " + entryFilePath);
            }
        }

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (!imageFile.delete()) {
                System.out.println("Failed to delete image file: " + imagePath);
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Deletion Confirmation", "Entry deleted successfully.");
    }

    public void addNowBtnClicked() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddJournalEntry.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) journalEntriesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TableCellWithImage extends javafx.scene.control.TableCell<JournalEntry, Image> {
        private final ImageView imageView;

        public TableCellWithImage() {
            imageView = new ImageView();
            imageView.setFitWidth(200);
            imageView.setFitHeight(120);
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

    private static class TableCellWithWrap extends javafx.scene.control.TableCell<JournalEntry, String> {
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
    }

    @FXML
    void signOutClicked() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) journalEntriesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
