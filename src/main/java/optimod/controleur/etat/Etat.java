package optimod.controleur.etat;

import optimod.controleur.ListeDeCommandes;
import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.util.List;

/**
 * Etat
 */

public interface Etat {
    /**
     * Methode appelee par controleur après un clic sur le bouton "Charger Plan"
     *
     * @param fenetreControleur
     */
    void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Charger Livraisons"
     *
     * @param fenetreControleur
     */
    void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Calculer l'itinéraire"
     *
     * @param fenetreControleur
     */
    void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Annuler"
     *
     * @param fenetreControleur
     * @param listeDeCdes
     */
    void undo(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Rejouer"
     *
     * @param fenetreControleur
     */
    void redo(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "+"
     *
     * @param fenetreControleur
     */
    void ajouterLivraison(FenetreControleur fenetreControleur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Générer feuille de route"
     *
     * @param fenetreControleur
     * @param ordonnanceur
     */
    void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après la saisie d'un caractere au clavier
     *
     * @param fenetreControleur
     * @param listeDeCdes
     * @param codeCar           le code ASCII du caractere saisi
     */
    void carSaisi(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCommandes listeDeCdes, int codeCar);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersection               l'intersection selectionnée
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersection               l'intersection déselectionnée
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Supprimer les livraisons selectionnees"
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees toutes les intersections dont les livraisons sont selectionnees pour la suppression
     * @param listeDeCdes
     */
    void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Echanger les livraisons selectionnees"
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees les deux intersections dont les livraisons sont selectionnees pour l'echange
     * @param listeDeCdes
     */
    void echangeesLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Annuler l'ajout d'une livraison"
     *
     * @param fenetreControleur
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     */
    void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Valider l'ajout de la livraison"
     *
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     * @param listeDeCdes
     */
    void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Méthode appelée après chaque action, permet de dire à la vue quels boutons activer
     *
     * @param fenetreControleur la vue citée ci-dessus
     */
    void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);
}