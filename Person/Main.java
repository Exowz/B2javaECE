package Person;

class Main {
    // Méthode principale pour tester la classe
        public static void main(String[] args) {
            // Instanciation avec le constructeur par défaut
            Person personne1 = new Person();
            personne1.sePresenter(); // Affiche : Je m'appelle  et j'ai 0 ans.
    
            // Instanciation avec le constructeur paramétré
            Person personne2 = new Person("Alice", 25);
            personne2.sePresenter(); // Affiche : Je m'appelle Alice et j'ai 25 ans.
        }
    }