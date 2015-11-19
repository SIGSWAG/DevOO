package optimod.vue;/**
 * Created by Jonathan on 18/11/2015.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import optimod.controleur.Controleur;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class OptimodApplication extends Application {

    protected static final String FENETRE_XML = "/fxml/Fenetre.fxml";
    protected static final String TITRE_FENETRE = "Optimod - Editeur de livraisons";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage fenetre) throws IOException {
        URL location = getClass().getResource(FENETRE_XML);
        FXMLLoader loader = creerFXMLLoader(location);

        // Création du contrôleur
        Controleur controleur = new Controleur();

        // Mise en place du controleur qui réagit aux évenements utilisateurs côté vue
        loader.setController(new FenetreControleur(fenetre, controleur));

        // Récupération de l'objet root
        Parent root = loader.load(location.openStream());

        //Parent root = FXMLLoader.load(getClass().getResource(FENETRE_XML));

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
