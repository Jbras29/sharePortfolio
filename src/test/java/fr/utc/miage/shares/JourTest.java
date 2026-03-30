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

    @Test
    void testEqualsBranches() {
        Jour jour = new Jour(2026, 3, 30);

        // TEST BRANCHE A : Comparaison avec null
        // Cela force (o == null) à être VRAI
        assertNotEquals(null, jour, "Un objet Jour ne peut pas être égal à null");

        // TEST BRANCHE B : Comparaison avec une autre classe
        // Cela force (getClass() != o.getClass()) à être VRAI
        assertNotEquals("Une simple chaîne de caractères", jour, "Un Jour n'est pas une String");

        // TEST BRANCHE C : Comparaison avec la bonne classe (déjà fait normalement)
        // Cela force les deux conditions à être FAUSSES pour continuer
        Jour autreJour = new Jour(2026, 3, 30);
        assertEquals(jour, autreJour);
    }

     /**
     * Teste la récupération du numéro du jour dans l'année.
     */
    @Test
    void testGetDayOfYear() {
        // Cas 1 : Premier jour de l'année (1er janvier)
        Jour premierJanvier = new Jour(2026, 1, 1);
        /* Vérifie que le 1er janvier correspond au jour 1 */
        assertEquals(1, premierJanvier.getDayOfYear(), "Le 1er janvier doit être le jour 1");

        // Cas 2 : Un jour spécifique (30 mars 2026)
        // Calcul : 31 (Jan) + 28 (Fév) + 30 (Mar) = 89
        Jour jourCourant = new Jour(2026, 3, 30);
        /* Vérifie le calcul cumulé des jours pour une date précise */
        assertEquals(89, jourCourant.getDayOfYear(), "Le 30 mars 2026 doit être le jour 89");

        // Cas 3 : Année bissextile (31 décembre 2024)
        // 2024 là năm nhuận, nên ngày cuối cùng phải là 366
        Jour finAnneeBissextile = new Jour(2024, 12, 31);
        /* Vérifie que le calcul prend en compte le jour supplémentaire des années bissextiles */
        assertEquals(366, finAnneeBissextile.getDayOfYear(), "Le 31 décembre 2024 (année bissextile) doit être le jour 366");
    }

    @Test
    void testEqualsCompletPourCouverture() {
        Jour jour = new Jour(2026, 3, 30);

        // 1. Branche "this == o" -> VRAI
        // Force la ligne 74 à retourner true immédiatement.
        assertEquals(jour, jour, "L'objet doit être égal à lui-même (réflexivité)");

        // 2. Branche "this == o" -> FAUX
        // Force le code à passer à la ligne 75.
        Jour autreInstance = new Jour(2026, 3, 30);
        // On utilise assertFalse pour être sûr qu'on ne compare pas les mêmes instances mémoire
        assertNotSame(jour, autreInstance);

        // 3. Branche "o == null" -> VRAI
        // Force la ligne 75 à retourner false à cause du null.
        assertNotEquals(null, jour, "Le test du null doit être couvert");

        // 4. Branche "getClass() != o.getClass()" -> VRAI
        // Force la ligne 75 à retourner false à cause du type différent (ex: String).
        assertNotEquals("Une chaine", jour, "La comparaison avec une autre classe doit être couverte");

        // 5. Branche "o == null || getClass() != o.getClass()" -> FAUX (les deux)
        // Force le code à passer à la ligne 76 et 77 (le cast et la comparaison finale).
        assertEquals(new Jour(2026, 3, 30), jour, "Deux jours identiques doivent passer les tests de structure");

        // 6. Bonus : Test de la valeur finale -> FAUX
        // Pour couvrir le cas où les dates sont différentes à la ligne 77.
        assertNotEquals(new Jour(2025, 1, 1), jour, "Des dates différentes doivent retourner false");
    }
}