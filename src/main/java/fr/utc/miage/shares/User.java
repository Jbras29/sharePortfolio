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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String name;
    private String firstName;
    private Portefeuille portefeuille;

    public User(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
        this.portefeuille = new Portefeuille(name + " " + firstName, "Standard", new HashMap<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Portefeuille getPortefeuille() {
        return portefeuille;
    }

    public void setPortefeuille(Portefeuille portefeuille) {
        this.portefeuille = portefeuille;
    }

    public Map<Action, Integer> quantitePossede() {
        Map<Action, Integer> result = new HashMap<>();

        if (portefeuille != null) {
            result.putAll(portefeuille.getMapActions());
        }

        return result;
    }
  
    public ActionDTO getInfoAction(Action action) {
        LocalDate today = LocalDate.now();
        Jour jourActuel = new Jour(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        return new ActionDTO(action.getLibelle(), action.getTypeAction(), action.valeur(jourActuel));
    } 
<<<<<<< HEAD
=======

    public float valeurPortefeuille(Jour jour) {
    float valeurTotale = 0;              // 1. On commence à 0€
    
    for (Map.Entry<Action, Integer> entry : this.portefeuille.getMapActions().entrySet()) 
    { 
        Action action = entry.getKey();   
        int quantite = entry.getValue();  
        
        valeurTotale += action.valeur(jour) * quantite;
        
    }
    
    return valeurTotale; 
}
>>>>>>> 3e0a860cfd5cce967a93b08ae0b1668ed64ba61f
}
