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

    public void supprimerAction(Action action) {
        if (catalogue.contains(action)) {
            catalogue.remove(action);
            System.out.println("Action '" + action.getLibelle() + "' supprimée avec succès.");
        } else {
            System.out.println("Cette action n'existe pas dans le catalogue.");
            throw new IllegalArgumentException("Action not found in catalogue");
        }
    }

    public List<Action> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(List<Action> catalogue) {
        this.catalogue = catalogue;
    }
}