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
import javafx.scene.image.Image;
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

public class EditJournalEntryController {

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

    private JournalEntry entryToUpdate;

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
    void updateEntryBtnClicked() {
        LocalDate localDate = date.getValue();
        String descriptionText = description.getText();
        String locationName = locations.getText();
        String totalExpenses = expsenses.getText();
        String rating = ratings.getText();
        String titleText = title.getText();

        if (localDate == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide date please!");
            return;
        }

        if (descriptionText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide description please!");
            return;
        }

        if (locationName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide location please!");
            return;
        }

        if (rating.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide rating please!");
            return;
        }

        if (titleText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Provide title please!");
            return;
        }

        if (entryToUpdate != null) {
            // Update the entry details
            entryToUpdate.setDate(localDate);
            entryToUpdate.setDescription(descriptionText);
            entryToUpdate.setLocation(locationName);
            entryToUpdate.setExpenses(totalExpenses);
            entryToUpdate.setRatings(rating);
            entryToUpdate.setTitle(titleText);

            // Update the entry details in the corresponding file
            String entryFilePath = "Entries/Entries" + entryToUpdate.getId() + ".txt";
            String imagePath = entryToUpdate.getPhotos().getUrl().substring("file:/".length());

            try {
                // Update the entry file
                updateEntryInFile(entryToUpdate, entryFilePath);

                // If needed, update the image file
                if (uploadedImageFile != null) {
                    String uniqueFileName = entryToUpdate.getId() + getFileExtension(uploadedImageFile.getName());
                    String imageDirectoryPath = "Images";
                    String destinationPath = Paths.get(imageDirectoryPath, uniqueFileName).toString();
                    saveImage(uploadedImageFile, destinationPath);

                    System.out.println(uniqueFileName);
                    // Update the entry with the new image path
                    entryToUpdate.setPhotos(new Image(new File("Images/" + uniqueFileName).toURI().toString()));

                    // Copy the new image file to the destination
                    Files.copy(uploadedImageFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                }

                showAlert(Alert.AlertType.INFORMATION, "Entry Updated", "Entry updated successfully.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update entry.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No entry selected for update.");
        }
    }


    private void updateEntryInFile(JournalEntry entry, String entryFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(entryFilePath))) {
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // Update the relevant lines in the file
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "Date":
                            line = "Date: " + entry.getDate();
                            break;
                        case "Location":
                            line = "Location: " + entry.getLocation();
                            break;
                        case "Description":
                            line = "Description: " + entry.getDescription();
                            break;
                        case "Expenses":
                            line = "Expenses: " + entry.getExpenses();
                            break;
                        case "Ratings":
                            line = "Ratings: " + entry.getRatings();
                            break;
                        case "Title":
                            line = "Title: " + entry.getTitle();
                            break;
                    }
                }
                content.append(line).append("\n");
            }

            // Write the updated content back to the file
            try (FileWriter writer = new FileWriter(entryFilePath)) {
                writer.write(content.toString());
            }
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

    public void populateFields(JournalEntry entry) {
        // If entry is not null, it means we are in edit mode
        if (entry != null) {
            entryToUpdate = entry; // Store the entry to be updated
            // Populate the fields with the details of the selected entry
            date.setValue(entry.getDate());
            description.setText(entry.getDescription());
            locations.setText(entry.getLocation());
            expsenses.setText(entry.getExpenses());
            ratings.setText(entry.getRatings());
            title.setText(entry.getTitle());
            // You may need to handle the image separately if needed
        }
    }
}
