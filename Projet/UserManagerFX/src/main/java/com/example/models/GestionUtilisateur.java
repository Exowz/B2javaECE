package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionUtilisateur {
    private Connection connexion;

    public GestionUtilisateur(BDD db) {
        this.connexion = db.getConnexion();
    }

    /**
     * Vérifie si la table est vide et réinitialise l'auto-incrémentation si nécessaire.
     */
    public void resetAutoIncrementIfEmpty() {
        try {
            String countQuery = "SELECT COUNT(*) FROM utilisateurs";
            Statement stmt = connexion.createStatement();
            ResultSet rs = stmt.executeQuery(countQuery);

            if (rs.next() && rs.getInt(1) == 0) {
                String resetQuery = "ALTER TABLE utilisateurs AUTO_INCREMENT = 1";
                stmt.executeUpdate(resetQuery);
                System.out.println("Auto-incrément réinitialisé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la réinitialisation de l'auto-incrément : " + e.getMessage());
        }
    }

    /**
     * Ajoute un utilisateur à la base de données.
     */
    public boolean ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, password, photo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getPassword());
            stmt.setString(5, utilisateur.getPhoto());
            int affected = stmt.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère la liste des utilisateurs depuis la base de données.
     */
    public List<Utilisateur> listerUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";

        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("photo")
                );
                utilisateurs.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    /**
     * Met à jour les informations d'un utilisateur.
     */
    public boolean modifierUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, password = ?, photo = ? WHERE id = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getPassword());
            stmt.setString(5, utilisateur.getPhoto());
            stmt.setInt(6, utilisateur.getId());
            int affected = stmt.executeUpdate();
            System.out.println("Utilisateur mis à jour !");
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un utilisateur en fonction de son ID.
     */
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            System.out.println("Utilisateur supprimé !");

            // Vérifier si la table est vide et réinitialiser l'auto-incrément
            if (affected > 0) {
                resetAutoIncrementIfEmpty();
            }
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche un utilisateur par nom, prénom ou email.
     */
    public List<Utilisateur> rechercherUtilisateur(String searchTerm) {
        List<Utilisateur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Utilisateur user = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("photo")
                );
                resultats.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }
        return resultats;
    }

    /**
     * Authentifie un utilisateur avec son email et mot de passe.
     * 
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     * @return L'objet Utilisateur si authentification réussie, null sinon
     */
    public Utilisateur authentifier(String email, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND password = ?";
        
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("photo")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        }
        return null;
    }
}
