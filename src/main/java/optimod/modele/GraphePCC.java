package optimod.modele;

import optimod.tsp.Graphe;
import optimod.tsp.TSP;
import optimod.tsp.TSP1;

import java.util.*;

public class GraphePCC implements Graphe {

    /**
     *
     */
    private Hashtable<Integer,Integer> mappingInverse;//calculatedIndex-->realIndex

    private Hashtable<Integer,List<Chemin>> cheminsParLivraison;//calculatedIndex-->chemins

    private Hashtable<Integer, Integer> vus = new Hashtable<Integer, Integer>();//reel --> calcule

    private Chemin[][] graphe;

    /**
     * Default constructor
     */
    public GraphePCC() {
    }


    private List<Chemin> chemins = new ArrayList<Chemin>();


    /**
     * @param chemins les chemins à définissants le graphe
     */
    public GraphePCC(Livraison entrepot, List<Chemin> chemins) {

        this.chemins = chemins;
        convertirLivraisonsEnSommets();
        construireGraphe();

    }

    /**
     * TODO !! Vide de sens pour l'instant
     */
    public List<Chemin> calculerItineraire() {
        List<Chemin> plusCourtParcours = new ArrayList<Chemin>();

        TSP tsp = new TSP1();

        tsp.chercheSolution(3, this);

        List<Integer> solution = new ArrayList<Integer>();

        int dureeDepuisDepart=0;
        for(int i=0;i<graphe.length-1;i++){

            int livraison = tsp.getSolution(i);
            int livraisonSuivante = tsp.getSolution(i+1);


            plusCourtParcours.add(graphe[livraison][livraisonSuivante]);

        }
        plusCourtParcours.add(graphe[tsp.getSolution(graphe.length-1)][0]); //TODO test this

        return plusCourtParcours;
    }

    /**
     * 
     */
    private void convertirLivraisonsEnSommets() {
        if(chemins!=null) {

            Iterator<Chemin> it = chemins.iterator();
            int i = 0;
            while (it.hasNext()) {

                Chemin chemin = it.next();
                List<Chemin> cheminsCourants;
                int adresse = chemin.getDepart().getIntersection().getAdresse();//adresse du depart

                if (vus.get(adresse) == null) { //on passe pour la première fois sur la livraison

                    cheminsCourants = new ArrayList<Chemin>();

                    vus.put(adresse, i);
                    cheminsParLivraison.put(i, cheminsCourants);
                    i++;

                } else {
                    int index = vus.get(adresse);
                    cheminsCourants = cheminsParLivraison.get(index); //on récupère la liste de chemins
                }
                cheminsCourants.add(chemin);
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

        }

    }

    /**
     * @param  n ???
     */
    private void retrouverItinéraire(List<Integer> n) {
        // TODO implement here
    }

    public int getCout(int i, int j) {

        int len= graphe.length;

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

    // à virer ???
    public void setChemins(List<Chemin> chemins) {
        this.chemins = chemins;
    }
}