package Produit;

public class Produit {
    protected String nom;
    protected double prixHT;

    public Produit(String nom, double prixHT) {
        this.nom = nom;
        this.prixHT = prixHT;
    }

    public double calculerPrix() {
        return prixHT;
    }
}

class Livre extends Produit {
    private static final double TVA_LIVRE = 0.05;

    public Livre(String nom, double prixHT) {
        super(nom, prixHT);
    }

    @Override
    public double calculerPrix() {
        return prixHT * (1 + TVA_LIVRE);
    }
}

class DVD extends Produit {
    private static final double TVA_DVD = 0.20;

    public DVD(String nom, double prixHT) {
        super(nom, prixHT);
    }

    @Override
    public double calculerPrix() {
        return prixHT * (1 + TVA_DVD);
    }
}

