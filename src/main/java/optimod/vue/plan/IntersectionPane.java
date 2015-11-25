package optimod.vue.plan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import optimod.modele.Intersection;
import optimod.modele.Livraison;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Représente une intersection à l'écran.
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionPane extends Circle implements Initializable {

    public static final int TAILLE = 6;

    public static final Color COULEUR_DEFAUT = Color.BLACK;
    public static final Color COULEUR_ENTREPOT = Color.GREEN;
    public static final Color COULEUR_SURVOL = Color.BLUE;
    public static final Color COULEUR_LIVRAISON = Color.RED;

    private Intersection intersection;
    private boolean estEntrepot;
    private boolean survol;

    private Tooltip infobulle;

    public IntersectionPane(Intersection intersection) {
        super(intersection.getX(), intersection.getY(), TAILLE);

        this.intersection = intersection;

        estEntrepot = false;
        survol = false;

        infobulle = new Tooltip();
        dureeApparition(infobulle, 1);

        setOnMouseEntered(event -> survol());
        setOnMouseExited(event -> quitteSurvol());
    }

    public void initialize(URL location, ResourceBundle resources) {
        mettreAJour();
    }

    public void setEstEntrepot(boolean estEntrepot) {
        this.estEntrepot = estEntrepot;
        mettreAJour();
    }

    private boolean aLivraison() {
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

    private void mettreAJour() {
        colorier();
        genererTexteInfobulle();
    }

    private void colorier() {
        if (survol)
            setFill(COULEUR_SURVOL);
        else if (estEntrepot)
            setFill(COULEUR_ENTREPOT);
        else if (aLivraison())
            setFill(COULEUR_LIVRAISON);
        else
            setFill(COULEUR_DEFAUT);
    }

    private void genererTexteInfobulle() {
        String texte = String.format("(%s;%s)",
                intersection.getX(),
                intersection.getY());
        texte += "\nAdresse : " + intersection.getAdresse();
        if (estEntrepot)
            texte += "\nENTREPÔT";
        if (aLivraison()) {
            Livraison livraison = intersection.getLivraison();
            texte += String.format("\nFenêtre de livraison : %s - %s",
                    livraison.getHeureDebutFenetre(),
                    livraison.getHeureFinFenetre());
        }

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

    /**
     * Hack permettant de personnaliser la durée d'apparition des infobulles.
     * <p>
     * Source : http://stackoverflow.com/a/27739605
     *
     * @param tooltip L'infobulle à modifier.
     * @param duree   La durée d'apparition
     */
    private static void dureeApparition(Tooltip tooltip, int duree) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(duree)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
