package optimod.modele;

import javafx.util.Pair;
import optimod.tsp.Graphe;
import optimod.tsp.GrapheComplet;
import optimod.tsp.TSP;
import optimod.tsp.TSP1;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Thibault on 19/11/2015.
 */
public class MainTest {


    public static void main(String[] args) {

        /** TSP tsp = new TSP1();
         for (int nbSommets = 8; nbSommets <= 16; nbSommets += 2){
         System.out.println("Graphes de "+nbSommets+" sommets :");
         Graphe g = new GrapheComplet(nbSommets);
         long tempsDebut = System.currentTimeMillis();
         tsp.chercheSolution(60000, g);
         System.out.print("Solution de longueur "+tsp.getCoutSolution()+" trouvee en "
         +(System.currentTimeMillis() - tempsDebut)+"ms : ");
         for (int i=0; i<nbSommets; i++)
         System.out.print(tsp.getSolution(i)+" ");
         System.out.println();
         }*/

        /**
         * Test Case 1
         */
        Intersection intersection1 = new Intersection(0,0,1,null);
        Intersection intersection2 = new Intersection(0,0,2,null);
        Intersection intersection3 = new Intersection(0,0,3,null);
        Intersection intersection4 = new Intersection(0,0,4,null);
        Intersection intersection5 = new Intersection(0,0,5,null);
        Intersection intersection6 = new Intersection(0,0,6,null);

        /**Intersection 1**/
        Troncon troncon1 = new Troncon(intersection2,1,1);
        Troncon troncon2 = new Troncon(intersection3,1,2);

        List<Troncon> tr1 = new ArrayList<Troncon>();
        tr1.add(troncon1);
        tr1.add(troncon2);

        intersection1.setSortants(tr1);

        /**Intersection 2**/
        Troncon troncon3 = new Troncon(intersection4,1,6);


        List<Troncon> tr2 = new ArrayList<Troncon>();
        tr2.add(troncon3);


        intersection2.setSortants(tr2);

        /**Intersection 3**/
        Troncon troncon4 = new Troncon(intersection2,1,4);
        Troncon troncon5 = new Troncon(intersection4,1,3);


        List<Troncon> tr3 = new ArrayList<Troncon>();
        tr3.add(troncon4);
        tr3.add(troncon5);

        intersection3.setSortants(tr3);

        /**Intersection 4**/
        Troncon troncon6 = new Troncon(intersection5,1,3);


        List<Troncon> tr4 = new ArrayList<Troncon>();
        tr4.add(troncon6);


        intersection4.setSortants(tr4);

       /**Intersection 5**/
        Troncon troncon7 = new Troncon(intersection3,1,1);
        Troncon troncon8 = new Troncon(intersection6,1,2);


        List<Troncon> tr5 = new ArrayList<Troncon>();
        tr5.add(troncon7);
        tr5.add(troncon8);

        intersection5.setSortants(tr5);


        Livraison livraison  = new Livraison(intersection1);
        intersection1.setLivraison(livraison);

        Livraison livraison3  = new Livraison(intersection3);
        intersection3.setLivraison(livraison3);

        Livraison livraison5  = new Livraison(intersection5);
        intersection5.setLivraison(livraison5);



        List<Chemin> chemins = new ArrayList<Chemin>();
        chemins.add(livraison.calculPCC(livraison3));
        chemins.add(livraison.calculPCC(livraison5));
       // chemins.add(livraison3.calculPCC(livraison));
        chemins.add(livraison3.calculPCC(livraison5));
        //chemins.add(livraison5.calculPCC(livraison));
        chemins.add(livraison5.calculPCC(livraison3));

        GraphePCC graphePCC = new GraphePCC(livraison, chemins);
        List<Chemin> cheminsPCC = graphePCC.calculerItineraire();

        for(Chemin chemin : cheminsPCC){

            System.out.println(" dep "+chemin.getDepart().getIntersection().getAdresse()+" arrivee "+chemin.getArrivee().getIntersection().getAdresse());
        }



    }





}
