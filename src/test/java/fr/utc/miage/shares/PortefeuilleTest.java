/*
 * Copyright 2025 David Navarre &lt;David.Navarre at irit.fr&gt;.
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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PortefeuilleTest {

    private Action action1;
    private Action action2;



    @Test
    void testConstructeurEtGetters() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 5);

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
        newMap.put(action1, 10);
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
        map.put(action1, 3);
        Portefeuille p = new Portefeuille("MonPortefeuille", "actions", map);
        String result = p.afficher();
        assertTrue(result.contains("actions"));
    }


    @Test
    void testContientActionPresente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        assertTrue(p.contientAction(action1));
    }

    @Test
    void testContientActionAbsente() {
        Map<Action, Integer> map = new HashMap<>();
        map.put(action1, 2);
        Portefeuille p = new Portefeuille("P1", "actions", map);
        assertTrue(p.contientAction(action2));
    }

    @Test
    void testContientActionPortefeuilleVide() {
        Portefeuille p = new Portefeuille("P1", "actions", new HashMap<>());
        assertFalse(p.contientAction(action1));
    }
}