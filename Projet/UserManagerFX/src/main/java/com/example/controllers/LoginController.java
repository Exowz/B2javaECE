package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.Utilisateur;
import com.example.GestionUtilisateur;
import com.example.BDD;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    private BDD db;
    private GestionUtilisateur gestionUtilisateur;
    
    @FXML
    public void initialize() {
        db = new BDD();
        gestionUtilisateur = new GestionUtilisateur(db);
        
        // Créer un utilisateur admin par défaut s'il n'existe pas
        creerUtilisateurAdmin();
    }
    
    private void creerUtilisateurAdmin() {
        try {
            // Vérifier si l'utilisateur admin existe déjà
            if (gestionUtilisateur.authentifier("admin@example.com", "admin123") == null) {
                Utilisateur admin = new Utilisateur(
                    0, 
                    "Admin", 
                    "System", 
                    "admin@example.com", 
                    "admin123", 
                    "default.png"
                );
                gestionUtilisateur.ajouterUtilisateur(admin);
                System.out.println("Utilisateur admin créé avec succès.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'utilisateur admin : " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        errorLabel.setText("");  // Réinitialiser le message d'erreur

        // Vérifier si les champs sont vides
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        // Authentification via la base de données
        if ("admin@example.com".equals(username) && "admin123".equals(password)) {
            openMenuView();
        } else {
            Utilisateur user = gestionUtilisateur.authentifier(username, password);
            if (user != null) {
                openMenuView();
            } else {
                errorLabel.setText("Identifiants incorrects. Réessayez.");
            }
        }
    }
    
    @FXML
    private void openMenuView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/MenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menu Principal");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur de chargement du menu: " + e.getMessage());
        }
    }
}