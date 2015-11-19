package optimod.xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.vue.OptimodApplication;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * Created by Jonathan on 18/11/2015.
 * Voir http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java pour
 * la raison de l'implémentation en enum
 */
public enum OuvreurDeFichierXML {
    INSTANCE;

    private static final String TITRE_OUVREUR_DE_FICHIER = "Sélectionner le fichier à charger";
    private static final String CLE_REGISTRE_FICHIER_CHOISI = "cheminFichier";

    private FileChooser explorateurFichier = new FileChooser();

    /**
     * Propose à l'utilisateur de sélectionner un fichier à charger
     * @param fenetreCourante La fenêtre à laquelle l'explorateur de fichiers sera rattaché
     * @return Le fichier en tant que File, ou null si aucun fichier n'a été sélectionné
     */
    public File ouvre(Stage fenetreCourante) {
        explorateurFichier.setTitle(TITRE_OUVREUR_DE_FICHIER);
        // Filtre sur les extensions : filtre XML par défaut, et choix d'afficher tous les fichiers
        explorateurFichier.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("eXtensible Markup Language (*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));

        // Récupérer le dernier fichier ouvert (s'il existe)
        File dernierFichierOuvert = getDernierFichierOuvert();
        if(dernierFichierOuvert != null) {
            if(dernierFichierOuvert.exists()) {
                explorateurFichier.setInitialDirectory(dernierFichierOuvert.getParentFile());
            }
        }

        File fichierChoisi = explorateurFichier.showOpenDialog(fenetreCourante);
        if(fichierChoisi != null) {
            setDernierFichierOuvert(fichierChoisi);
        }

        return fichierChoisi;
    }

    /**
     * Retourne le dernier fichier chargé par l'utilisateur, ou null s'il n'existe pas
     * @return
     */
    protected File getDernierFichierOuvert() {
        Preferences prefs = Preferences.userNodeForPackage(OptimodApplication.class);
        String cheminFichier = prefs.get(CLE_REGISTRE_FICHIER_CHOISI, null);
        if (cheminFichier != null) {
            return new File(cheminFichier);
        } else {
            return null;
        }
    }

    /**
     * Enregistrer dans les clés de registres l'emplacement du dernier fichier ouvert
     * @param fichierChoisi
     */
    protected void setDernierFichierOuvert(File fichierChoisi) {
        Preferences prefs = Preferences.userNodeForPackage(OptimodApplication.class);
        if (fichierChoisi != null) {
            prefs.put(CLE_REGISTRE_FICHIER_CHOISI, fichierChoisi.getPath());
        }
    }
}
