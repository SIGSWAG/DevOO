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
        initGraphe();
    }


    public static void initGraphe(){
        /**
         * Test Case 1
         */

        Ordonnanceur ordonnanceur = new Ordonnanceur();

        Intersection intersection1 = new Intersection(0, 0, 1, null);
        Intersection intersection2 = new Intersection(0, 0, 2, null);
        Intersection intersection3 = new Intersection(0, 0, 3, null);
        Intersection intersection4 = new Intersection(0, 0, 4, null);
        Intersection intersection5 = new Intersection(0, 0, 5, null);
        Intersection intersection6 = new Intersection(0, 0, 6, null);

        /**Intersection 1**/
        Troncon troncon1 = new Troncon(intersection2, 1, 1, "2.1");
        Troncon troncon2 = new Troncon(intersection3, 1, 2, "3.1");

        List<Troncon> tr1 = new ArrayList<Troncon>();
        tr1.add(troncon1);
        tr1.add(troncon2);

        intersection1.setSortants(tr1);

        /**Intersection 2**/
        Troncon troncon3 = new Troncon(intersection4, 1, 2, "4.1");


        List<Troncon> tr2 = new ArrayList<Troncon>();
        tr2.add(troncon3);


        intersection2.setSortants(tr2);

        /**Intersection 3**/
        Troncon troncon4 = new Troncon(intersection2, 1, 4, "2.2");
        Troncon troncon5 = new Troncon(intersection4, 1, 3, "4.2");

        List<Troncon> tr3 = new ArrayList<Troncon>();
        tr3.add(troncon4);
        tr3.add(troncon5);

        intersection3.setSortants(tr3);

        /**Intersection 4**/
        Troncon troncon6 = new Troncon(intersection5, 1, 3, "5.1");


        List<Troncon> tr4 = new ArrayList<Troncon>();
        tr4.add(troncon6);


        intersection4.setSortants(tr4);

        /**Intersection 5**/
        Troncon troncon7 = new Troncon(intersection3, 1, 1, "3.2");
        Troncon troncon8 = new Troncon(intersection6, 1, 2, "6.1");
        Troncon troncon9 = new Troncon(intersection1, 1, 4, "6.1");


        List<Troncon> tr5 = new ArrayList<Troncon>();
        tr5.add(troncon7);
        tr5.add(troncon8);
        tr5.add(troncon9);

        intersection5.setSortants(tr5);
       // intersection6.setSortants(new ArrayList<Troncon>());


        Livraison livraison  = new Livraison(intersection1);
        intersection1.setLivraison(livraison);

        Livraison livraison3  = new Livraison(intersection3);
        intersection3.setLivraison(livraison3);

        Livraison livraison5  = new Livraison(intersection5);
        intersection5.setLivraison(livraison5);

        Livraison livraison2 = new Livraison(intersection2);
        intersection2.setLivraison(livraison2);


        List<Intersection> intersections = new ArrayList<Intersection>();
        intersections.add(intersection1);
        intersections.add(intersection2);
        intersections.add(intersection3);
        intersections.add(intersection4);
        intersections.add(intersection5);
        intersections.add(intersection6);

        ordonnanceur.getPlan().setIntersections(intersections);
        ordonnanceur.getDemandeLivraison().setEntrepot(livraison);

        List<FenetreLivraison> fenetres = new ArrayList<FenetreLivraison>();


        List<Livraison> entrep = new ArrayList<Livraison>();
        entrep.add(livraison);
        List<Livraison> f1 = new ArrayList<Livraison>();
        f1.add(livraison3);
        f1.add(livraison5);
        List<Livraison> f2 = new ArrayList<Livraison>();
        f2.add(livraison2);


        FenetreLivraison fenetreLivraison1 = new FenetreLivraison(f1,8,20);
        FenetreLivraison fenetreLivraison2 = new FenetreLivraison(f2,5,7);
        livraison2.setHeureDebutFenetre(5);
        livraison2.setHeureFinFenetre(7);

        livraison3.setHeureDebutFenetre(8);
        livraison3.setHeureFinFenetre(20);
        livraison5.setHeureDebutFenetre(8);
        livraison5.setHeureFinFenetre(20);
        fenetres.add(fenetreLivraison2);
        fenetres.add(fenetreLivraison1);

        ordonnanceur.getDemandeLivraison().setFenetres(fenetres);





        DemandeLivraison dl = ordonnanceur.getDemandeLivraison();
        dl.calculerItineraire();

        for(Chemin chemin : dl.getItineraire()){

            System.out.println("from "+chemin.getDepart().getIntersection().getAdresse()+" to "+chemin.getArrivee().getIntersection().getAdresse()+" arrivee a "+chemin.getArrivee().getHeureLivraison());
            if(chemin.getArrivee().estEnRetard()){
                System.out.println(chemin.getArrivee().getIntersection().getAdresse()+" est en retard ");
            }

        }





    }


}
