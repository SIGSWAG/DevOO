package optimod.modele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FenetreLivraison {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int heureDebut;

    private int heureFin;

    private List<Livraison> livraisons = new ArrayList<>();

    public FenetreLivraison(List<Livraison> livraisons, int heureDebut, int heureFin) {
        this.livraisons = livraisons;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    /**
     * Contrat : Calcule les plus courts chemins entre toutes les livraisons présentes au sein de la fenêtere
     */
    public List<Chemin> calculPCCInterne() {
        List<Chemin> chemins = new ArrayList<>();
        for(Livraison depart : livraisons) {
            for (Livraison arrivee : livraisons) {
                if (!depart.equals(arrivee)) {
                    logger.debug("dep {} arr {}", depart.getIntersection().getAdresse(), arrivee.getIntersection().getAdresse());
                    Chemin chemin = depart.calculPCC(arrivee);
                    if(chemin != null) {
                        chemins.add(chemin); // on ajoute le plus court chemin entre depart et arrivee
                    }
                }
            }
        }
        return chemins;
    }

    /**
     * Contrat : Calcule les plus courts chemins depuis chaque livraison de la fenêtre actuelle vers toutes les livraisons de la fenêtre fdl
     * @param fdl : fenêtre de livraison suivante
     */
    public List<Chemin> calculPCCSuivant(FenetreLivraison fdl) {
        List<Chemin> chemins = new ArrayList<>();
        List<Livraison> livraisonsSuivantes = fdl.getLivraisons();
        for(Livraison depart : livraisons) {
            for (Livraison arrivee : livraisonsSuivantes) {
                logger.debug("dep {} arr {}", depart.getIntersection().getAdresse(), arrivee.getIntersection().getAdresse());
                Chemin chemin = depart.calculPCC(arrivee);
                if(chemin != null) {
                    chemins.add(chemin); // on ajoute le plus court chemin entre depart et arrivee
                }
            }
        }
        return chemins;
    }

    public int getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(int heureDebut) {
        this.heureDebut = heureDebut;
    }

    public int getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(int heureFin) {
        this.heureFin = heureFin;
    }

    public List<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public int getHeureDebutHeure() {
        return heureDebut / 3600;
    }

    public int getHeureDebutMinute() {
        return (heureDebut % 3600) / 60;
    }

    public int getHeureDebutSeconde() { return (heureDebut % 3600) % 60; }

    public int getHeureFinHeure() {
        return heureFin / 3600;
    }

    public int getHeureFinMinute() {
        return (heureFin % 3600) / 60;
    }

    public int getHeureFinSeconde() { return (heureFin % 3600) % 60; }


}