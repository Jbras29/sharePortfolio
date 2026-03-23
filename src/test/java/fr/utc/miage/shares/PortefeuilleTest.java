/*
 * Copyright 2024 David Navarre &lt;David.Navarre at irit.fr&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utc.miage.shares;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

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
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        p.setType("crypto");
        assertEquals("crypto", p.getType());
    }

    @Test
    void testSetMapActions() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        Map<Action, Integer> newMap = new HashMap<>();
        newMap.put(actionApple, 10);
        p.setMapActions(newMap);
        assertEquals(newMap, p.getMapActions());
    }

    @Test
    void testAfficherPortefeuilleVide() {
        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", new HashMap<>());
        String result = p.afficher();
        assertTrue(result.contains("vide"));
    }

    @Test
    void testAfficherPortefeuilleNonVide() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(actionApple, 3);
        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", map);
        String result = p.afficher();
        assertTrue(result.contains("actions"));
    }

    @Test
    void testContientActionPresente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(actionApple, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        assertTrue(p.contientAction(actionApple));
    }

    @Test
    void testContientActionAbsente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(actionApple, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        // etfTech n'est pas dans la map, donc on attend false
        assertFalse(p.contientAction(etfTech));
    }

    @Test
    void testContientActionPortefeuilleVide() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        assertFalse(p.contientAction(actionApple));
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
    void testAfficherDetailsPortefeuille() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        portefeuille.acheterAction(actionApple, 10);
        Jour aujourdHui = new Jour(2024, 1);
        portefeuille.afficherDetailsPortefeuille(aujourdHui);

        String sortieConsole = outputStreamCaptor.toString();

        assertTrue(sortieConsole.contains("Mon Portefeuille Test"));
        assertTrue(sortieConsole.contains("Apple"));
        assertTrue(sortieConsole.contains("10"));
        assertTrue(sortieConsole.contains("VALEUR TOTALE DU PORTEFEUILLE"));

        System.setOut(System.out);
    }
}