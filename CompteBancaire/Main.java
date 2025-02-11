package CompteBancaire;

public class Main {
    public static void main(String[] args) {
        CompteBancaire compte1 = new CompteBancaire("Ewan", 10000);
        compte1.retirer(500);
        compte1.deposer(300);
        compte1.retirer(3000);
    }
}