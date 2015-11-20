package optimod.es.xml;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by (PRO) Loïc Touzard on 18/11/2015.
 */
public class OuvreurDeFichierXML {

    private static OuvreurDeFichierXML instance = new OuvreurDeFichierXML();

    public static OuvreurDeFichierXML getInstance() {
        return instance;
    }

    private FileChooser selecteurDeFichier = new FileChooser();

    /**
     *  Constructeur par défaut
     */
    private OuvreurDeFichierXML() {
        this.selecteurDeFichier.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers XML","*.xml"));
    }

    /**
     *
     * @param lecture boolean, true pour ouvrir un fichier (lecture), false pour sauver un fichier (ecriture)
     * @return xml File
     * @throws ExceptionXML
     */
    public File ouvre(boolean lecture) throws ExceptionXML{
        File xml = null;
        if (lecture)
            xml = selecteurDeFichier.showOpenDialog(null);
        else
            xml = selecteurDeFichier.showSaveDialog(null);
        if (xml == null)
            throw new ExceptionXML("Probleme a l'ouverture du fichier");
        return xml;
    }
}
