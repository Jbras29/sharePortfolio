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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UserTest {

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


    @Test
    void valeurPortefeuille_portefeuilleVide_retourneZero() {
        User user = new User("Doe", "John");
        Jour jour = new Jour(2025, 1, 1);

        float result = user.valeurPortefeuille(jour);

        assertEquals(0f, result);
    }

    @Test
    void valeurPortefeuille_uneAction_retourneValeurCorrecte() {
        User user = new User("Doe", "John");
        ActionSimple action = new ActionSimple("Apple");
        Jour jour = new Jour( 2025,1, 1);
        action.enrgCours(jour, 100f);
        user.getPortefeuille().acheterAction(action, 3);

        float result = user.valeurPortefeuille(jour);

        assertEquals(300f, result);
    }

    @Test
    void valeurPortefeuille_plusieursActions_retourneSommeCorrecte() {
        User user = new User("Doe", "John");
        ActionSimple action1 = new ActionSimple("Apple");
        ActionSimple action2 = new ActionSimple("Google");
        Jour jour = new Jour( 2025,1, 1);
        action1.enrgCours(jour, 100f);
        action2.enrgCours(jour, 50f);
        user.getPortefeuille().acheterAction(action1, 2);
        user.getPortefeuille().acheterAction(action2, 4);

        float result = user.valeurPortefeuille(jour);

        assertEquals(400f, result); // (100*2) + (50*4)
    }

    @Test
    void testGetFavoris_listeVideParDefaut() {
        User user = new User("Doe", "John");
        assertNotNull(user.getFavoris());
        assertTrue(user.getFavoris().isEmpty());
    }

    @Test
    void ajouterFavori_actionValide_retourneTrue() {
        User user = new User("Doe", "John");
        ActionSimple action = new ActionSimple("Apple");

        boolean result = user.ajouterFavori(action);

        assertTrue(result);
        assertEquals(1, user.getFavoris().size());
        assertTrue(user.getFavoris().contains(action));
    }

    @Test
    void ajouterFavori_actionNull_leveException() {
        User user = new User("Doe", "John");

        assertThrows(IllegalArgumentException.class, () -> user.ajouterFavori(null));
    }

    // Test ComparaisonPortefeuille

    @Test
    void testComparerValeurPortefeuille_Augmentation() {
        User user = new User("Dupont", "Jean");

        ActionSimple apple = new ActionSimple("Apple");
        Jour dateDebut = new Jour(2025, 1, 1);
        Jour dateFin = new Jour(2025, 1, 2);

        apple.enregistrerCours(dateDebut, 100.0f);
        apple.enregistrerCours(dateFin, 150.0f);

        user.getPortefeuille().getMapActions().put(apple, 2);

        float resultat = user.comparerValeurPortefeuille(dateDebut, dateFin);

        assertEquals(100.0f, resultat);
    }

    @Test
    void testComparerValeurPortefeuille_DateDebutNull() {
        User user = new User("Dupont", "Jean");
        Jour dateFin = new Jour(2025, 1, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            user.comparerValeurPortefeuille(null, dateFin);
        });
    }

    @Test
    void testComparerValeurPortefeuille_DateFinNull() {
        User user = new User("Dupont", "Jean");
        Jour dateDebut = new Jour(2025, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            user.comparerValeurPortefeuille(dateDebut, null);
        });
    }
    }