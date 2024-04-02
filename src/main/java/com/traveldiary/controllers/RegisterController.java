package com.traveldiary.controllers;

import com.traveldiary.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterController {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void registerBtnClicked() {
        try{
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if(!password.equals(confirmPassword)){
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Password & Confirm Password must match!");
                return;
            }

            if(username.isEmpty()){
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Username must not be empty!");
                return;
            }

            User user = new User(username, password);
            saveUserToFile(user);

        }catch (Exception e){
            throw new Error(e);
        }
    }

    public void showAlert(Alert.AlertType type, String title, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveUserToFile(User user) {
        try {
            Path userFile = Paths.get("users.txt");
            FileWriter writer = new FileWriter(userFile.toFile(), true);
            writer.write(user.username() + "," + user.password() + "\n");
            writer.close();
            showAlert(Alert.AlertType.INFORMATION, "Registration Success", "User registered successfully!");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error occurred while saving user details!");
        }
    }

    public void loginClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) confirmPasswordField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Login screen!");
        }
    }
}
