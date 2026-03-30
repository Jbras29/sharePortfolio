/*
 * Copyright 2025 David Navarre <David.Navarre at irit.fr>.
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testGetFirstName() {
        User user = new User("Doe", "John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testGetName() {
        User user = new User("Doe", "John");
        assertEquals("Doe", user.getName());
    }

    @Test
    void testGetPortefeuille() {
        User user = new User("Doe", "John");
        assertEquals("Doe John", user.getPortefeuille().getNom());
        assertEquals("Standard", user.getPortefeuille().getType());
        assertEquals(Map.of(), user.getPortefeuille().getMapActions());
    }

    @Test
    void testSetFirstName() {
        User user = new User("Doe", "John");
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void testSetName() {
        User user = new User("Doe", "John");
        user.setName("Smith");
        assertEquals("Smith", user.getName());
    }

    @Test
    void testSetPortefeuille() {
        User user = new User("Doe", "John");
        Portefeuille portefeuille = new Portefeuille("Doe John", "Standard", new HashMap<>());
        user.setPortefeuille(portefeuille);
        assertEquals(portefeuille, user.getPortefeuille());
    }

    @Test
    void testGetInfoAction() {
        User user = new User("Doe", "John");
        Action action = new ActionSimple("Action France TV");
        ActionDTO actionDTO = user.getInfoAction(action);
        assertEquals("Action France TV", actionDTO.libelle());
        assertEquals(TypeAction.SIMPLE, actionDTO.typeAction());
    }
}
    void quantitePossede_portefeuilleVide_retourneMapVide() {
        User user = new User("Doe", "John");
        Map<Action, Integer> result = user.quantitePossede();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void quantitePossede_uneAction_retourneQuantiteCorrecte() {
        User user = new User("Doe", "John");
        ActionSimple action = new ActionSimple("Apple");
        user.getPortefeuille().acheterAction(action, 5);

        Map<Action, Integer> result = user.quantitePossede();

        assertEquals(1, result.size());
        assertEquals(5, result.get(action));
    }

    @Test
    void testQuantitePossede() {
        User user = new User("Sub", "Lilian");
        ActionSimple action = new ActionSimple("Apple");
        Map<Action, Integer> actions = new HashMap<>();
        actions.put(action, 5);
        Portefeuille portefeuille = new Portefeuille("Sub Lilian", "Standard", actions);
        user.setPortefeuille(portefeuille); 
        assertEquals(actions, user.quantitePossede());
    }
}
