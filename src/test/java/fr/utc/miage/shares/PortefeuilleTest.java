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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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
        Logger logger = Logger.getLogger(Portefeuille.class.getName());
        StringBuilder logsCaptor = new StringBuilder();
        Handler testHandler = new Handler() {
            @Override
            public void publish(LogRecord recordT) {
                if (recordT.getParameters() != null) {
                    logsCaptor.append(MessageFormat.format(recordT.getMessage(), recordT.getParameters())).append("\n");
                } else {
                    logsCaptor.append(recordT.getMessage()).append("\n");
                }
            }

            @Override
            public void flush() {
                // No-op for in-memory capture
            }

            @Override
            public void close() {
                // No-op for in-memory capture
            }
        };

        boolean originalUseParentHandlers = logger.getUseParentHandlers();
        Level originalLevel = logger.getLevel();
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
        logger.addHandler(testHandler);

        try {
            portefeuille.acheterAction(actionApple, 10);
            Jour aujourdHui = new Jour(2024, 1,10);
            portefeuille.afficherDetailsPortefeuille(aujourdHui);

            String sortieLogs = logsCaptor.toString();

            assertTrue(sortieLogs.contains("Mon Portefeuille Test"));
            assertTrue(sortieLogs.contains("Apple"));
            assertTrue(sortieLogs.contains("10"));
            assertTrue(sortieLogs.contains("VALEUR TOTALE"));
        } finally {
            logger.removeHandler(testHandler);
            logger.setUseParentHandlers(originalUseParentHandlers);
            logger.setLevel(originalLevel);
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
    void getActionsSortedByName_portefeuilleVide_retourneListeVide() {
        Portefeuille portefeuilleTest = new Portefeuille("Test", "Standard", new HashMap<>());

        List<Action> result = portefeuilleTest.getActionsSortedByName();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getActionsSortedByName_plusieursActions_retourneListeTriee() {
        Portefeuille portefeuilleTest = new Portefeuille("Test", "Standard", new HashMap<>());
        ActionSimple actionC = new ActionSimple("Google");
        ActionSimple actionA = new ActionSimple("Apple");
        ActionSimple actionB = new ActionSimple("Facebook");
        portefeuilleTest.acheterAction(actionC, 1);
        portefeuilleTest.acheterAction(actionA, 1);
        portefeuilleTest.acheterAction(actionB, 1);

        List<Action> result = portefeuilleTest.getActionsSortedByName();

        assertEquals("Apple", result.get(0).getLibelle());
        assertEquals("Facebook", result.get(1).getLibelle());
        assertEquals("Google", result.get(2).getLibelle());
    }

    @Test
    void getActionsSortedByName_uneAction_retourneListeUneAction() {
        Portefeuille portefeuilleTest = new Portefeuille("Test", "Standard", new HashMap<>());
        ActionSimple action = new ActionSimple("Apple");
        portefeuilleTest.acheterAction(action, 3);

        List<Action> result = portefeuilleTest.getActionsSortedByName();

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getLibelle());
    }

    @Test
    void testCalculerValeurTotale() {
        Jour j = new Jour(2026, 3, 30);

        ActionSimple a1 = new ActionSimple("Action A");
        a1.enregistrerCours(j, 100f);

        ActionSimple a2 = new ActionSimple("Action B");
        a2.enregistrerCours(j, 50f);

        /* Achat d'actions : 10*100 + 20*50 = 1000 + 1000 = 2000 */
        portefeuille.acheterAction(a1, 10);
        portefeuille.acheterAction(a2, 20);


        assertEquals(2000f, portefeuille.calculerValeurTotale(j), "Le capital total calculé est incorrect.");
    }

    @Test
    void testCalculerValeurTotaleAvecJourNul() {
        assertThrows(IllegalArgumentException.class, () -> {
            portefeuille.calculerValeurTotale(null);
        }, "Une exception devrait être levée si le jour est nul.");
    }
}