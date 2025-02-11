package Animal;

public class Chat extends Animal {
    // Redéfinition de la méthode faireDuBruit()
    @Override
    public void faireDuBruit() {
        System.out.println("Le chat miaule : Meow Meow !");
    }
}