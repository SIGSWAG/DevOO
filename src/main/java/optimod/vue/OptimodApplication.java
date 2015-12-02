package optimod.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.es.xml.DeserialiseurXML;
import optimod.modele.Ordonnanceur;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Point d'entrée de l'application, permettant de lancer l'IHM de l'application Optimod en initialisant tous les composants
 */
public class OptimodApplication extends Application {

    private static final String FENETRE_XML = "/vue/Fenetre.fxml";
    private static final String TITRE_FENETRE = "Optimod - Editeur de livraisons";
    private static final String STYLE_CSS = "/css/style.css";
    private static final String OPTIMOD_ICONE = "/img/truck.png";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fenetre) throws IOException {
        URL location = getClass().getResource(FENETRE_XML);
        FXMLLoader loader = creerFXMLLoader(location);

        // Mise en place de l'ordonnanceur
        Ordonnanceur ordonnanceur = new Ordonnanceur();

        // Création du contrôleur
        Controleur controleur = new Controleur(ordonnanceur);


        // Mise en place du controleur qui réagit aux évenements utilisateurs côté vue
        FenetreControleur fenetreControleur = new FenetreControleur(fenetre, controleur);
        loader.setController(fenetreControleur);
        controleur.setFenetreControleur(fenetreControleur);

        // Mise en place des observateurs
        ordonnanceur.getPlan().addObserver(fenetreControleur);
        ordonnanceur.getDemandeLivraisons().addObserver(fenetreControleur);

        // Récupération de l'objet root
        Parent root = loader.load(location.openStream());

        DeserialiseurXML.INSTANCE.setFenetre(fenetre);
        fenetre.getIcons().add(new Image(OPTIMOD_ICONE));
        fenetre.setTitle(TITRE_FENETRE);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(STYLE_CSS);
        fenetre.setScene(scene);
        fenetre.show();
        controleur.updateVue();
    }

    /**
     * Création d'une instance du chargeur de FXML utilisé pour l'IHM
     * @param localisation Localisation du fichier FXML correspondant
     * @return
     */
    private FXMLLoader creerFXMLLoader(URL localisation) {
        return new FXMLLoader(localisation, null, new JavaFXBuilderFactory(), null, Charset.forName(FXMLLoader.DEFAULT_CHARSET_NAME));
    }

}
