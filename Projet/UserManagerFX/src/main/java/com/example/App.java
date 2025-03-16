package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage; // Référence globale de la fenêtre principale

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layouts/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Ajout du fichier CSS pour styliser l'application
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        stage.setTitle("UserManagerFX");
        stage.setScene(scene);
        stage.setResizable(true);

        // Afficher un message de bienvenue
        showWelcomeMessage();

        stage.show();
    }

    /**
     * Affiche un message de bienvenue à l'ouverture de l'application.
     */
    private void showWelcomeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Bienvenue !");
        alert.setHeaderText(null);
        alert.setContentText("Bienvenue sur UserManagerFX, l'application de gestion des utilisateurs.\nVeuillez vous connecter pour continuer.");
        alert.showAndWait();
    }

    /**
     * Change la scène actuelle.
     * @param fxmlFile Chemin du fichier FXML de la nouvelle vue
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/layouts/" + fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

            // Ajout du fichier CSS après changement de scène
            scene.getStylesheets().add(App.class.getResource("/styles/main.css").toExternalForm());

            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du changement de scène : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}