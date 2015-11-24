package optimod.controleur;

import optimod.modele.Intersection;
import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;

import java.awt.*;
import java.util.List;

/**
 * Created by hdelval on 11/23/15.
 */

public interface Etat {
    /**
     * Methode appelee par controleur apres un clic sur le bouton "Charger Plan"
     * @param fenetreControleur
     */
    public void chargerPlan(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Charger Livraisons"
     * @param fenetreControleur
     */
    public void chargerDemandeLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Calculer l'itinéraire"
     * @param fenetreControleur
     */
    public void calculerItineraire(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Annuler"
     * @param fenetreControleur
     * @param listeDeCdes
     */
    public void undo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Rejouer"
     * @param fenetreControleur
     */
    public void redo(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "+"
     * @param fenetreControleur
     */
    public void ajouterLivraison(FenetreControleur fenetreControleur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Générer feuille de route"
     * @param fenetreControleur
     * @param ordonnanceur
     */
    public void genererFeuilleDeRoute(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres la saisie d'un caractere au clavier
     * @param fenetreControleur
     * @param listeDeCdes
     * @param codeCar le code ASCII du caractere saisi
     */
    public void carSaisi(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, int codeCar);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     * @param fenetreControleur
     * @param listeDeCdes
     * @param p coordonnees du plan correspondant au point clique
     */
    public void clicGauche(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, Point p);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     * @param fenetreControleur
     * @param ordonnanceur
     * @param p coordonnees du plan correspondant au point clique
     * @param rayon le rayon d'imprecision du click, pour localiser la Livraison
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    public boolean selectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     * @param fenetreControleur
     * @param ordonnanceur
     * @param p coordonnees du plan correspondant au point clique
     * @param rayon le rayon d'imprecision du click, pour localiser la Livraison
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    public void deselectionnerIntersection(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, Point p, int rayon, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la vue graphique
     * Precondition : p != null
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees toutes les intersections actuellement selectionnees
     */
    public void deselectionnerToutesIntersections(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Supprimer les livraisons selectionnees"
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees toutes les intersections dont les livraisons sont selectionnees pour la suppression
     * @param listeDeCdes
     */
    public void supprimerLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Echanger les livraisons selectionnees"
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees les deux intersections dont les livraisons sont selectionnees pour l'echange
     * @param listeDeCdes
     */
    public void echangeesLivraisonsSelectionnees(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Annuler l'ajout d'une livraison"
     * @param fenetreControleur
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     */
    public void annulerAjout(FenetreControleur fenetreControleur, List<Intersection> intersectionsSelectionnees);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Valider l'ajout de la livraison"
     * @param fenetreControleur
     * @param ordonnanceur
     * @param intersectionsSelectionnees les intersections selectionnees pour l'ajout
     * @param listeDeCdes
     */
    public void validerAjout(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, List<Intersection> intersectionsSelectionnees, ListeDeCdes listeDeCdes);

    /**
     * Méthode appelée après chaque action, permet de dire à la vue quels boutons activer
     * @param fenetreControleur la vue citée ci-dessus
     */
    public void updateVue(FenetreControleur fenetreControleur, ListeDeCdes listeDeCdes);
}
