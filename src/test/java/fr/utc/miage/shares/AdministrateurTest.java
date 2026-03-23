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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AdministrateurTest {

    @Test
    void testGetName() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        assertEquals("Doe", administrateur.getName());
    }

    @Test
    void testGetFirstName() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        assertEquals("John", administrateur.getFirstName());
    }

    @Test
    void testSetName() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        administrateur.setName("Smith");
        assertEquals("Smith", administrateur.getName());
    }

    @Test
    void testSetFirstName() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        administrateur.setFirstName("Jane");
        assertEquals("Jane", administrateur.getFirstName());
    }

    @Test
    void testCatalogueVideAuDepart() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        assertTrue(administrateur.getCatalogue().isEmpty());
    }

    @Test
    void testPublierActionSimple() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierActionSimple(action);
        assertTrue(administrateur.getCatalogue().contains(action));
    }

    @Test
    void testPublierActionSimpleDejaExistante() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        ActionSimple action = new ActionSimple("Action France TV");
        administrateur.publierActionSimple(action);
        administrateur.publierActionSimple(action);
        assertEquals(1, administrateur.getCatalogue().size());
    }

    @Test
    void testPublierActionCompose() {
        Administrateur administrateur = new Administrateur("Doe", "John", null, null);
        ActionCompose actionCompose = new ActionCompose("Action Composée");
        administrateur.publierActionCompose(actionCompose);
        assertTrue(administrateur.getCatalogue().contains(actionCompose));
    }
}