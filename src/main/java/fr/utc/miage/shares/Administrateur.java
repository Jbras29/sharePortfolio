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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Administrateur extends User {

    private static final Logger LOGGER = Logger.getLogger(Administrateur.class.getName());
    private List<Action> catalogue;

    public Administrateur(String name, String firstName) {
        super(name, firstName); // appel du constructeur de User
        this.catalogue = new ArrayList<>();
    }

    public void publierAction(Action action) {
        if (!catalogue.contains(action)) {
            catalogue.add(action);
            LOGGER.log(Level.INFO, "Action ''{0}'' publiée avec succès.", action.getLibelle());
        } else {
            LOGGER.warning("Cette action est déjà disponible sur la plateforme.");
        }
    }

    public void supprimerAction(Action action) {
        if (catalogue.remove(action)) {
            LOGGER.log(Level.INFO, "Action ''{0}'' supprimée avec succès.", action.getLibelle());
        } else {
            throw new IllegalArgumentException("Impossible de supprimer l'action '" + action.getLibelle() + "' : elle n'existe pas dans le catalogue.");
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

    public void ajouterPourcentageActionComposee (ActionCompose actionCompose, ActionSimple actionSimple, float pourcentage) {
        if (actionCompose.addAction(actionSimple, pourcentage)) {
            LOGGER.log(Level.INFO,
                    "Action ''{0}'' ajoutée à l''action composée ''{1}'' avec un pourcentage de {2}",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle(), pourcentage});
        } else {
            LOGGER.log(Level.WARNING,
                    "Impossible d''ajouter l''action ''{0}'' à l''action composée ''{1}''. Vérifiez le pourcentage ou si l''action est déjà présente.",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle()});
            throw new IllegalArgumentException("Proportion must be between 0 and 1 and action must be valid and not already exist in the composition.");
        }
    }

    public void updatePourcentageActionComposee (ActionCompose actionCompose, ActionSimple actionSimple, float pourcentage) {
        if (actionCompose.updateProportion(actionSimple, pourcentage)) {
            LOGGER.log(Level.INFO,
                    "Pourcentage de l''action ''{0}'' dans l''action composée ''{1}'' mis à jour à {2}",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle(), pourcentage});
        } else {
            LOGGER.log(Level.WARNING,
                    "Impossible de mettre à jour le pourcentage de l''action ''{0}'' dans l''action composée ''{1}''. Vérifiez le pourcentage ou si l''action est déjà présente.",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle()});
            throw new IllegalArgumentException("Proportion must be between 0 and 1 and action must be valid and already exist in the composition.");
        }
    }

    public void supprimerActionComposee (ActionCompose actionCompose, ActionSimple actionSimple) {
        if (actionCompose.removeAction(actionSimple)) {
            LOGGER.log(Level.INFO,
                    "Action ''{0}'' supprimée de l''action composée ''{1}''.",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle()});
        } else {
            LOGGER.log(Level.WARNING,
                    "Impossible de supprimer l''action ''{0}'' de l''action composée ''{1}''. Vérifiez si l''action est présente dans la composition.",
                    new Object[]{actionSimple.getLibelle(), actionCompose.getLibelle()});
            throw new IllegalArgumentException("Action must be valid and already exist in the composition.");
        }
    }

    public List<Action> retrieveActionsWithNoCours(Jour j) {
        List<Action> actionsWithoutCours = new ArrayList<>();
        for (Action action : catalogue) {
            if (action.valeur(j) == 0.0f) { // 0.0f signifie "pas de cours enregistré"
                actionsWithoutCours.add(action);
            }
        }
        return actionsWithoutCours;
    }

    public List<Action> retrieveActionsWithNoCoursCurrentDate() {
        LocalDate currentDate = LocalDate.now(); // Obtenir la date actuelle
        Jour today = new Jour(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        return retrieveActionsWithNoCours(today);
    }
}