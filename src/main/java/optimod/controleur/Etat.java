package optimod.controleur;

import optimod.modele.Ordonnanceur;
import optimod.vue.FenetreControleur;
import java.awt.*;

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
    public void undo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Rejouer"
     * @param fenetreControleur
     */
    public void redo(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "+"
     * @param fenetreControleur
     */
    public void ajouterLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Echanger"
     * @param fenetreControleur
     */
    public void echangerLivraisons(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Supprimer"
     * @param fenetreControleur
     */
    public void supprimerLivraison(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur);

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
     * @param p = coordonnees du plan correspondant au point clique
     */
    public void clicGauche(FenetreControleur fenetreControleur, Ordonnanceur ordonnanceur, ListeDeCdes listeDeCdes, Point p);

    /**
     * Méthode appelée après chaque action, permet de dire à la vue quels boutons activer
     * @param fenetreControleur la vue citée ci-dessus
     */
    public void updateVue(FenetreControleur fenetreControleur);
}
