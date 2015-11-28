package optimod.vue.plan;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import optimod.modele.*;
import optimod.vue.FenetreControleur;
import optimod.vue.livraison.AfficheurFenetresLivraison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by aurelien on 23/11/15.
 */
public final class AfficheurPlan {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FenetreControleur fenetreControleur;

    private Group group;
    
    private List<Color> couleurs;

    private List<Color> couleursUtilisees;

    private List<IntersectionPane> intersectionsSelectionnees;

    private AfficheurFenetresLivraison afficheurFenetresLivraison;

    public AfficheurPlan(Group group, FenetreControleur fenetreControleur) {
        this.group = group;
        this.fenetreControleur = fenetreControleur;
        this.couleurs = chargerCouleurs();
        this.couleursUtilisees = new ArrayList<>();
        this.couleursUtilisees.add(IntersectionPane.COULEUR_DEFAUT);
        this.couleursUtilisees.add(IntersectionPane.COULEUR_ENTREPOT);
        this.couleursUtilisees.add(IntersectionPane.COULEUR_LIVRAISON);
        this.intersectionsSelectionnees = new ArrayList<>();
    }

    private final List<Color> chargerCouleurs() {
        List<Color> couleurs = chargerEchantillonCouleurs();
//        try {
//            couleurs = chargerToutesLesCouleurs();
//        } catch (ClassNotFoundException e) {
//            logger.error("Classe Color non trouvée, impossible de charger les couleurs", e);
//            this.couleurs = new ArrayList<>();
//        } catch (IllegalAccessException e) {
//            logger.error("Accès illégal à une propriété", e);
//            this.couleurs = new ArrayList<>();
//        }

        return couleurs;
    }

    private static final List<Color> chargerToutesLesCouleurs() throws ClassNotFoundException, IllegalAccessException {
        List<Color> couleurs = new ArrayList<>();
        Class clazz = Class.forName("javafx.scene.paint.Color");
        if (clazz != null) {
            Field[] champs = clazz.getFields();
            for(int i = 0; i < champs.length; i++) {
                Field champ = champs[i];
                Object obj = champ.get(null);
                if(obj instanceof Color) {
                    couleurs.add((Color) obj);
                }
            }
        }
        Collections.shuffle(couleurs);
        return couleurs;
    }

    private static final List<Color> chargerEchantillonCouleurs() {
        List<Color> couleurs = new ArrayList<>();
        couleurs.add(Color.RED);
        couleurs.add(Color.YELLOW);
        couleurs.add(Color.GREEN);
        couleurs.add(Color.BROWN);
        couleurs.add(Color.GOLD);
        couleurs.add(Color.VIOLET);
        couleurs.add(Color.PINK);
        couleurs.add(Color.ORANGE);
        couleurs.add(Color.OLIVE);
        couleurs.add(Color.STEELBLUE);

        Collections.shuffle(couleurs);

        return couleurs;
    }

    /**
     * Ajoute des intersections au plan, ainsi que leurs tronçons.
     *
     * @param plan Le plan à charger.
     */
    public void chargerPlan(Plan plan) {
        vider();

        for (Intersection intersection : plan.getIntersections()) {
            IntersectionPane intersectionPane = new IntersectionPane(intersection, fenetreControleur);
            group.getChildren().add(intersectionPane);
            for (Troncon troncon : intersection.getSortants()) {
                group.getChildren().add(new TronconPane(intersectionPane, troncon));
            }
            intersectionPane.toFront(); // Les intersections s'affichent au dessus des tronçons
        }
    }

    /**
     * Affiche une demande de livraison sur le plan.
     *
     * @param demandeLivraisons
     */
    public void chargerDemandeLivraisons(DemandeLivraisons demandeLivraisons) {
        reinitialiserLivraisons();
        Livraison entrepot = demandeLivraisons.getEntrepot();
        IntersectionPane intersectionPane = trouverIntersectionPane(entrepot.getIntersection());
        intersectionPane.setEstEntrepot(true);

    }

    /**
     * Affiche un itinéraire sur le plan.
     */
    public void chargerItineraire() {

        getTronconsPane().forEach(TronconPane::mettreAJour);

    }

    /**
     * Vide le plan de tous ses éléments.
     */
    private void vider() {
        group.getChildren().clear();
    }

