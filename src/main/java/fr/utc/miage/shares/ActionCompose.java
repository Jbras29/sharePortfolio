package fr.utc.miage.shares;

import java.util.Collections;
import java.util.Map;

public class ActionCompose extends Action {

    private Map<Action, Float> actions;

    public ActionCompose(String libelle) {
        super(libelle);
    }

    public Map<Action, Float> getActions() {
        return Collections.unmodifiableMap(actions);
    }

    public boolean addAction(ActionSimple action, float proportion) {
        if (proportion < 0 || proportion > 1) {
            throw new IllegalArgumentException("Proportion must be between 0 and 1");
        }
        if (actions.containsKey(action)) {
            return false; // Action already exists
        }
        actions.put(action, proportion);
        return true;
    }

    public boolean isComposeValid() {
        float totalProportion = 0;
        for (float proportion : actions.values()) {
            totalProportion += proportion;
        }
        return !(totalProportion > 1 || totalProportion < 0);
    }

    public Float getProportion(Action action) {
        return actions.get(action);
    }

    @Override
    public float valeur(Jour j) {
        if (!isComposeValid()) {
            throw new IllegalStateException("Total proportion cannot be less than 1 or exceed 1");
        }

        float res = 0;
        for (Map.Entry<Action, Float> entry : actions.entrySet()) {
            res += entry.getKey().valeur(j) * entry.getValue();
        }
        return res;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ActionCompose other = (ActionCompose) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActionCompose [actions=");
        for (Map.Entry<Action, Float> entry : actions.entrySet()) {
            sb.append(entry.getKey().getLibelle()).append(": ").append(entry.getValue()).append(", ");
        }
        if (!actions.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]"); 
        return sb.toString();
    }

    

}