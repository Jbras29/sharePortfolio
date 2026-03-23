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
            throw new IllegalStateException("La composition n'est pas valide : la somme des proportions doit être comprise entre 0 et 1");
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

    public boolean removeAction(Action action) {
        if (!actions.containsKey(action)) {
            return false; // Action does not exist
        }
        actions.remove(action);
        return true;
    }   

    public boolean updateProportion(Action action, float newProportion) {
        if (newProportion < 0 || newProportion > 1) {
            throw new IllegalArgumentException("Proportion must be between 0 and 1");
        }
        if (!actions.containsKey(action)) {
            return false; // Action does not exist
        }
        actions.put(action, newProportion);
        return true;
    }   
    

}