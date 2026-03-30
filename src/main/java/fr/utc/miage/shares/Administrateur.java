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

    public List<Action> getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(List<Action> catalogue) {
        this.catalogue = catalogue;
    }

    public void ajouterPourcentageActionComposée (ActionCompose actionCompose, ActionSimple actionSimple, float pourcentage) {
        if (actionCompose.addAction(actionSimple, pourcentage)) {
            System.out.println("Action '" + actionSimple.getLibelle() + "' ajoutée à l'action composée '" + actionCompose.getLibelle() + "' avec un pourcentage de " + pourcentage);
        } else {
            System.out.println("Impossible d'ajouter l'action '" + actionSimple.getLibelle() + "' à l'action composée '" + actionCompose.getLibelle() + "'. Vérifiez le pourcentage ou si l'action est déjà présente.");
            throw new IllegalArgumentException("Proportion must be between 0 and 1 and action must be valid and not already exist in the composition.");
        }
    }

    public void updatePourcentageActionComposée (ActionCompose actionCompose, ActionSimple actionSimple, float pourcentage) {
        if (actionCompose.updateProportion(actionSimple, pourcentage)) {
            System.out.println("Pourcentage de l'action '" + actionSimple.getLibelle() + "' dans l'action composée '" + actionCompose.getLibelle() + "' mis à jour à " + pourcentage);
        } else {
            System.out.println("Impossible de mettre à jour le pourcentage de l'action '" + actionSimple.getLibelle() + "' dans l'action composée '" + actionCompose.getLibelle() + "'. Vérifiez le pourcentage ou si l'action est déjà présente.");
            throw new IllegalArgumentException("Proportion must be between 0 and 1 and action must be valid and already exist in the composition.");
        }
    }

    public void supprimerActionComposée (ActionCompose actionCompose, ActionSimple actionSimple) {
        if (actionCompose.removeAction(actionSimple)) {
            System.out.println("Action '" + actionSimple.getLibelle() + "' supprimée de l'action composée '" + actionCompose.getLibelle() + "'.");
        } else {
            System.out.println("Impossible de supprimer l'action '" + actionSimple.getLibelle() + "' de l'action composée '" + actionCompose.getLibelle() + "'. Vérifiez si l'action est présente dans la composition.");
            throw new IllegalArgumentException("Action must be valid and already exist in the composition.");
        }
    }
}