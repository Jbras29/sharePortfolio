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

    // --- TESTS D'AFFICHAGE ET SORTIE ---

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
}