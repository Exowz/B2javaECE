package Produit;

public class Main {
    public static void main(String[] args) {
        Produit livre = new Livre("Les Miserables", 20.0);
        Produit dvd = new DVD("Inception", 15.0);
        
        System.out.println("Prix final du livre: " + livre.calculerPrix() + " EUR");
        System.out.println("Prix final du DVD: " + dvd.calculerPrix() + " EUR");
    }
}