    /**
     * Réinitialise toutes les livraisons dessinées.
     */
    private void reinitialiserLivraisons() {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            intersectionPane.setEstEntrepot(false);
        }
    }

    private IntersectionPane trouverIntersectionPane(Intersection intersection) {
        for (IntersectionPane intersectionPane : getIntersectionsPane()) {
            if (intersectionPane.getIntersection().equals(intersection))
                return intersectionPane;
        }
        return null;
    }

    public void selectionnerLivraison(Livraison livraison, boolean deselectionnerAvant) {
        Intersection intersection = livraison.getIntersection();
        IntersectionPane intersectionPane = trouverIntersectionPane(intersection);
        if (intersectionPane != null) {
            logger.debug("Surbrillance");
            if(deselectionnerAvant) {
                deselectionnerIntersections();
            }
            //intersectionPane.setStyle("-fx-background-color:#10cc00;");
            if (Platform.isSupported(ConditionalFeature.EFFECT)) {
                DropShadow dropShadow = new DropShadow(10, Color.BLUE);
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                intersectionPane.setEffect(dropShadow);
                intersectionsSelectionnees.add(intersectionPane);
            }
        }
        intersectionPane.selectionner();
    }

    public void deselectionnerToutesIntersections() {
        getIntersectionsPane().forEach(IntersectionPane::deselectionner);
    }

    public void selectionnerLivraisons(FenetreLivraison fenetreLivraison) {
        deselectionnerIntersections();
        for(Livraison livraison : fenetreLivraison.getLivraisons()) {
            selectionnerLivraison(livraison, false);
        }
    }

    private void deselectionnerIntersections() {
        for(IntersectionPane intersectionPane : intersectionsSelectionnees) {
            intersectionPane.setEffect(null);
        }
    }

    public Color colorierLivraisons(FenetreLivraison fenetreLivraison) {
        logger.debug("Coloriage de la fenêtre de livraison");
        Color couleur = choisirCouleurNonUtilisee();
        if(couleur == null) {
            logger.error("Pas de couleur disponible pour colorier les intersections de la fenêtre de livraison");
            // TODO Throw Exception
            return null;
        }

        for(Livraison livraison : fenetreLivraison.getLivraisons()) {
            Intersection intersection = livraison.getIntersection();
            IntersectionPane intersectionPane = trouverIntersectionPane(intersection);
            if(intersectionPane.aUneLivraison()) {
                logger.debug("Coloriage de l'intersection en {}", couleur.toString());
                intersectionPane.setFill(couleur);

            }
        }

        return couleur;
    }

    private Color choisirCouleurNonUtilisee() {
        if (couleursUtilisees.isEmpty()) {
            Color couleur = couleurs.get(0);
            couleursUtilisees.add(couleur);
            return couleur;
        }

        for (Color couleur : couleurs) {
            boolean estUtilisee = false;
            for (Color couleurUtilisee : couleursUtilisees) {
                if (couleurUtilisee.equals(couleur)) {
                    estUtilisee = true;
                    break;
                }
            }
            if (!estUtilisee) {
                couleursUtilisees.add(couleur);
                return couleur;
            }
        }

        return null;
    }

    private Collection<IntersectionPane> getIntersectionsPane() {
        List<IntersectionPane> intersectionsCercle = new ArrayList<IntersectionPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersectionsCercle.add((IntersectionPane) noeud);
            }
        }
        return intersectionsCercle;
    }

    private Collection<TronconPane> getTronconsPane() {
        List<TronconPane> tronconsLigne = new ArrayList<TronconPane>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                tronconsLigne.add((TronconPane) noeud);
            }
        }
        return tronconsLigne;
    }

    private Collection<Intersection> getIntersections() {
        ArrayList<Intersection> intersections = new ArrayList<Intersection>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof IntersectionPane) {
                intersections.add(((IntersectionPane) noeud).getIntersection());
            }
        }
        return intersections;
    }

    private Collection<Troncon> getTroncons() {
        List<Troncon> troncons = new ArrayList<Troncon>();
        for (Node noeud : group.getChildren()) {
            if (noeud instanceof TronconPane) {
                troncons.add(((TronconPane) noeud).getTroncon());
            }
        }
        return troncons;
    }

    public void setAfficheurFenetresLivraison(AfficheurFenetresLivraison afficheurFenetresLivraison) {
        this.afficheurFenetresLivraison = afficheurFenetresLivraison;
    }

}
