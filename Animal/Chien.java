package Animal;

// Classe enfant Chien
public class Chien extends Animal {
    // Redéfinition de la méthode faireDuBruit()
    @Override
    public void faireDuBruit() {
        System.out.println("Le chien aboie : Woof Woof !");
    }
}
