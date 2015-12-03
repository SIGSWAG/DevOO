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
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param ordonnanceur      Ordonnanceur du système
     */
    void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Charger Livraisons"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param ordonnanceur      Ordonnanceur du système
     */
    void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Calculer l'itinéraire"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param ordonnanceur      Ordonnanceur du système
     */
    void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Annuler"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param listeDeCdes       Liste des Commandes undo/redo du système
     */
    void annulerDerniereAction(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Rejouer"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param listeDeCdes       Liste des Commandes undo/redo du système
     */
    void rejouerDerniereAction(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "+"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     */
    void ajouterLivraison(FenetreControleur fenetreControleur);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Générer feuille de route"
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param ordonnanceur      Ordonnanceur du système
     */
    void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersection               l'intersection selectionnée
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersection               l'intersection déselectionnée
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    boolean deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Intersection intersection, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic gauche sur un point de la vue graphique
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Supprimer les livraisons selectionnees"
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersectionsSelectionnees toutes les intersections dont les livraisons sont selectionnees pour la suppression
     * @param listeDeCdes                Liste des Commandes undo/redo du système
     */
    void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Echanger les livraisons selectionnees"
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersectionsSelectionnees les deux intersections dont les livraisons sont selectionnees pour l'echange
     * @param listeDeCdes                Liste des Commandes undo/redo du système
     */
    void echangerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Annuler l'ajout d'une livraison"
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     */
    void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur après un clic sur le bouton "Valider l'ajout de la livraison"
     *
     * @param fenetreControleur          FenetreControleur contrôlant l'IHM
     * @param ordonnanceur               Ordonnanceur du système
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     * @param listeDeCdes                Liste des Commandes undo/redo du système
     */
    void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCommandes listeDeCdes);

    /**
     * Méthode appelée après chaque action, permet de dire à la vue quels boutons activer
     *
     * @param fenetreControleur FenetreControleur contrôlant l'IHM
     * @param listeDeCdes       Liste des Commandes undo/redo du système
     */
    void mettreAJourVue(FenetreControleur fenetreControleur, ListeDeCommandes listeDeCdes);
}
