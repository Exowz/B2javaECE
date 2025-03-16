package com.example;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String photo;

    public Utilisateur(int id, String nom, String prenom, String email, String password, String photo) {
        if (nom == null || nom.trim().isEmpty() ) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }

        this.id = id;
        this.nom = nom.trim();
        this.prenom = prenom.trim();
        this.email = email.trim();
        this.password = password;
        this.photo = (photo != null && !photo.trim().isEmpty()) ? photo : "default.png";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        this.prenom = prenom.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide.");
        }
        this.email = email.trim();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }
        this.password = password;
    }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) {
        this.photo = (photo != null && !photo.trim().isEmpty()) ? photo : "default.png";
    }

    @Override
    public String toString() {
        return "ID:" + id + " Nom: " + nom + " Prénom: " + prenom + " Email: " + email + " Photo: " + photo;
    }
}