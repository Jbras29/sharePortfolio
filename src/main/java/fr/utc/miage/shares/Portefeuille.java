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
