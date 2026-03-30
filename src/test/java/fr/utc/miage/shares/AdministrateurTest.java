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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AdministrateurTest {

    @Test
    void testGetName() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        assertEquals("Doe", administrateur.getName());
    }

    @Test
    void testGetFirstName() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        assertEquals("John", administrateur.getFirstName());
    }

    @Test
    void testSetName() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        administrateur.setName("Smith");
        assertEquals("Smith", administrateur.getName());
    }

    @Test
    void testSetFirstName() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        administrateur.setFirstName("Jane");
        assertEquals("Jane", administrateur.getFirstName());
    }

    @Test
    void testCatalogueVideAuDepart() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        assertTrue(administrateur.getCatalogue().isEmpty());
    }

    @Test
    void testPublierActionSimple() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierAction(action);
        assertTrue(administrateur.getCatalogue().contains(action));
    }

    @Test
    void testPublierActionSimpleDejaExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierAction(action);
        administrateur.publierAction(action);
        assertEquals(1, administrateur.getCatalogue().size());
    }

    @Test
    void testPublierActionCompose() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.publierAction(actionCompose);
        assertTrue(administrateur.getCatalogue().contains(actionCompose));
    }

    @Test
    public void testUpdateActionSimpleCours() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("BNP");
        Jour jourJ = new Jour(2026, 3, 30);

        // 1. Vérification de la valeur initiale (devrait être 0 par défaut)
        assertEquals(0f, action.valeur(jourJ), "La valeur initiale doit être 0");

        // 2. Action de l'administrateur : mise à jour du cours
        float nouveauPrix = 200.0f;
        admin.updateActionSimpleCours(action, jourJ, nouveauPrix);

        // 3. Vérification du changement
        assertEquals(nouveauPrix, action.valeur(jourJ), "Le prix n'a pas été mis à jour correctement");
    }

    /**
     * Test de la modification du libellé et de la cohérence du Set.
     */
    @Test
    public void testUpdateActionLibelle() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("AncienNom");
        Set<Action> catalogue = new HashSet<>();
        catalogue.add(action);

        String nouveauNom = "NouveauNom";

        // 1. Vérifier que l'action est bien dans le catalogue au départ
        assertTrue(catalogue.contains(action), "L'action doit être présente initialement");

        // 2. Action de l'administrateur : changement de nom sécurisé
        admin.updateActionLibelle(catalogue, action, nouveauNom);

        // 3. Vérifier que le libellé de l'objet lui-même a changé
        assertEquals(nouveauNom, action.getLibelle(), "Le libellé aurait dû changer");

        // 4. Vérifier que l'objet est toujours "trouvable" dans le HashSet
        // C'est ici que l'on valide la logique remove -> set -> add
        assertTrue(catalogue.contains(action), "L'action doit rester accessible dans le Set malgré le changement de nom");

        // 5. Vérifier qu'il n'y a pas de doublon (la taille doit rester 1)
        assertEquals(1, catalogue.size(), "Le Set ne doit contenir qu'un seul élément");
    }
        /**
         * Teste la mise à jour du catalogue complet.
         */
        @Test
        void testSetCatalogue () {
            // Khởi tạo admin
            Administrateur admin = new Administrateur("Navarre", "David");

            // Tạo một danh sách mới
            List<Action> nouveauCatalogue = new ArrayList<>();
            nouveauCatalogue.add(new ActionSimple("AXA"));
            nouveauCatalogue.add(new ActionSimple("Orange"));

            // Gán danh sách mới cho admin
            admin.setCatalogue(nouveauCatalogue);

            // Kiểm tra (assertions)
            assertNotNull(admin.getCatalogue(), "Le catalogue ne doit pas être nul.");
            assertEquals(2, admin.getCatalogue().size(), "Le catalogue doit contenir exactement 2 actions.");
            assertEquals(nouveauCatalogue, admin.getCatalogue(), "Le catalogue récupéré doit être identique à celui défini.");
        }

        /**
         * Teste la modification sécurisée du libellé d'une action.
         * Vérifie que l'objet reste accessible dans un HashSet après modification.
         */
    /**
     * Teste le cas "else" : modification du libellé quand l'action n'est pas dans le Set.
     */
    @Test
    void testUpdateActionLibelleLorsquElleNestPasDansLeCatalogue() {

        Administrateur admin = new Administrateur("Navarre", "David");
        ActionSimple action = new ActionSimple("AncienNom");


        Set<Action> catalogueVide = new HashSet<>();

        String nouveauNom = "NouveauNom";


        admin.updateActionLibelle(catalogueVide, action, nouveauNom);


        assertEquals(nouveauNom, action.getLibelle(), "Le libellé doit être mis à jour même si l'action n'est pas dans le Set.");


        assertFalse(catalogueVide.contains(action), "Le catalogue ne doit pas contenir l'action s'il ne la contenait pas au départ.");
        assertEquals(0, catalogueVide.size());
    }
}