package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BDD {
    // Informations de connexion à MySQL via MAMP
    private static final String URL_MYSQL = "jdbc:mysql://localhost:8889/";
    private static final String DB_NAME = "user_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection connexion;

    public BDD() {
        try {
            // Connexion à MySQL sans spécifier la base de données
            Connection tempConn = DriverManager.getConnection(URL_MYSQL, USER, PASSWORD);
            System.out.println("Connexion réussie à MySQL.");

            // Création de la base de données si elle n'existe pas
            createDatabaseIfNotExists(tempConn);
            
            // Fermer la connexion temporaire
            tempConn.close();

            // Connexion à la base de données spécifique
            this.connexion = DriverManager.getConnection(URL_MYSQL + DB_NAME, USER, PASSWORD);
            System.out.println("Connexion réussie à la base '" + DB_NAME + "'.");

            // Création de la table si elle n'existe pas
            createTableIfNotExists();

        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            System.exit(1);
        }
    }

    // Création de la base de données si elle n'existe pas
    private void createDatabaseIfNotExists(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String sqlCreateDB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sqlCreateDB);
            System.out.println("Base de données '" + DB_NAME + "' vérifiée/créée.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base : " + e.getMessage());
        }
    }

    // Création de la table utilisateurs si elle n'existe pas
    private void createTableIfNotExists() {
        String sqlCreateTable = """
            CREATE TABLE IF NOT EXISTS utilisateurs (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100) NOT NULL,
                prenom VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                photo VARCHAR(255) DEFAULT 'default.png',
                createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
        """;

        try (Statement stmt = connexion.createStatement()) {
            stmt.executeUpdate(sqlCreateTable);
            System.out.println("Table 'utilisateurs' vérifiée/créée.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table : " + e.getMessage());
        }
    }

    public void close() {
        if (this.connexion != null) {
            try {
                this.connexion.close();
                System.out.println("Connexion fermée avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    public Connection getConnexion() {
        return connexion;
    }
}