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
public class Administrateur {

    private String name;
    private String firstName;
    private ActionCompose actionCompose;
    private ActionSimple action;
    private List<Action> catalogue;

     public Administrateur(String name, String firstName, ActionCompose actionCompose,ActionSimple action) {
        this.name = name;
        this.firstName = firstName;
        this.actionCompose=actionCompose;
        this.action=action;
        this.catalogue = new ArrayList<>();
    }

     public void publierActionSimple(ActionSimple actionSimple) {
        if (!catalogue.contains(actionSimple)) {
            catalogue.add(actionSimple);
            System.out.println("Action simple '" + actionSimple.getLibelle() + "' publiée avec succès.");
        } else {
            System.out.println("Cette action est déjà disponible sur la plateforme.");
        }

        public void publierActionCompose(ActionCompose actionCompose) {
        if (!catalogue.contains(actionCompose)) {
            catalogue.add(actionCompose);
            System.out.println("Action composée '" + actionCompose.getLibelle() + "' publiée avec succès.");
        } else {
            System.out.println("Cette action est déjà disponible sur la plateforme.");
        }
    
        }

        public List<Action> getCatalogue() {
        return catalogue;
    }
}
