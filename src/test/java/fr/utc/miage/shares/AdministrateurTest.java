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
}