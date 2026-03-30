/*
 * Copyright 2025 David Navarre <David.Navarre at irit.fr>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utc.miage.shares;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortefeuilleTest {

    private Portefeuille portefeuille;
    private Action actionApple;
    private Action actionGoogle;

    @BeforeEach
    void setUp() {
        // Initialisation des objets de test (à adapter selon vos constructeurs d'Action)
        actionApple = new ActionSimple("Apple");
        actionGoogle = new ActionSimple("Google");
        portefeuille = new Portefeuille("Mon Portefeuille Test", "actions", new HashMap<>());
    }

    // --- TESTS CONSTRUCTEUR ET ACCESSEURS ---

    @Test
    void testConstructeurEtGetters() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(actionApple, 5);

        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", map);

        assertEquals("MonPortefeuille", p.getNom());
        assertEquals("actions", p.getType());
        assertEquals(map, p.getMapActions());
    }

    @Test
    void testSetType() {
        portefeuille.setType("crypto");
        assertEquals("crypto", portefeuille.getType());
    }

    @Test
    void testSetMapActions() {
        Map<Action, Integer> newMap = new HashMap<>();
        newMap.put(actionApple, 10);
        portefeuille.setMapActions(newMap);
        assertEquals(newMap, portefeuille.getMapActions());
    }

    // --- TESTS LOGIQUE D'ACTION (Contient / Acheter) ---

    @Test
    void testContientActionPresente() {
        portefeuille.acheterAction(actionApple, 2);
        assertTrue(portefeuille.contientAction(actionApple));
    }

    @Test
    void testContientActionAbsente() {
        portefeuille.acheterAction(actionApple, 2);
        assertFalse(portefeuille.contientAction(actionGoogle));
    }

    @Test
    void testContientActionPortefeuilleVide() {
        assertFalse(portefeuille.contientAction(actionApple));
    }

    @Test
    void testAcheterNouvelleAction() {
        portefeuille.acheterAction(actionApple, 10);
        assertTrue(portefeuille.getMapActions().containsKey(actionApple));
        assertEquals(10, portefeuille.getMapActions().get(actionApple));
    }

    @Test
    void testAcheterActionExistante() {
        portefeuille.acheterAction(actionApple, 10);
        portefeuille.acheterAction(actionApple, 5);
        assertEquals(15, portefeuille.getMapActions().get(actionApple));
    }

    @Test
    void testAcheterActionQuantiteInvalide() {
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.acheterAction(actionApple, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.acheterAction(actionApple, -5);
        });
    }

    @Test
    void testAcheterActionActionNulle() {
        // Initialisation du portefeuille


        // On vérifie que l'appel avec 'null' lève bien une IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.acheterAction(null, 10);
        });

        // Vérification que le message d'erreur est exactement celui attendu
        assertEquals("L'action à acheter ne peut pas être nulle.", exception.getMessage());
    }



    // --- TESTS D'AFFICHAGE ET SORTIE ---
    @Test
    void testAfficherPourcentagePortefeuilleValeurZero() {
        // Initialisation du portefeuille (supposé vide au départ)

        Jour jourJ = new Jour(2026, 3, 30);

        /* * CAS 1 : Portefeuille vide.
         * La boucle ne s'exécute pas, valeurTotale reste à 0.
         */
        Map<Action, Double> resultVide = portefeuille.afficherPourcentagePortefeuille(jourJ);

        // Vérification : La map doit être vide
        assertTrue(resultVide.isEmpty(), "Le résultat doit être une map vide si le portefeuille est vide.");
        assertEquals(0, resultVide.size(), "La taille doit être 0.");

        /* * CAS 2 : Portefeuille avec une action dont la valeur est 0.
         * On ajoute une action mais on s'assure que son prix ce jour-là est 0.
         */
        ActionSimple actionGratuite = new ActionSimple("ActionGratuite");
        actionGratuite.enregistrerCours(jourJ, 0f); // Prix à 0
        portefeuille.acheterAction(actionGratuite, 10); // Quantité 10, mais prix 0 -> Total 0

        Map<Action, Double> resultValeurNulle = portefeuille.afficherPourcentagePortefeuille(jourJ);

        // Vérification : Même avec une action, si le total est 0, on doit retourner une map vide
        assertTrue(resultValeurNulle.isEmpty(), "Le résultat doit être vide si la valeur totale du portefeuille est 0.");
    }


    @Test
    void testAfficherPortefeuilleVide() {
        String result = portefeuille.afficher();
        assertTrue(result.contains("vide"));
    }

    @Test
    void testAfficherPortefeuilleNonVide() {
        portefeuille.acheterAction(actionApple, 3);
        String result = portefeuille.afficher();
        assertTrue(result.contains("actions"));
    }

    @Test
    void testAfficherDetailsPortefeuille() {
        // Capture de la sortie console
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        try {
            portefeuille.acheterAction(actionApple, 10);
            Jour aujourdHui = new Jour(2024, 1,10);
            portefeuille.afficherDetailsPortefeuille(aujourdHui);

            String sortieConsole = outputStreamCaptor.toString();

            assertTrue(sortieConsole.contains("Mon Portefeuille Test"));
            assertTrue(sortieConsole.contains("Apple"));
            assertTrue(sortieConsole.contains("10"));
            assertTrue(sortieConsole.contains("VALEUR TOTALE"));
        } finally {
            // Toujours remettre le flux standard
            System.setOut(originalOut);
        }
    }

    @Test
    void testVendreActionsWithSufficientQuantity() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertDoesNotThrow(() -> portefeuille.acheterAction(actionMSFT, 10));
        assertDoesNotThrow(() -> portefeuille.vendreAction(actionMSFT, 5));
        assertEquals(5, portefeuille.getMapActions().get(actionMSFT));
    }

    @Test
    void testVendreActionsWithAllQuantity() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertDoesNotThrow(() -> portefeuille.acheterAction(actionMSFT, 10));
        assertDoesNotThrow(() -> portefeuille.vendreAction(actionMSFT, 10));
        assertFalse(portefeuille.getMapActions().containsKey(actionMSFT));
    }

    @Test
    void testVendreActionsWithSuperiorQuantity() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertDoesNotThrow(() -> portefeuille.acheterAction(actionMSFT, 10));
        assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreAction(actionMSFT, 15));
    }

    @Test
    void testVendreActionsWithUnpossessedAction() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreAction(actionMSFT, 15));
    }

    @Test
    void testVendreActionsWithInvalidQuantity() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertDoesNotThrow(() -> portefeuille.acheterAction(actionMSFT, 10));
        assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreAction(actionMSFT, 0));
        assertThrows(IllegalArgumentException.class, () -> portefeuille.vendreAction(actionMSFT, -5));
    }

    @Test
    void testVendreActionsWithSufficientQuantityReturnsTrue() {
        Action actionMSFT = new ActionSimple("Microsoft");
        assertDoesNotThrow(() -> portefeuille.acheterAction(actionMSFT, 10));
        assertTrue(portefeuille.vendreAction(actionMSFT, 5));
        assertEquals(5, portefeuille.getMapActions().get(actionMSFT));
    }

    @Test
    void afficherPourcentagePortefeuilleWithFiveActions() {
        ActionSimple actionA = new ActionSimple("ActionA");
        ActionSimple actionB = new ActionSimple("ActionB");
        ActionSimple actionC = new ActionSimple("ActionC");
        ActionSimple actionD = new ActionSimple("ActionD");
        ActionSimple actionE = new ActionSimple("ActionE");
        
        Administrateur administrateur = new Administrateur(null, null);

        portefeuille.acheterAction(actionA, 10);
        portefeuille.acheterAction(actionB, 20);
        portefeuille.acheterAction(actionC, 30);
        portefeuille.acheterAction(actionD, 40);
        portefeuille.acheterAction(actionE, 50);

        Jour jour = new Jour(2024, 1, 10);
        administrateur.updateActionSimpleCours(actionA, jour, 1);
        administrateur.updateActionSimpleCours(actionB, jour, 2);
        administrateur.updateActionSimpleCours(actionC, jour, 3);
        administrateur.updateActionSimpleCours(actionD, jour, 4);
        administrateur.updateActionSimpleCours(actionE, jour, 5);


        Map<Action, Double> pourcentages = portefeuille.afficherPourcentagePortefeuille(jour);

        assertEquals(5, pourcentages.size());
        assertTrue(pourcentages.containsKey(actionA));
        assertTrue(pourcentages.containsKey(actionB));
        assertTrue(pourcentages.containsKey(actionC));
        assertTrue(pourcentages.containsKey(actionD));
        assertTrue(pourcentages.containsKey(actionE));
    }

    @Test
    void testAcheterActionWithCorrectInitialActions() {
        ActionSimple actionA = new ActionSimple("ActionA");
        ActionSimple actionB = new ActionSimple("ActionB");
        ActionSimple actionC = new ActionSimple("ActionC");

        LocalDate currentDate = LocalDate.now();
        Jour jour = new Jour(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        Administrateur administrateur = new Administrateur(null, null);
        administrateur.updateActionSimpleCours(actionA, jour, 10);
        administrateur.updateActionSimpleCours(actionB, jour, 20);
        administrateur.updateActionSimpleCours(actionC, jour, 30);

        portefeuille.acheterAction(actionA, 2);
        portefeuille.acheterAction(actionB, 3);
        portefeuille.acheterAction(actionC, 4);

        assertEquals(20f, portefeuille.getMapActionsInitial().get(actionA));
        assertEquals(60f, portefeuille.getMapActionsInitial().get(actionB));
        assertEquals(120f, portefeuille.getMapActionsInitial().get(actionC));
    }

    @Test
    void testCalculerGainEnCoursPourUneAction() {
        ActionSimple actionA = new ActionSimple("ActionA");
        Administrateur administrateur = new Administrateur(null, null);
        Jour jour = new Jour(2024, 1, 10);

        administrateur.updateActionSimpleCours(actionA, jour, 15);
        portefeuille.acheterAction(actionA, 2);

        Map<Action, Float> mapInitiale = new HashMap<>();
        mapInitiale.put(actionA, 20f);
        portefeuille.setMapActionsInitial(mapInitiale);

        float gain = portefeuille.calculerGainEnCours(actionA, jour);
        assertEquals(10f, gain);
    }

    @Test
    void testCalculerGainTotalEnCours() {
        ActionSimple actionA = new ActionSimple("ActionA");
        ActionSimple actionB = new ActionSimple("ActionB");
        Administrateur administrateur = new Administrateur(null, null);
        Jour jour = new Jour(2024, 1, 10);

        administrateur.updateActionSimpleCours(actionA, jour, 15);
        administrateur.updateActionSimpleCours(actionB, jour, 5);

        portefeuille.acheterAction(actionA, 2);
        portefeuille.acheterAction(actionB, 4);

        Map<Action, Float> mapInitiale = new HashMap<>();
        mapInitiale.put(actionA, 20f);
        mapInitiale.put(actionB, 30f);
        portefeuille.setMapActionsInitial(mapInitiale);

        float gainTotal = portefeuille.calculerGainTotalEnCours(jour);
        assertEquals(0f, gainTotal);
    }

    @Test
    void testCalculerGainEnCoursParAction() {
        ActionSimple actionA = new ActionSimple("ActionA");
        ActionSimple actionB = new ActionSimple("ActionB");
        Administrateur administrateur = new Administrateur(null, null);
        Jour jour = new Jour(2024, 1, 10);

        administrateur.updateActionSimpleCours(actionA, jour, 15);
        administrateur.updateActionSimpleCours(actionB, jour, 5);

        portefeuille.acheterAction(actionA, 2);
        portefeuille.acheterAction(actionB, 4);

        Map<Action, Float> mapInitiale = new HashMap<>();
        mapInitiale.put(actionA, 20f);
        mapInitiale.put(actionB, 30f);
        portefeuille.setMapActionsInitial(mapInitiale);

        Map<Action, Float> gains = portefeuille.calculerGainEnCoursParAction(jour);
        assertEquals(2, gains.size());
        assertEquals(10f, gains.get(actionA));
        assertEquals(-10f, gains.get(actionB));
    }

    @Test
    void testAfficherPourcentagePortefeuilleValeurTotaleZeroRetourneMapVide() {
        ActionSimple actionA = new ActionSimple("ActionA");
        portefeuille.acheterAction(actionA, 3);
        Jour jour = new Jour(2024, 1, 10);

        Map<Action, Double> pourcentages = portefeuille.afficherPourcentagePortefeuille(jour);
        assertTrue(pourcentages.isEmpty());
    }

    @Test
    void testAcheterActionNull() {
        assertThrows(IllegalArgumentException.class, () -> portefeuille.acheterAction(null, 1));
    }

    @Test
    void testCalculerGainEnCoursActionNull() {
        Jour jour = new Jour(2024, 1, 10);
        assertThrows(IllegalArgumentException.class, () -> portefeuille.calculerGainEnCours(null, jour));
    }

    @Test
    void testCalculerGainEnCoursJourNull() {
        ActionSimple actionA = new ActionSimple("ActionA");
        portefeuille.acheterAction(actionA, 1);
        assertThrows(IllegalArgumentException.class, () -> portefeuille.calculerGainEnCours(actionA, null));
    }

    @Test
    void testCalculerGainEnCoursActionAbsenteDuPortefeuille() {
        ActionSimple actionA = new ActionSimple("ActionA");
        Jour jour = new Jour(2024, 1, 10);
        assertThrows(IllegalArgumentException.class, () -> portefeuille.calculerGainEnCours(actionA, jour));
    }

    @Test
    void testCalculerGainTotalEnCoursJourNull() {
        assertThrows(IllegalArgumentException.class, () -> portefeuille.calculerGainTotalEnCours(null));
    }

    @Test
    void testCalculerGainEnCoursParActionJourNull() {
        assertThrows(IllegalArgumentException.class, () -> portefeuille.calculerGainEnCoursParAction(null));
    }
}