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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;




class ActionSimpleTest {

    private static final String LIBELLE = "AXA";
    private static final float PRIX_VALIDE = 150.5f;
    private static final float PRIX_NEGATIF = -10.0f;

    @Test
    void testEnregistrerEtLireCours() {
        // Initialisation de l'action
        ActionSimple action = new ActionSimple(LIBELLE);
        Jour jour = new Jour(2026, 3, 30);

        // 1. Test de l'enregistrement (enregistrerCours)
        /* On vérifie que l'enregistrement ne lève pas d'exception */
        assertDoesNotThrow(() -> action.enregistrerCours(jour, PRIX_VALIDE));

        // 2. Test de la lecture (valeur)
        /* On vérifie que la valeur récupérée est bien celle enregistrée */
        assertEquals(PRIX_VALIDE, action.valeur(jour), "La valeur lue doit être égale à la valeur enregistrée.");
    }

    @Test
    void testEnregistrerCoursNegatifDoitLeverException() {
        ActionSimple action = new ActionSimple(LIBELLE);
        Jour jour = new Jour(2026, 3, 30);

        /* On vérifie qu'un prix négatif déclenche une IllegalArgumentException */
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            action.enregistrerCours(jour, PRIX_NEGATIF);
        });

        assertEquals("Le cours ne peut pas être négatif.", exception.getMessage());
    }

    @Test
    void testValeurParDefaut() {
        ActionSimple action = new ActionSimple(LIBELLE);
        Jour jourSansCours = new Jour(2025, 1, 1);

        // Test de la branche 'getOrDefault'
        /* Si aucun cours n'est enregistré pour ce jour, on doit obtenir la valeur par défaut (0.0f) */
        assertEquals(0f, action.valeur(jourSansCours),
                "La valeur par défaut (0.0f) doit être retournée si le jour n'existe pas.");
    }

    @Test
    void testCompatibiliteEnrgCours() {
        ActionSimple action = new ActionSimple(LIBELLE);
        Jour jour = new Jour(2026, 3, 30);

        // Test de la méthode alternative (enrgCours)
        /* On vérifie que cette méthode fonctionne comme un alias de enregistrerCours */
        action.enrgCours(jour, 200f);
        assertEquals(200f, action.valeur(jour), "La méthode enrgCours doit également enregistrer le prix.");
    }


    @Test
    void testGetHistoriqueCours() {
        ActionSimple axa = new ActionSimple("AXA");
        Jour j1 = new Jour(2026, 3, 1);
        Jour j2 = new Jour(2026, 3, 15);
        Jour j3 = new Jour(2026, 3, 30);

        axa.enregistrerCours(j1, 100f);
        axa.enregistrerCours(j2, 120f);
        axa.enregistrerCours(j3, 110f);

        /* On demande l'historique du 10 au 31 mars */
        Jour debutRecherche = new Jour(2026, 3, 10);
        Jour finRecherche = new Jour(2026, 3, 31);

        Map<Jour, Float> result = axa.getHistoriqueCours(debutRecherche, finRecherche);

        /* Vérifications */
        assertEquals(2, result.size(), "Il devrait y avoir 2 points dans l'historique.");
        assertTrue(result.containsKey(j2), "Le cours du 15 mars doit être présent.");
        assertTrue(result.containsKey(j3), "Le cours du 30 mars doit être présent.");
        assertFalse(result.containsKey(j1), "Le cours du 1er mars ne doit pas être présent.");
    }

    @Test
    void testAfficherAnalyseCourbeExecution() {
        ActionSimple action = new ActionSimple("AXA");
        Jour j1 = new Jour(2026, 3, 1);
        Jour j2 = new Jour(2026, 3, 2);

        action.enregistrerCours(j1, 100f);
        action.enregistrerCours(j2, 120f);

        /* On vérifie que la méthode s'exécute sans erreur (pas d'exception) */
        assertDoesNotThrow(() -> {
            action.afficherAnalyseCourbe(j1, j2);
        }, "La méthode d'affichage ne doit pas lever d'exception");
    }

    @Test
    void testEqualsWithSameReferenceShouldReturnTrue() {
        ActionSimple action = new ActionSimple(LIBELLE);

        assertTrue(action.equals(action));
    }

    @Test
    void testEqualsWithNullShouldReturnFalse() {
        ActionSimple action = new ActionSimple(LIBELLE);

        assertFalse(action.equals(null));
    }

    @Test
    void testEqualsWithDifferentTypeShouldReturnFalse() {
        ActionSimple actionSimple = new ActionSimple(LIBELLE);
        ActionCompose actionCompose = new ActionCompose(LIBELLE);

        assertFalse(actionSimple.equals(actionCompose));
    }

    @Test
    void testEqualsWithSameLibelleAndSameCoursShouldReturnTrue() {
        ActionSimple action1 = new ActionSimple(LIBELLE);
        ActionSimple action2 = new ActionSimple(LIBELLE);
        Jour jour = new Jour(2026, 3, 30);

        action1.enregistrerCours(jour, 100f);
        action2.enregistrerCours(jour, 100f);

        assertTrue(action1.equals(action2));
        assertTrue(action2.equals(action1));
    }

    @Test
    void testEqualsWithDifferentLibelleShouldReturnFalse() {
        ActionSimple action1 = new ActionSimple("AXA");
        ActionSimple action2 = new ActionSimple("TOTAL");

        assertFalse(action1.equals(action2));
    }

    @Test
    void testEqualsWithDifferentCoursShouldReturnFalse() {
        ActionSimple action1 = new ActionSimple(LIBELLE);
        ActionSimple action2 = new ActionSimple(LIBELLE);
        Jour jour = new Jour(2026, 3, 30);

        action1.enregistrerCours(jour, 100f);
        action2.enregistrerCours(jour, 120f);

        assertFalse(action1.equals(action2));
    }
}
