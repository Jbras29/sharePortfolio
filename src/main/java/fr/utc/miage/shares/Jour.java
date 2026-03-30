/*
 * Copyright 2025 David Navarre <David.Navarre@irit.fr>.
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

/**
 * This class aims at describing a day based on a year and the day in this year.
 *
 * @author David Navarre &lt;David.Navarre@ut-capitole.fr&gt;
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Cette classe décrit un jour précis en utilisant LocalDate pour gérer la logique temporelle.
 */
public final class Jour implements Comparable<Jour> {

    /* Utilisation de la bibliothèque standard Java pour gérer les dates */
    private final LocalDate date;

    /**
     * Constructeur à partir de l'année, du mois et du jour du mois.
     * Exemple : new Jour(2026, 3, 30)
     */
    public Jour(int year, int month, int dayOfMonth) {
        // LocalDate.of gère automatiquement les erreurs (ex: 31 février)
        this.date = LocalDate.of(year, month, dayOfMonth);
    }

    /**
     * Retourne le mois sous forme d'entier (1-12).
     */
    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }

    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    /**
     * Retourne le numéro du jour dans l'année (ce que vous aviez avant).
     */
    public int getDayOfYear() {
        return date.getDayOfYear();
    }

    @Override
    public int compareTo(Jour o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jour jour = (Jour) o;
        return Objects.equals(date, jour.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        // Formatage lisible : "30/03/2026"
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
