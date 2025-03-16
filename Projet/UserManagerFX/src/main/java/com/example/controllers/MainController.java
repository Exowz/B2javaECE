package com.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.example.Utilisateur;
import com.example.GestionUtilisateur;
import com.example.BDD;
import com.example.App;

public class MainController {
    @FXML private TableView<Utilisateur> userTable;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colPhoto;
    @FXML private TableColumn<Utilisateur, String> colPassword;
    @FXML private TextField nameField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView profileImageView;
    @FXML private Button btnRetour;

    private GestionUtilisateur gestionUtilisateur;
    private ObservableList<Utilisateur> usersList;
    private String selectedPhoto = "default.png";
    private BDD db;

    public void initialize() {
        System.out.println("Initialisation de MainController...");
        db = new BDD();
        gestionUtilisateur = new GestionUtilisateur(db);

        // Vérifier si le répertoire d'images existe
        checkAndCreateImageDirectory();

        // Vérifier si l'image par défaut existe, sinon la créer
        checkDefaultImage();

        // Lier les colonnes aux propriétés de l'objet Utilisateur
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));

        // Affichage de la photo de profil dans le TableView
        colPhoto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoto()));
        colPhoto.setCellFactory(column -> new TableCell<Utilisateur, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String photoName, boolean empty) {
                super.updateItem(photoName, empty);
                if (empty || photoName == null || photoName.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Path imagePath = Paths.get("src/main/resources/images", photoName);
                        if (Files.exists(imagePath)) {
                            Image image = new Image(imagePath.toUri().toString());
                            imageView.setImage(image);
                        } else {
                            // Utiliser l'image par défaut si l'image spécifiée n'existe pas
                            Path defaultPath = Paths.get("src/main/resources/images/default.png");
                            imageView.setImage(new Image(defaultPath.toUri().toString()));
                        }
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        System.err.println("Erreur chargement image : " + e.getMessage());
                        setGraphic(null);
                    }
                }
            }
        });

        // Configurer la sélection de ligne dans le TableView
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getNom());
                prenomField.setText(newSelection.getPrenom());
                emailField.setText(newSelection.getEmail());
                passwordField.setText(newSelection.getPassword());
                selectedPhoto = newSelection.getPhoto();
                
                try {
                    Path imagePath = Paths.get("src/main/resources/images", selectedPhoto);
                    if (Files.exists(imagePath)) {
                        profileImageView.setImage(new Image(imagePath.toUri().toString()));
                    } else {
                        profileImageView.setImage(new Image(Paths.get("src/main/resources/images/default.png").toUri().toString()));
                    }
                } catch (Exception e) {
                    System.err.println("Erreur de chargement de l'image sélectionnée : " + e.getMessage());
                }
            }
        });

        loadUsers();
    }

    private void checkAndCreateImageDirectory() {
        Path imagesDir = Paths.get("src/main/resources/images");
        if (!Files.exists(imagesDir)) {
            try {
                Files.createDirectories(imagesDir);
                System.out.println("Répertoire d'images créé : " + imagesDir);
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du répertoire d'images : " + e.getMessage());
                showAlert(AlertType.ERROR, "Erreur", "Impossible de créer le répertoire d'images");
            }
        }
    }

    private void checkDefaultImage() {
        Path defaultImage = Paths.get("src/main/resources/images/default.png");
        if (!Files.exists(defaultImage)) {
            try (InputStream is = getClass().getResourceAsStream("/images/default.png")) {
                if (is != null) {
                    Files.copy(is, defaultImage, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Image par défaut copiée avec succès");
                } else {
                    // Créer une image par défaut vide
                    System.out.println("Aucune image par défaut trouvée dans les ressources");
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la copie de l'image par défaut : " + e.getMessage());
            }
        }
    }

    @FXML
    private void loadUsers() {
        System.out.println("Chargement des utilisateurs...");
        List<Utilisateur> users = gestionUtilisateur.listerUtilisateurs();
        usersList = FXCollections.observableArrayList(users);
        userTable.setItems(usersList);

        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.println(users.size() + " utilisateurs chargés.");
        }
    }

    @FXML
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path destination = Paths.get("src/main/resources/images", file.getName());
                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                selectedPhoto = file.getName();
                profileImageView.setImage(new Image(destination.toUri().toString()));
                System.out.println("Image sélectionnée : " + selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erreur", "Impossible de sauvegarder l'image : " + e.getMessage());
            }
        }
    }

    @FXML
    private void ajouterUtilisateur() {
        try {
            String nom = nameField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
        
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
                return;
            }
            
            if (password.length() < 6) {
                showAlert(AlertType.ERROR, "Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
                return;
            }
        
            Utilisateur utilisateur = new Utilisateur(0, nom, prenom, email, password, selectedPhoto);
            boolean success = gestionUtilisateur.ajouterUtilisateur(utilisateur);
            
            if (success) {
                showAlert(AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès.");
                loadUsers();
                clearFields();
            } else {
                showAlert(AlertType.ERROR, "Erreur", "Impossible d'ajouter l'utilisateur. Vérifiez que l'email n'est pas déjà utilisé.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    @FXML
    private void modifierUtilisateur() {
        Utilisateur selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur.");
            return;
        }

        try {
            selectedUser.setNom(nameField.getText());
            selectedUser.setPrenom(prenomField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setPassword(passwordField.getText());
            selectedUser.setPhoto(selectedPhoto);

            boolean success = gestionUtilisateur.modifierUtilisateur(selectedUser);
            if (success) {
                showAlert(AlertType.INFORMATION, "Succès", "Utilisateur mis à jour avec succès.");
                loadUsers();
                clearFields();
            } else {
                showAlert(AlertType.ERROR, "Erreur", "Impossible de mettre à jour l'utilisateur.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerUtilisateur() {
        Utilisateur selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur.");
            return;
        }

        boolean success = gestionUtilisateur.supprimerUtilisateur(selectedUser.getId());
        if (success) {
            showAlert(AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès.");
            loadUsers();
            clearFields();
        } else {
            showAlert(AlertType.ERROR, "Erreur", "Impossible de supprimer l'utilisateur.");
        }
    }

    @FXML
    private void clearFields() {
        nameField.clear();
        prenomField.clear();
        emailField.clear();
        passwordField.clear();
        try {
            profileImageView.setImage(new Image(Paths.get("src/main/resources/images/default.png").toUri().toString()));
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image par défaut : " + e.getMessage());
        }
        selectedPhoto = "default.png";
        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void afficherListeUtilisateurs() {
        System.out.println("🔄 Rafraîchissement de la liste des utilisateurs...");
        loadUsers();
        showAlert(AlertType.INFORMATION, "Info", "Liste des utilisateurs rechargée !");
    }

    @FXML
    private void retourMenu() {
        App.switchScene("MenuView.fxml");
    }
}