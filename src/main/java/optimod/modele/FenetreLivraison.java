package optimod.modele;

import java.util.*;

public class FenetreLivraison {

    private int heureDebut;

    private int heureFin;

    private List<Livraison> livraisons = new ArrayList<Livraison>();

    public FenetreLivraison(List<Livraison> livraisons, int heureDebut, int heureFin) {
        this.livraisons = livraisons;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    /**
     * Contrat : Calcule les plus courts chemins entre toutes les livraisons présentes au sein de la fenêtere
     */
    public List<Chemin> calculPCCInterne() {
        List<Chemin> chemins = new ArrayList<Chemin>();
        for(Livraison depart : livraisons) {
            for (Livraison arrivee : livraisons) {
                if (!depart.equals(arrivee)) {
                    System.out.println("dep "+depart.getIntersection().getAdresse()+" arr "+arrivee.getIntersection().getAdresse());
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
        List<Chemin> chemins = new ArrayList<Chemin>();
        List<Livraison> livraisonsSuivantes = fdl.getLivraisons();
        for(Livraison depart : livraisons) {
            for (Livraison arrivee : livraisonsSuivantes) {
                System.out.println("dep "+depart.getIntersection().getAdresse()+" arr "+arrivee.getIntersection().getAdresse());
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
}