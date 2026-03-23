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

import java.util.Map;

public class Portefeuille {
    private final String nom;
    private String type;
    private Map<Action, Integer> mapActions;

    public Portefeuille(final String nom, final String type, Map<Action, Integer> mapActions) {
        this.nom = nom;
        this.type = type;
        this.mapActions = mapActions;

    }

        public String getNom() {
            return nom;
        }

        public String getType() {
            return type;
        }

        public Map<Action, Integer> getMapActions() {
            return mapActions;
        }

        public void setType(String type) {
            this.type=type;
        }

        public void setMapActions(Map<Action, Integer> mapActions) {
            this.mapActions=mapActions;
        }

        public String afficher(){
            if (mapActions.isEmpty()){
                return "Le portefeuille" +nom+ "est vide";
            }
            return "Le portefeuille " + nom + "contient des actions";
        }

        public boolean contientAction (Action action){
            return mapActions.containsKey(action);
        }

        

}
