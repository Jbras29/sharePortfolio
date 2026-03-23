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


    /**
     * Méthode pour afficher le détail des actions détenues dans le portefeuille.
     * * @param jour Le jour pour lequel on souhaite consulter les valeurs.
     */
    public void afficherDetailsPortefeuille(Jour jour) {
        // Affichage de l'en-tête du tableau
        System.out.println("=== Portefeuille : " + this.nom + " (" + this.type + ") ===");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-20s | %-10s | %-15s | %-15s%n", "Libellé", "Quantité", "Valeur Unitaire", "Valeur Globale");
        System.out.println("-------------------------------------------------------------------------");

        float valeurTotalePortefeuille = 0f;

        // Parcours de la map contenant les actions et leurs quantités
        for (Map.Entry<Action, Integer> entry : this.mapActions.entrySet()) {
            Action action = entry.getKey();
            int quantite = entry.getValue();

            // Récupération du libellé de l'action
            String libelle = action.getLibelle();

            // Calcul de la valeur unitaire pour le jour spécifié
            float valeurUnitaire = action.valeur(jour);

            // Calcul de la valeur globale (quantité * valeur unitaire)
            float valeurGlobale = quantite * valeurUnitaire;

            // Cumul pour obtenir la valeur totale du portefeuille
            valeurTotalePortefeuille += valeurGlobale;

            // Affichage formaté de la ligne pour l'action courante
            System.out.printf("%-20s | %-10d | %-15.2f | %-15.2f%n",
                    libelle, quantite, valeurUnitaire, valeurGlobale);
        }

        // Affichage du pied de page avec la valeur totale du portefeuille
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-51s | %-15.2f%n", "VALEUR TOTALE DU PORTEFEUILLE", valeurTotalePortefeuille);
        System.out.println("=========================================================================\n");
    }

    /**
     * Permet d'acheter une action (simple ou composée) et de l'ajouter au portefeuille.
     * Si l'action existe déjà, on augmente simplement sa quantité.
     *
     * @param action   L'action (ActionSimple ou ActionCompose) à acheter.
     * @param quantite La quantité d'actions à acheter.
     * @throws IllegalArgumentException si la quantité est inférieure ou égale à zéro.
     */
    public void acheterAction(Action action, int quantite) {
        // Vérification de la validité de la quantité
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité à acheter doit être strictement positive.");
        }

        // Si le portefeuille contient déjà cette action, on met à jour la quantité
        if (this.mapActions.containsKey(action)) {
            int quantiteActuelle = this.mapActions.get(action);
            this.mapActions.put(action, quantiteActuelle + quantite);
        } else {
            // Sinon, on ajoute la nouvelle action avec sa quantité
            this.mapActions.put(action, quantite);
        }
    }


}