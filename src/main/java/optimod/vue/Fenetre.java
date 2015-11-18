package optimod.vue;/**
 * Created by Jonathan on 18/11/2015.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Fenetre extends Application {

    protected static final String FENETRE_XML = "/fxml/Fenetre.fxml";
    protected static final String TITRE_FENETRE = "Optimod - Editeur de livraisons";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(FENETRE_XML));
        primaryStage.setTitle(TITRE_FENETRE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
