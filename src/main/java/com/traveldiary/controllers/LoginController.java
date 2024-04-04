package com.traveldiary.controllers;

import com.traveldiary.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void loginBtnClicked() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Load user credentials from file
            User user = getUserFromCredentials(username, password);

            if (user != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                try {
                    Parent root = loader.load();
                    Stage stage = (Stage) passwordField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Travel Diary");
                    stage.setResizable(false);
                    stage.setResizable(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + username + "!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password!");
            }

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private User getUserFromCredentials(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return new User(username, password);
                }
            }
            reader.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error occurred while reading user details!");
        }
        return null;
    }

    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void createBtnClicked() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
            stage.setResizable(false);
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
