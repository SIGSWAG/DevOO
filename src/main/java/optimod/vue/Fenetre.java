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

public class Fenetre extends Application {

    protected static final String FENETRE_XML = "/fxml/Fenetre.fxml";
    protected static final String TITRE_FENETRE = "Optimod - Editeur de livraisons";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL location = getClass().getResource(FENETRE_XML);
        FXMLLoader loader = creerFXMLLoader(location);

        // Mise en place du controleur qui réagit aux évenements utilisateurs côté vue
        loader.setController(new Controleur(primaryStage));

        // Récupération de l'objet root
        Parent root = loader.load(location.openStream());

        //Parent root = FXMLLoader.load(getClass().getResource(FENETRE_XML));

        primaryStage.setTitle(TITRE_FENETRE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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
