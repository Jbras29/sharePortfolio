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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Administrateur extends User {

    private List<Action> catalogue;

    public Administrateur(String name, String firstName) {
        super(name, firstName); // appel du constructeur de User
        this.catalogue = new ArrayList<>();
    }

    public void publierAction(Action action) {
        if (!catalogue.contains(action)) {
            catalogue.add(action);
            System.out.println("Action '" + action.getLibelle() + "' publiée avec succès.");
        } else {
            System.out.println("Cette action est déjà disponible sur la plateforme.");
        }
    }


    public void updateActionSimpleCours(ActionSimple as, Jour j, float v) {
        // Logique pour mettre à jour la valeur dans l'objet ActionSimple
        as.enregistrerCours(j, v);
    }


    public void updateActionLibelle(Set<Action> catalogue, Action a, String nouveauLibelle) {
        // 1. Retirer du catalogue pour préserver l'intégrité du Hash
        if (catalogue.contains(a)) {
            catalogue.remove(a);

            // 2. Changer le nom via le setter (assurez-vous d'avoir ajouté setLibelle dans Action)
            a.setLibelle(nouveauLibelle);

            // 3. Réinsérer avec le nouveau hashCode
            catalogue.add(a);
        } else {
            // Si l'action n'est pas dans ce catalogue, on la modifie simplement
            a.setLibelle(nouveauLibelle);
        }
    }

    public List<Action> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(List<Action> catalogue) {
        this.catalogue = catalogue;
    }
}