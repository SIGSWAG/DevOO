package optimod.vue.plan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import optimod.modele.Intersection;

import java.lang.reflect.Field;

/**
 * Représente une intersection à l'écran.
 * Created by Jonathan on 19/11/2015.
 */
public class IntersectionPane extends Circle {

    public static final int TAILLE = 6;

    public static final Color COULEUR_ENTREPOT = Color.GREEN;
    public static final Color COULEUR_DEFAUT = Color.BLACK;
    public static final Color COULEUR_SURVOL = Color.BLUE;

    private Intersection intersection;
    private boolean estEntrepot;
    private boolean survol;

    private Tooltip infobulle;

    public IntersectionPane(Intersection intersection) {
        super(intersection.getX(), intersection.getY(), TAILLE);

        this.intersection = intersection;

        setEstEntrepot(false);
        survol = false;

        infobulle = new Tooltip(getTexteInfobulle());
        dureeApparition(infobulle, 25);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                survol();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                quitteSurvol();
            }
        });
    }

    public void setEstEntrepot(boolean estEntrepot) {
        this.estEntrepot = estEntrepot;
        colorier();
    }

    private void survol() {
        survol = true;
        Tooltip.install(this, infobulle);
        colorier();
    }

    private void quitteSurvol() {
        survol = false;
        Tooltip.uninstall(this, infobulle);
        colorier();
    }

    private void colorier() {
        if (survol)
            setFill(COULEUR_SURVOL);
        else if (estEntrepot)
            setFill(COULEUR_ENTREPOT);
        else
            setFill(COULEUR_DEFAUT);
    }

    private String getTexteInfobulle() {
        return String.format("(%s;%s)\nAdresse : %s",
                intersection.getX(),
                intersection.getY(),
                intersection.getAdresse());
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
     * @param tooltip
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
