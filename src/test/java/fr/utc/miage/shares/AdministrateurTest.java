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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    void testPublierActionComposeDejaExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.publierAction(actionCompose);
        administrateur.publierAction(actionCompose);
        assertEquals(1, administrateur.getCatalogue().size());
    }

    @Test
    void testAjouterProucentageActionComposée() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f);
        assertTrue(actionCompose.getActions().containsKey(actionSimple));
        assertEquals(0.5f, actionCompose.getActions().get(actionSimple));
    }

    @Test
    void testAjouterPourcentageActionComposéeAvecProportionInvalide() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        assertThrows(IllegalArgumentException.class, () -> administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 1.5f));
    }

    @Test
    void testAjouterPourcentageActionComposéeAvecActionDejaExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f);
        assertThrows(IllegalArgumentException.class, () -> administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f));
    }

    @Test
    void testMettreAJourPourcentageActionComposée() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f);
        administrateur.updatePourcentageActionComposee(actionCompose, actionSimple, 0.7f);
        assertEquals(0.7f, actionCompose.getActions().get(actionSimple));
    }

    @Test
    void testMettreAJourPourcentageActionComposéeAvecProportionInvalidee() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f);
        assertThrows(IllegalArgumentException.class, () -> administrateur.updatePourcentageActionComposee(actionCompose, actionSimple, 1.5f));
    }

    @Test
    void testMettreAJourPourcentageActionComposéeAvecActionNonExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        assertThrows(IllegalArgumentException.class, () -> administrateur.updatePourcentageActionComposee(actionCompose, actionSimple, 0.5f));
    }

    @Test
    void testSupprimerPourcentageActionCompose() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.ajouterPourcentageActionComposee(actionCompose, actionSimple, 0.5f);
        administrateur.supprimerActionComposee(actionCompose, actionSimple);
        assertFalse(actionCompose.getActions().containsKey(actionSimple));
    }  

    @Test
    void testSupprimerPourcentageActionComposeAvecActionNonExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple actionSimple = new ActionSimple("Action Simple");
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        assertThrows(IllegalArgumentException.class, () -> administrateur.supprimerActionComposee(actionCompose, actionSimple));
    }  
    void testSupprimerAction() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierAction(action);
        administrateur.supprimerAction(action);
        assertFalse(administrateur.getCatalogue().contains(action));
    }

    @Test
    void testAnnulerSuppressionAction() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierAction(action);
        administrateur.supprimerAction(action);
        assertFalse(administrateur.getCatalogue().contains(action));
        administrateur.publierAction(action);
        assertTrue(administrateur.getCatalogue().contains(action));
    }

    @Test
    void testSupprimerActionInexistante() {
        Administrateur administrateur = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("Action France TV");
        assertThrows(IllegalArgumentException.class, () -> administrateur.supprimerAction(action));
    }

    @Test
    void testUpdateActionSimpleCours() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action = new ActionSimple("BNP");
        Jour jourJ = new Jour(2026,  3,30);

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

    @Test
    public void testRetrieveActionsWithNoCoursCurrentDate_IncludesActionsWithoutCourse() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action1 = new ActionSimple("Action1");
        ActionSimple action2 = new ActionSimple("Action2");

        // Publier les actions
        admin.publierAction(action1);
        admin.publierAction(action2);

        // Récupérer les actions sans cours pour la date actuelle
        List<Action> actionsSansCours = admin.retrieveActionsWithNoCoursCurrentDate();

        // Vérifier que action1 et action2 sont dans la liste
        assertTrue(actionsSansCours.contains(action1), "Action1 devrait être dans la liste des actions sans cours");
        assertTrue(actionsSansCours.contains(action2), "Action2 devrait être dans la liste des actions sans cours");
    }

    @Test
    public void testRetrieveActionsWithNoCoursCurrentDate_IncludesActionWithoutCourseWhenOtherHasCourse() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action1 = new ActionSimple("Action1");
        ActionSimple action2 = new ActionSimple("Action2");

        // Publier les actions
        admin.publierAction(action1);
        admin.publierAction(action2);

        // Mettre à jour le cours de action1 pour la date actuelle
        LocalDate currentDate = LocalDate.now();
        Jour today = new Jour(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        admin.updateActionSimpleCours(action1, today, 100.0f);

        // Récupérer les actions sans cours pour la date actuelle
        List<Action> actionsSansCours = admin.retrieveActionsWithNoCoursCurrentDate();

        // Vérifier que action2 est dans la liste
        assertTrue(actionsSansCours.contains(action2), "Action2 devrait être dans la liste des actions sans cours");
    }

    @Test
    public void testRetrieveActionsWithNoCoursCurrentDate_DoesNotIncludeActionWithCourse() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");
        ActionSimple action1 = new ActionSimple("Action1");

        // Publier l'action
        admin.publierAction(action1);

        // Mettre à jour le cours de action1 pour la date actuelle
        LocalDate currentDate = LocalDate.now();
        Jour today = new Jour(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        admin.updateActionSimpleCours(action1, today, 100.0f);

        // Récupérer les actions sans cours pour la date actuelle
        List<Action> actionsSansCours = admin.retrieveActionsWithNoCoursCurrentDate();

        // Vérifier que action1 n'est pas dans la liste
        assertFalse(actionsSansCours.contains(action1), "Action1 ne devrait pas être dans la liste car elle a un cours");
    }

    @Test
    public void testRetrieveActionsWithNoCoursCurrentDate_WithEmptyCatalogue() {
        // Initialisation locale
        Administrateur admin = new Administrateur("Doe", "John");

        // Récupérer les actions sans cours pour la date actuelle
        List<Action> actionsSansCours = admin.retrieveActionsWithNoCoursCurrentDate();

        // Vérifier que la liste est vide
        assertTrue(actionsSansCours.isEmpty(), "La liste devrait être vide quand aucune action n'est publiée");
    }
}