package optimod.vue.plan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import optimod.modele.Intersection;
import optimod.modele.Livraison;
import optimod.vue.FenetreControleur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Représente une intersection à l'écran.
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionPane extends Circle {

    private static final Logger logger = LoggerFactory.getLogger(IntersectionPane.class);

    public static final int TAILLE = 6;

    private static final String FORMAT_HEURE = "%02d:%02d:%02d";

    private Color couleur;
    private static final Color COULEUR_DEFAUT = Color.BLACK;
    private static final Color COULEUR_ENTREPOT = Color.GREEN;
    private static final Color COULEUR_LIVRAISON = Color.BLUE;
    private final FenetreControleur fenetreControleur;

    private final Intersection intersection;
    private boolean estEntrepot;
    private boolean survol;
    private boolean selectionne;

    private final Tooltip infobulle;

    public IntersectionPane(final Intersection intersection, final FenetreControleur fenetreControleur) {
        super(intersection.getX(), intersection.getY(), TAILLE);

        this.intersection = intersection;
        this.fenetreControleur = fenetreControleur;

        infobulle = new Tooltip();
        dureeApparition(infobulle, 1);

        setOnMouseEntered(evenement -> survol());
        setOnMouseExited(evenement -> quitteSurvol());
        setOnMouseClicked(evenement -> click());

        reinitialiser();
    }

    public void reinitialiser() {
        couleur = COULEUR_DEFAUT;

        estEntrepot = false;
        survol = false;
        selectionne = false;
    }

    private void click() {
        if (estEntrepot)
            return;
        if (selectionne) {
            fenetreControleur.deselectionner(intersection);
        } else {
            fenetreControleur.selectionner(intersection);
        }
    }

    public void selectionner() {
        selectionne = true;
        colorier();
    }

    public void deselectionner() {
        selectionne = false;
        colorier();
    }

    public void setEstEntrepot(final boolean estEntrepot) {
        this.estEntrepot = estEntrepot;
        couleur = COULEUR_ENTREPOT;
        mettreAJour();
    }

    public boolean aUneLivraison() {
        return intersection.getLivraison() != null;
    }

    private void survol() {
        survol = true;
        if (!infobulle.getText().isEmpty())
            Tooltip.install(this, infobulle);
        colorier();
    }

    private void quitteSurvol() {
        survol = false;
        Tooltip.uninstall(this, infobulle);
        colorier();
    }

    public void mettreAJour() {
        colorier();
        genererTexteInfobulle();
    }

    private void colorier() {
        if (estEntrepot) {
            setFill(COULEUR_ENTREPOT);
            final InnerShadow ombre = new InnerShadow(10, Color.WHITE);
            setEffect(ombre);
            return;
        }

        if (survol && aUneLivraison()) {
            setCursor(Cursor.HAND);
        }

        setFill(couleur);

        if (selectionne) {
            final DropShadow ombre = new DropShadow(10, couleur);
            ombre.setBlurType(BlurType.GAUSSIAN);
            setEffect(ombre);
        } else {
            setEffect(null);
        }

    }

    public String genererTexteIntersection(final Intersection intersection) {
        String texte = String.format("(%s;%s)",
                intersection.getX(),
                intersection.getY());
        texte += "\nAdresse : " + intersection.getAdresse();
        if (estEntrepot)
            texte += "\nENTREPÔT";
        else {
            if (aUneLivraison()) { // Si l'intersection est l'entrepôt, on ne veut pas afficher sa fenêtre de livraison...
                final Livraison livraison = intersection.getLivraison();
                final String heureDebut = String.format(FORMAT_HEURE, livraison.getHeureDebutFenetreHeure(), livraison.getHeureDebutFenetreMinute(), livraison.getHeureDebutFentreSeconde());
                final String heureFin = String.format(FORMAT_HEURE, livraison.getHeureFinFenetreHeure(), livraison.getHeureFinFenetreMinute(), livraison.getHeureFinFentreSeconde());
                texte += String.format("\nFenêtre de livraison : %s - %s",
                        heureDebut,
                        heureFin);
                if (livraison.initeraireCalcule()) {
                    final String heureLivraison = String.format(FORMAT_HEURE, livraison.getHeure(), livraison.getMinute(), livraison.getSeconde());
                    texte += "\nHeure de livraison prévue : " + heureLivraison;
                }
            }
        }

        return texte;
    }

    private void genererTexteInfobulle() {
        String texte = genererTexteIntersection(intersection);
        infobulle.setText(texte);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public int getX() {
        return intersection.getX();
    }

    public int getY() {
        return intersection.getY();
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    /**
     * Permet de personnaliser la durée d'apparition des infobulles.
     * <p>
     * Source : http://stackoverflow.com/a/27739605
     *
     * @param tooltip L'infobulle à modifier.
     * @param duree   La durée d'apparition
     */
    private static void dureeApparition(Tooltip tooltip, int duree) {
        try {
            Field comportementChamp = tooltip.getClass().getDeclaredField("BEHAVIOR");
            comportementChamp.setAccessible(true);
            Object comportementObjet = comportementChamp.get(tooltip);

            Field timerChamp = comportementObjet.getClass().getDeclaredField("activationTimer");
            timerChamp.setAccessible(true);
            Timeline objTimer = (Timeline) timerChamp.get(comportementObjet);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(duree)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Problème dans la mise en place de la durée d'apparition de l'infobulle", e);
        }
    }

}
