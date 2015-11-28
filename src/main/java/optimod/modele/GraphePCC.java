package optimod.modele;

import optimod.tsp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GraphePCC implements Graphe {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Hashtable<Integer,List<Chemin>> cheminsParLivraison;//calculatedIndex-->chemins

    private Hashtable<Integer, Integer> vus = new Hashtable<Integer, Integer>();//reel --> calcule

    private Chemin[][] graphe;

    private Livraison entrepot;

    private TSP tsp;

    private List<Chemin> chemins = new ArrayList<Chemin>();

    /**
     * Default constructor
     */
    public GraphePCC() {
        this.tsp = new TSP3();
    }

    /**
     * @param chemins les chemins à définissants le graphe
     */
    public GraphePCC(Livraison entrepot, List<Chemin> chemins) {
        this();
        this.chemins = chemins;
        this.entrepot = entrepot;
        convertirLivraisonsEnSommets();
        construireGraphe();
    }


    public List<Chemin> calculerItineraire() {
        List<Chemin> plusCourtParcours = new ArrayList<>();

        tsp.chercheSolution(60000, this);

        System.out.println("fin du calcul");


        for(int i=0;i<graphe.length-1;i++){
            if(tsp == null) {
                logger.error("TSP nul {}", i);
                return null;
            }
            int livraison = tsp.getSolution(i);
            int livraisonSuivante = tsp.getSolution(i+1);


            plusCourtParcours.add(graphe[livraison][livraisonSuivante]);

        }
        plusCourtParcours.add(graphe[tsp.getSolution(graphe.length-1)][0]);//retour a l'entrepot

        return plusCourtParcours;
    }

    /**
     *
     */
    private void convertirLivraisonsEnSommets() {
        if(chemins!=null) {


            cheminsParLivraison = new Hashtable<>();
            Iterator<Chemin> it = chemins.iterator();
            int i = 1;
            while (it.hasNext()) {

                Chemin chemin = it.next();
                if (chemin != null) {

                    List<Chemin> cheminsCourants; //chemins pour la livraison courante
                    int adresse = chemin.getDepart().getIntersection().getAdresse();//adresse du depart

                    if (vus.get(adresse) == null) { //on passe pour la première fois sur la livraison

                        int index = i;
                        if (adresse == entrepot.getIntersection().getAdresse()) { //cas de l'entrepot, toujours à zero
                            index = 0;
                        }


                        cheminsCourants = new ArrayList<Chemin>();
                        vus.put(adresse, index);
                        cheminsParLivraison.put(index, cheminsCourants);
                        if (index != 0) {
                            i++;
                        }

                    } else {
                        int index = vus.get(adresse);
                        cheminsCourants = cheminsParLivraison.get(index); //on récupère la liste de chemins
                    }
                    cheminsCourants.add(chemin);
                }
            }
        }
    }

    /**
     *
     */
    private void construireGraphe(){
        if(cheminsParLivraison!=null) {


            int tailleGraphe = cheminsParLivraison.size();


            graphe = new Chemin[tailleGraphe][tailleGraphe];
            logger.info("Taille du graphe {}", graphe.length);
            for(int i=0;i<tailleGraphe;i++){
                for(int j=0;j<tailleGraphe;j++){
                    graphe[i][j]=null;
                }
            }

            for(int i=0;i < cheminsParLivraison.size(); i++){
                List<Chemin> cheminsCourants = cheminsParLivraison.get(i);

                Iterator<Chemin> it = cheminsCourants.iterator();

                while(it.hasNext()){
                    Chemin chemin = it.next();
                    int depart = i;
                    int arrivee = vus.get(chemin.getArrivee().getIntersection().getAdresse());

                    graphe[depart][arrivee]=chemin;

                }

            }

            for(int i=0;i<tailleGraphe;i++){
                for(int j=0;j<tailleGraphe;j++){
                    if( graphe[i][j]==null){
                        System.out.print("N, ");
                    }else {
                        System.out.print(graphe[i][j].getDuree()+", ");
                    }
                }
                System.out.println("");
            }
        }


    }

    /**
     * @param  n ???
     */
    private void retrouverItinéraire(List<Integer> n) {
        // TODO implement here
    }

    public int getCout(int i, int j) {


        if(i<0 || i>=graphe.length || j<0 || j>=graphe.length){
            return -1;
        }

        return graphe[i][j] == null ? -1 : graphe[i][j].getDuree();
    }

    public boolean estArc(int i, int j) {

        if(i<0 || i>=graphe.length || j<0 || j>=graphe.length){
            return false;
        }

        return graphe[i][j] != null;
    }

    public int getNbSommets() {
        return graphe.length;
    }

    public List<Chemin> getChemins() {
        return chemins;
    }

    public void setChemins(List<Chemin> chemins) {
        this.chemins = chemins;
    }
}