package optimod.vue;/**
 * Created by Jonathan on 18/11/2015.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import optimod.controleur.Controleur;
import optimod.es.xml.DeserialiseurXML;
import optimod.modele.Ordonnanceur;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class OptimodApplication extends Application {

    protected static final String FENETRE_XML = "/vue/Fenetre.fxml";
    protected static final String TITRE_FENETRE = "Optimod - Editeur de livraisons";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fenetre) throws IOException {
        URL location = getClass().getResource(FENETRE_XML);
        FXMLLoader loader = creerFXMLLoader(location);

        Ordonnanceur ordonnanceur = new Ordonnanceur();

        // Création du contrôleur
        Controleur controleur = new Controleur(ordonnanceur);


        // Mise en place du controleur qui réagit aux évenements utilisateurs côté vue
        FenetreControleur fenetreControleur = new FenetreControleur(fenetre, controleur);
        loader.setController(fenetreControleur);
        controleur.setFenetreControleur(fenetreControleur);

        ordonnanceur.getPlan().addObserver(fenetreControleur);
        ordonnanceur.getDemandeLivraison().addObserver(fenetreControleur);

        // Récupération de l'objet root
        Parent root = loader.load(location.openStream());

        //Parent root = FXMLLoader.load(getClass().getResource(FENETRE_XML));

        DeserialiseurXML.INSTANCE.setFenetre(fenetre);



        fenetre.setTitle(TITRE_FENETRE);
        fenetre.setScene(new Scene(root));
        fenetre.show();
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
