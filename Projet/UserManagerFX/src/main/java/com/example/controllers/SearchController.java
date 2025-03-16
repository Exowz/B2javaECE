package com.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.Utilisateur;
import com.example.GestionUtilisateur;
import com.example.BDD;
import com.example.App;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchController {
    @FXML private TextField searchField;
    @FXML private TableView<Utilisateur> userTable;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colPassword;
    @FXML private TableColumn<Utilisateur, String> colPhoto;
    @FXML private Button returnButton;

    private GestionUtilisateur gestionUtilisateur;
    private ObservableList<Utilisateur> usersList;

    public void initialize() {
        BDD db = new BDD();
        gestionUtilisateur = new GestionUtilisateur(db);

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
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

        usersList = FXCollections.observableArrayList();
        userTable.setItems(usersList);
        
        // Add double-click event handler to the table
        userTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && userTable.getSelectionModel().getSelectedItem() != null) {
                openUserDetails(userTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    private void searchUser() {
        String searchTerm = searchField.getText();
        if (searchTerm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Recherche", "Veuillez entrer un terme de recherche.");
            return;
        }

        try {
            List<Utilisateur> foundUsers = gestionUtilisateur.rechercherUtilisateur(searchTerm);
            
            usersList.setAll(foundUsers);
            
            if (foundUsers.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Résultat", "Aucun utilisateur trouvé pour: " + searchTerm);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    @FXML
    private void retourMenu() {
        App.switchScene("MenuView.fxml");
    }
    
    private void openUserDetails(Utilisateur user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/UserDetailView.fxml"));
            Parent root = loader.load();
            
            DetailUtilisateurController controller = loader.getController();
            controller.setUtilisateur(user);
            
            Stage stage = (Stage) userTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Détails de l'utilisateur: " + user.getNom());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open user details: " + e.getMessage());
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