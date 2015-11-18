package optimod.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.vue.Fenetre;
import optimod.vue.OptimodApplication;

import java.io.File;

/**
 * Created by Jonathan on 18/11/2015.
 * Voir http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java pour
 * la raison de l'implémentation en enum
 */
public enum OuvreurDeFichierXML {
    INSTANCE;

    private static final String TITRE_OUVREUR_DE_FICHIER = "Sélectionner le fichier à charger";

    private FileChooser explorateurFichier = new FileChooser();

    /**
     * Propose à l'utilisateur de sélectionner un fichier à charger
     * @param fenetreCourante La fenêtre à laquelle l'explorateur de fichiers sera rattaché
     * @return Le fichier en tant que File, ou null si aucun fichier n'a été sélectionné
     */
    public File ouvre(Fenetre fenetreCourante) {
        explorateurFichier.setTitle(TITRE_OUVREUR_DE_FICHIER);
        // Filtre sur les extensions : filtre XML par défaut, et choix d'afficher tous les fichiers
        explorateurFichier.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("eXtensible Markup Language (*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));
        return explorateurFichier.showOpenDialog(fenetreCourante);
    }
}
