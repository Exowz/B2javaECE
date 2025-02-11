package Person;

public class Person {
    // Attributs
    private String nom;
    private int age;

    // Constructeur par défaut
    public Person() {
        this.nom = "";
        this.age = 0;
    }

    // Constructeur avec paramètres
    public Person(String nom, int age) {
        this.nom = nom;
        this.age = age;
    }

    // Méthode pour se présenter
    public void sePresenter() {
        System.out.println("Je m'appelle " + this.nom + " et j'ai " + this.age + " ans.");
    }
}