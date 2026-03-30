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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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


    // Test pour RechercheActionParNom
    @Test
    public void testRechercherActionParNom_ActionSimpleExistante() {
        User user = new User("Dupont", "Jean");
        Administrateur admin = new Administrateur("Navarre", "David");

        admin.publierAction(new ActionSimple("Apple"));

        Optional<Action> resultat = user.rechercherActionParNom(admin.getCatalogue(), "Apple");

        assertTrue(resultat.isPresent());
        assertEquals("Apple", resultat.get().getLibelle());
        assertTrue(resultat.get() instanceof ActionSimple);
    }

    @Test
    public void testRechercherActionParNom_ActionComposeeExistante() {
        User user = new User("Dupont", "Jean");
        Administrateur admin = new Administrateur("Navarre", "David");

        admin.publierAction(new ActionCompose("Tech"));

        Optional<Action> resultat = user.rechercherActionParNom(admin.getCatalogue(), "Tech");

        assertTrue(resultat.isPresent());
        assertEquals("Tech", resultat.get().getLibelle());
        assertTrue(resultat.get() instanceof ActionCompose);
    }

    @Test
    public void testRechercherActionParNom_ActionInexistante() {
        User user = new User("Dupont", "Jean");
        Administrateur admin = new Administrateur("Navarre", "David");

        admin.publierAction(new ActionSimple("Apple"));

        Optional<Action> resultat = user.rechercherActionParNom(admin.getCatalogue(), "Tesla");

        assertTrue(resultat.isEmpty());
    }

    @Test
    public void testRechercherActionParNom_NomNull() {
        User user = new User("Dupont", "Jean");
        Administrateur admin = new Administrateur("Navarre", "David");

        admin.publierAction(new ActionSimple("Apple"));

        Optional<Action> resultat = user.rechercherActionParNom(admin.getCatalogue(), null);

        assertTrue(resultat.isEmpty());
    }

    @Test
    public void testRechercherActionParNom_CatalogueNull() {
        User user = new User("Dupont", "Jean");

        Optional<Action> resultat = user.rechercherActionParNom(null, "Apple");

        assertTrue(resultat.isEmpty());
    }

    @Test
    public void testRechercherActionParNom_CatalogueVide() {
        User user = new User("Dupont", "Jean");

        Optional<Action> resultat = user.rechercherActionParNom(new ArrayList<>(), "Apple");}

@Test
void getPrixAction_actionEtJourValides_retourneValeurCorrecte() {
    User user = new User("Doe", "John");
    ActionSimple action = new ActionSimple("Apple");
    Jour jour = new Jour(2025, 3,2);
    action.enrgCours(jour,160);

    float result = user.getPrixAction(action, jour);

    assertEquals(160, result);
}

@Test 
void getPrixAction_actionNull(){
    User user= new User("Smith", "John");
    Jour jour = new Jour(2025,4,3);
    assertThrows(IllegalArgumentException.class, ()->user.getPrixAction(null, jour));
}

@Test
void getPrixAction_jourNull_leveException() {
    User user = new User("Doe", "John");
    ActionSimple action = new ActionSimple("Apple");

    assertThrows(IllegalArgumentException.class, () -> user.getPrixAction(action, null));
}

@Test 
void getPrixAction_jourSansCours_retourneZero() {
    User user = new User("Doe", "John");
    ActionSimple action = new ActionSimple("Apple");
    Jour jour = new Jour(2025, 5,17);

    float result = user.getPrixAction(action, jour);

    assertEquals(0, result);
}

@Test 
void getPrixAction_plusieursJours(){
    User user = new User("Smith","Jason");
    ActionSimple action=new ActionSimple("Tesla");
    Jour jour1 = new Jour(2025, 3, 14);
    Jour jour2= new Jour(2025, 7, 23);

    action.enrgCours(jour1, 100);
    action.enrgCours(jour2, 200);

    assertEquals(100,user.getPrixAction(action, jour1));
    assertEquals(200,user.getPrixAction(action, jour2));
}
}
