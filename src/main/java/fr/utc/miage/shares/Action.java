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

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Cette classe définit le comportement commun de tout objet Action.
 *
 * @author David Navarre &lt;David.Navarre at irit.fr&gt;
 */
public abstract class Action {

    private static final Logger LOGGER = Logger.getLogger(Action.class.getName());

    /* Le libellé de l'action (non-final pour permettre la modification) */
    private String libelle;

    protected TypeAction typeAction;

    /**
     * Retourne la valeur du libellé.
     *
     * @return le libellé de l'action
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit ou modifie le libellé de l'action.
     * Cette méthode permet à un administrateur de mettre à jour le nom.
     *
     * @param libelle le nouveau nom de l'action
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Construit une Action à partir d'un paramètre String.
     *
     * @param libelle le nom de l'objet action
     */
    protected Action(final String libelle) {
        this.libelle = libelle;
    }

    /**
     * Fournit la valeur de l'objet action pour un jour donné.
     *
     * @param j le jour concerné
     * @return la valeur de l'action
     */
    public abstract float valeur(Jour j);

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.libelle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Action other = (Action) obj;
        return Objects.equals(this.libelle, other.libelle);
    }

    @Override
    public String toString() {
        return this.getLibelle();
    }

    public TypeAction getTypeAction() {
        return typeAction;
    }

    protected void setTypeAction(TypeAction typeAction) {
        this.typeAction = typeAction;
    }

    public TypeAction simpleOuComposee() {
        if (typeAction == TypeAction.SIMPLE) {
            LOGGER.info("L'action '" + getLibelle() + "' est une action simple.");
        } else {
            LOGGER.info("L'action '" + getLibelle() + "' est une action composée.");
        }
        return typeAction;
    }
}
