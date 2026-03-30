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

import java.util.HashMap;
import java.util.Map;

/**
 * Permet la création d'objets Action simples.
 *
 * @author David Navarre <David.Navarre at irit.fr>
 */
public class ActionSimple extends Action {

    private static final float DEFAULT_ACTION_VALUE = 0.0f;

    /* Dictionnaire stockant les cours par jour */
    private final Map<Jour, Float> mapCours;

    /**
     * Constructeur d'une ActionSimple.
     *
     * @param libelle le nom de l'action
     */
    public ActionSimple(String libelle) {
        super(libelle);
        this.mapCours = new HashMap<>();
        this.typeAction = TypeAction.SIMPLE;
    }

    /**
     * Enregistre ou met à jour le cours pour un jour donné.
     * Cette méthode permet à l'administrateur de modifier une valeur existante.
     *
     * @param j le jour concerné
     * @param v la nouvelle valeur du cours
     */
    public void enregistrerCours(final Jour j, final float v) {
        // Suppression de la condition 'if (!containsKey)' pour permettre la modification
        if (v < 0) {
            throw new IllegalArgumentException("Le cours ne peut pas être négatif.");
        }
        this.mapCours.put(j, v);
    }

    /**
     * Méthode alternative si vous voulez conserver 'enrgCours' pour la compatibilité,
     * mais 'enregistrerCours' est nécessaire pour corriger votre erreur de compilation.
     */
    public void enrgCours(final Jour j, final float v) {
        this.enregistrerCours(j, v);
    }

    @Override
    public float valeur(final Jour j) {
        // Utilisation de getOrDefault pour simplifier le code
        return this.mapCours.getOrDefault(j, DEFAULT_ACTION_VALUE);
    }
}