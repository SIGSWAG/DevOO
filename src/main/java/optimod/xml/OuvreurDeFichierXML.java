package optimod.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Jonathan on 18/11/2015.
 * Voir http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java pour
 * la raison de l'implémentation en enum
 */
public enum OuvreurDeFichierXML {
    INSTANCE;

    private FileChooser explorateurFichier = new FileChooser();

    public File ouvre(Stage fenetreCourante) {
        explorateurFichier.setTitle("Sélectionner le fichier à ouvrir");
        return explorateurFichier.showOpenDialog(fenetreCourante);
    }
}
