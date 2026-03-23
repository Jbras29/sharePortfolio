package fr.utc.miage.shares;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortefeuilleTest {

    private Portefeuille portefeuille;
    private ActionSimple actionApple;
    private ActionCompose etfTech;

    @BeforeEach
    void setUp() {
        // Initialisation d'un portefeuille vide avec une HashMap modifiable avant chaque test
        Map<Action, Integer> mapVide = new HashMap<>();
        portefeuille = new Portefeuille("Mon Portefeuille Test", "PEA", mapVide);

        // Création de quelques actions pour simuler le marché
        actionApple = new ActionSimple("Apple");
        etfTech = new ActionCompose("ETF Tech Leaders");
    }

    @Test
    void testAcheterNouvelleAction() {
        // Achat d'une action qui n'est pas encore dans le portefeuille
        portefeuille.acheterAction(actionApple, 10);

        // Vérification : l'action doit être présente dans la map avec la bonne quantité
        assertTrue(portefeuille.getMapActions().containsKey(actionApple), "Le portefeuille doit contenir l'action Apple.");
        assertEquals(10, portefeuille.getMapActions().get(actionApple), "La quantité d'actions Apple doit être de 10.");
    }

    @Test
    void testAcheterActionExistante() {
        // Achat initial
        portefeuille.acheterAction(actionApple, 10);

        // Nouvel achat de la même action pour tester le cumul
        portefeuille.acheterAction(actionApple, 5);

        // Vérification : la quantité doit s'additionner (10 + 5 = 15)
        assertEquals(15, portefeuille.getMapActions().get(actionApple), "La quantité totale d'actions Apple doit être de 15.");
    }

    @Test
    void testAcheterActionQuantiteInvalide() {
        // Vérification que l'exception IllegalArgumentException est bien levée pour une quantité de 0
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.acheterAction(actionApple, 0);
        }, "Une quantité de 0 doit déclencher une IllegalArgumentException.");

        // Vérification que l'exception est levée pour une quantité négative
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.acheterAction(actionApple, -5);
        }, "Une quantité négative doit déclencher une IllegalArgumentException.");
    }

    @Test
    void testAfficherDetailsPortefeuille() {
        // Redirection de la sortie standard (System.out) pour capturer le texte affiché
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Préparation des données du test : ajout d'une action au portefeuille
        portefeuille.acheterAction(actionApple, 10);

        // Instanciation d'un jour avec une année et un jour spécifiques valides (ex: année 2024, 1er jour)
        Jour aujourdHui = new Jour(2024, 1);

        // Appel de la méthode à tester
        portefeuille.afficherDetailsPortefeuille(aujourdHui);

        // Récupération de la sortie console sous forme de chaîne de caractères
        String sortieConsole = outputStreamCaptor.toString();

        // Vérifications du contenu affiché pour s'assurer que les données clés sont présentes
        assertTrue(sortieConsole.contains("Mon Portefeuille Test"), "L'affichage doit contenir le nom du portefeuille.");
        assertTrue(sortieConsole.contains("Apple"), "L'affichage doit contenir le libellé de l'action.");
        assertTrue(sortieConsole.contains("10"), "L'affichage doit contenir la quantité détenue.");
        assertTrue(sortieConsole.contains("VALEUR TOTALE DU PORTEFEUILLE"), "L'affichage doit contenir la ligne de totalisation.");

        // Restauration de la sortie standard normale pour ne pas perturber les autres tests
        System.setOut(System.out);
    }
}