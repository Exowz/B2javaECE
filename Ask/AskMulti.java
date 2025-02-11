import java.util.Scanner;

public class AskMulti {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez votre prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Entrez votre âge : ");
        int age = scanner.nextInt();

        System.out.print("Entrez votre taille : ");
        double taille = scanner.nextDouble();

        System.out.println("Bonjour " + prenom + ", vous avez " + age + " ans et vous mesurez " + taille + " m.");

        scanner.close();
    }
}
