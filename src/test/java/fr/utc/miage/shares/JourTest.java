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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe Jour (version LocalDate).
 */
class JourTest {

    private static final int DEFAULT_YEAR = 2026;
    private static final int DEFAULT_MONTH = 3;
    private static final int DEFAULT_DAY = 30;

    @Test
    void testAllConstructorUsage() {
        Assertions.assertAll("Groupe de tests sur le constructeur",
                // Test valide
                () -> assertDoesNotThrow(() -> new Jour(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY)),

                // Test mois invalide (ex: mois 13)
                () -> assertThrows(DateTimeException.class, () -> {
                    new Jour(DEFAULT_YEAR, 13, DEFAULT_DAY);
                }, "Le mois doit être compris entre 1 et 12"),

                // Test jour invalide (ex: 31 février)
                () -> assertThrows(DateTimeException.class, () -> {
                    new Jour(2026, 2, 30);
                }, "Le 30 février n'existe pas")
        );
    }

    @Test
    void testAccessorsShouldWork() {
        final Jour jour = getDefaultJour();

        Assertions.assertAll("Vérification des accesseurs",
                () -> assertEquals(DEFAULT_YEAR, jour.getYear(), "L'année est incorrecte"),
                () -> assertEquals(DEFAULT_MONTH, jour.getMonth(), "Le mois est incorrect"),
                () -> assertEquals(DEFAULT_DAY, jour.getDayOfMonth(), "Le jour du mois est incorrect")
        );
    }

    @Test
    void testEqualsAndHashCode() {
        final Jour jour1 = getDefaultJour();
        final Jour jour2 = new Jour(2026, 3, 30);
        final Jour jourDiff = new Jour(2026, 3, 31);

        Assertions.assertAll("Tests d'égalité et de hashcode",
                () -> assertEquals(jour1, jour1, "Un objet doit être égal à lui-même"),
                () -> assertEquals(jour1, jour2, "Deux objets avec la même date doivent être égaux"),
                () -> assertNotEquals(jour1, jourDiff, "Deux dates différentes ne doivent pas être égales"),
                () -> assertNotEquals(jour1, null, "Un objet ne peut pas être égal à null"),
                () -> assertEquals(jour1.hashCode(), jour2.hashCode(), "Le hashcode doit être identique pour deux dates égales")
        );
    }

    @Test
    void testComparable() {
        final Jour jour1 = new Jour(2026, 1, 1);
        final Jour jour2 = new Jour(2026, 1, 2);

        assertTrue(jour1.compareTo(jour2) < 0, "Le 1er janvier doit être avant le 2 janvier");
        assertTrue(jour2.compareTo(jour1) > 0, "Le 2 janvier doit être après le 1er janvier");
        assertEquals(0, jour1.compareTo(new Jour(2026, 1, 1)), "La comparaison de dates identiques doit retourner 0");
    }

    @Test
    void testToStringFormat() {
        final Jour jour = new Jour(2026, 3, 30);
        // Le format attendu selon DateTimeFormatter.ofPattern("dd/MM/yyyy")
        final String expected = "30/03/2026";

        assertEquals(expected, jour.toString(), "Le format toString doit être dd/MM/yyyy");
    }

    /**
     * Crée un objet Jour par défaut pour les tests.
     */
    private Jour getDefaultJour() {
        return new Jour(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY);
    }
}