package com.traveldiary.controllers;

import com.traveldiary.datamanagement.Session;
import com.traveldiary.models.JournalEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

import java.io.File;
import java.io.IOException;

public class AddJournalEntryController {

    @FXML
    private DatePicker date;

    @FXML
    private TextArea description;

    @FXML
    private TextField expsenses;

    @FXML
    private TextField locations;

    @FXML
    private TextField ratings;

    @FXML
    private TextField title;

    @FXML
    private File uploadedImageFile;

    @FXML
    void uploadImagesBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            uploadedImageFile = file;
        }
        else{
            showAlert(Alert.AlertType.ERROR, "Error", "No image selected.");
        }
    }

    @FXML
    void addEntryBtnClicked() {
        LocalDate localDate = date.getValue();
        String descriptionText = description.getText();
        String locationName = locations.getText();
        String totalExpenses = expsenses.getText();
        String rating = ratings.getText();
        String titleText = title.getText();

        if(localDate == null){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide date please!");
            return;
        }

        if(descriptionText.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide description please!");
            return;
        }

        if(locationName.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide location please!");
            return;
        }

        if(rating.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide rating please!");
            return;
        }

        if(titleText.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide title please!");
            return;
        }

        String entryId = UUID.randomUUID().toString();


        try {
            if (uploadedImageFile != null) {
                String uniqueFileName = entryId + getFileExtension(uploadedImageFile.getName());
                String imageDirectoryPath = "Images";
                String destinationPath = Paths.get(imageDirectoryPath, uniqueFileName).toString();
                saveImage(uploadedImageFile, destinationPath);

                JournalEntry entry = new JournalEntry(entryId, Session.getUsername(), titleText, localDate, "Images/"+uniqueFileName, locationName, descriptionText, totalExpenses, rating);

                saveEntry(entry);
                showAlert(Alert.AlertType.INFORMATION, "Entry Added", "Entry added successfully.");
            }else {
                showAlert(Alert.AlertType.ERROR, "Error", "No image selected.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the entry.");
            throw new Error(e);
        }
    }

    private void saveEntry(JournalEntry entry) throws IOException {
        File entryFile = new File("Entries/Entries" + entry.getId() + ".txt");

        String url = entry.getPhotos().getUrl();
        if (url.startsWith("file:/")) {
            url = url.substring("file:/".length());
        }

        try (FileWriter writer = new FileWriter(entryFile)) {
            writer.write("ID: " + entry.getId() + "\n");
            writer.write("Username: " + entry.getUsername() + "\n");
            writer.write("Title: " + entry.getTitle() + "\n");
            writer.write("Date: " + entry.getDate() + "\n");
            writer.write("Image: " +  url + "\n");
            writer.write("Location: " + entry.getLocation() + "\n");
            writer.write("Description: " + entry.getDescription() + "\n");
            writer.write("Expenses: " + entry.getExpenses() + "\n");
            writer.write("Ratings: " + entry.getRatings() + "\n");
        }
    }


    private void saveImage(File sourceFile, String destinationPath) throws IOException {
        File destinationFile = new File(destinationPath);
        File destinationDirectory = destinationFile.getParentFile();

        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void backBtnClicked() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) description.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
