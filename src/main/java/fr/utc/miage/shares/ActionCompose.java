package fr.utc.miage.shares;

import java.util.Map;

public class ActionCompose extends Action {

   private Map<Action, Float> actions;

    public ActionCompose(String libelle) {
        super(libelle);
    }

    @Override
    public float valeur(Jour j) {
        float res = 0;
        for (Map.Entry<Action, Float> entry : actions.entrySet()) {
            res += entry.getKey().valeur(j) * entry.getValue();
        }
        return res;
    }

}