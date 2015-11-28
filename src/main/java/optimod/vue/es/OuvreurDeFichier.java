package optimod.vue.es;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.es.xml.ExceptionXML;
import optimod.vue.OptimodApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by Jonathan on 18/11/2015.
 * Voir http://stackoverflow.com/questions/70689/what-is-an-efficient-way-to-implement-a-singleton-pattern-in-java pour
 * la raison de l'implémentation en enum
 */
public enum OuvreurDeFichier { // Singleton
    INSTANCE;

    // TODO enum pour les modes ?
    public static final boolean MODE_LECTURE = true;
    public static final boolean MODE_ECRITURE = false;

    private static final String CLE_REGISTRE_FICHIER_CHOISI_LECTURE = "cheminFichierLecture";
    private static final String CLE_REGISTRE_FICHIER_CHOISI_ECRITURE = "cheminFichierEcriture";
    private static String TITRE_OUVREUR_DE_FICHIER = "Sélectionner le fichier à charger";
    private static List<FileChooser.ExtensionFilter> EXTENSIONS = new ArrayList<FileChooser.ExtensionFilter>();
    private static boolean MODE = MODE_LECTURE;

    private FileChooser explorateurFichier = new FileChooser();


    /**
     * Propose à l'utilisateur de sélectionner un fichier à charger
     * @param fenetreCourante La fenêtre à laquelle l'explorateur de fichiers sera rattaché
     * @return Le fichier en tant que File, ou null si aucun fichier n'a été sélectionné
     */
    public File ouvre(Stage fenetreCourante) throws ExceptionXML {
        //explorateurFichier.setTitle(TITRE_OUVREUR_DE_FICHIER);
        // Filtre sur les extensions : filtre XML par défaut, et choix d'afficher tous les fichiers
        //explorateurFichier.getExtensionFilters().addAll(
        //          new FileChooser.ExtensionFilter("eXtensible Markup Language (*.xml)", "*.xml"),
        //          new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));

        // Récupérer le dernier fichier ouvert (s'il existe)
        File dernierFichierOuvert = getDernierFichierOuvert();
        if(dernierFichierOuvert != null) {
            if(dernierFichierOuvert.exists()) {
                explorateurFichier.setInitialDirectory(dernierFichierOuvert.getParentFile());
            }
        }

        File fichierChoisi = this.ouvreFenetreDeDialogue(fenetreCourante);

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
        String cheminFichier = null;
        if(MODE = MODE_LECTURE){
            cheminFichier = prefs.get(CLE_REGISTRE_FICHIER_CHOISI_LECTURE, null);
        }
        else if(MODE = MODE_ECRITURE){
            cheminFichier = prefs.get(CLE_REGISTRE_FICHIER_CHOISI_ECRITURE, null);
        }
        if (cheminFichier != null) {
            return new File(cheminFichier);
        } else {
            return null;
        }
    }

    /**
     * Enregistre dans les clés de registres l'emplacement du dernier fichier ouvert
     * @param fichierChoisi
     */
    protected void setDernierFichierOuvert(File fichierChoisi) {
        Preferences prefs = Preferences.userNodeForPackage(OptimodApplication.class);
        if (fichierChoisi != null) {
            if(MODE = MODE_LECTURE){
                prefs.put(CLE_REGISTRE_FICHIER_CHOISI_LECTURE, fichierChoisi.getPath());
            }
            else if(MODE = MODE_ECRITURE){
                prefs.put(CLE_REGISTRE_FICHIER_CHOISI_ECRITURE, fichierChoisi.getPath());
            }
        }
    }

    /**
     * Ouvre la fenetre de dialogue et retourne le fichier choisi
     * @param fenetre La fenêtre à laquelle l'explorateur de fichiers sera rattaché
     * @return Le fichier choisi par l'utilisateur, ou null s'il a annulé sa sélection
     */
    protected File ouvreFenetreDeDialogue(Stage fenetre) {
        if(MODE = MODE_LECTURE){
            return explorateurFichier.showOpenDialog(fenetre);
        }
        else if(MODE = MODE_ECRITURE){
           return  explorateurFichier.showSaveDialog(fenetre);
        }
        else{
            return null;
        }
    }


    // TODO Utiliser des setters ? ou des parametres à la methode ouvre ? Pour les extensions, le mode, le titre. Penser au problème apporté par le multithreading sur ce cas.
    /**
     * Définit les extensions disponibles lors de l'ouverture de l'explorateur de fichier.
     * @param extensions
     * @return
     */
    public OuvreurDeFichier setExtensions(List<FileChooser.ExtensionFilter> extensions){
        EXTENSIONS = extensions;
        explorateurFichier.getExtensionFilters().setAll(EXTENSIONS);
        return this;
    }

    /**
     * Définit le mode d'ouverture : OuvreurDeFichier.MODE_LECTURE ou OuvreurDeFichier.MODE_ECRITURE, pour le fichier qui sera retourné.
     * @param mode
     * @return
     */
    public OuvreurDeFichier setMode(boolean mode){
        MODE = mode;
        return this;
    }

    /**
     * Définit le titre de la boite de dialogue qui sera affichée.
     * @param titre
     * @return
     */
    public OuvreurDeFichier setTitre(String titre){
        TITRE_OUVREUR_DE_FICHIER = titre;
        explorateurFichier.setTitle(TITRE_OUVREUR_DE_FICHIER);
        return this;
    }

}
