package fr.utc.miage.shares;

import java.util.Map;

public class Portefeuille {
    private final String nom;
    private final String type;
    private final Map<Action, Integer> mapActions;

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

        public String setNom(String nom) {
            return this.nom;
        }

        public String setType(String type) {
            return this.type;
        }

        public Map<Action, Integer> setMapActions(Map<Action, Integer> mapActions) {
            return this.mapActions;
        }

        
}
