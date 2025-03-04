package mon-sql.src.main.java.com.monprojet;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;

    public Utilisateur(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkName(String name){
        if(!name.equals("")){
            return true;
        }

        return false;
    }

    public void __String() {
        System.out.println("Nom: " + nom + ", Email: " + email);
    }
}
