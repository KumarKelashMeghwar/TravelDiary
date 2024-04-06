package com.traveldiary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ForgotController {

    @FXML
    private TextField username;

    @FXML
    void recoverNowClicked() {
        try{
            String name = username.getText();
            String password  = "";

            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 2 && parts[0].equals(name)){
                    password = parts[1];
                    showAlert(Alert.AlertType.INFORMATION, "Your password is " + parts[1]);
                }
            }
            reader.close();

            if(password.isEmpty()){
                showAlert(Alert.AlertType.ERROR, "This username doesn't exist! Register a new account, please.");
            }
        } catch (Exception e){
            throw new Error(e);
        }

    }

    @FXML
    void backBtnClicked() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Diary");
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Password Recovery");
        alert.setContentText(content);
        alert.showAndWait();
    }

}
