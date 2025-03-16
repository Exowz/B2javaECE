package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.Utilisateur;
import com.example.GestionUtilisateur;
import com.example.BDD;

public class DetailUtilisateurController {
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Button backButton;
    
    private Utilisateur user;
    
    public void initialize() {
        // Initialize the controller
        backButton.setOnAction(event -> retourMenu());
    }
    
    public void setUtilisateur(Utilisateur user) {
        this.user = user;
        displayUserDetails();
    }
    
    private void displayUserDetails() {
        if (user != null) {
            idLabel.setText(String.valueOf(user.getId()));
            nameLabel.setText(user.getNom());
            emailLabel.setText(user.getEmail());
        }
    }
    
    private void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/SearchView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Rechercher un utilisateur");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not return to search view: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

