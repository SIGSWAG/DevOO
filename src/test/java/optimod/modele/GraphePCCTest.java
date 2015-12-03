package optimod.modele;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


public class GraphePCCTest {

    @Test
    public void testCalculerItineraire() throws Exception {
        Intersection intersection1 = new Intersection(0, 0, 1, null);
        Intersection intersection2 = new Intersection(0, 0, 2, null);
        Intersection intersection3 = new Intersection(0, 0, 3, null);
        Intersection intersection4 = new Intersection(0, 0, 4, null);
        Intersection intersection5 = new Intersection(0, 0, 5, null);
        Intersection intersection6 = new Intersection(0, 0, 6, null);

        /**Intersection 1**/
        Troncon troncon1 = new Troncon(intersection2, 1, 1, "2.1");
        Troncon troncon2 = new Troncon(intersection3, 1, 2, "3.1");

        List<Troncon> tr1 = new ArrayList<>();
        tr1.add(troncon1);
        tr1.add(troncon2);

        intersection1.setSortants(tr1);

        /**Intersection 2**/
        Troncon troncon3 = new Troncon(intersection4, 1, 2, "4.1");


        List<Troncon> tr2 = new ArrayList<>();
        tr2.add(troncon3);


        intersection2.setSortants(tr2);

        /**Intersection 3**/
        Troncon troncon4 = new Troncon(intersection2, 1, 4, "2.2");
        Troncon troncon5 = new Troncon(intersection4, 1, 3, "4.2");
        Troncon tronconBis = new Troncon(intersection1, 1, 9, "1.1");

        List<Troncon> tr3 = new ArrayList<>();
        tr3.add(troncon4);
        tr3.add(troncon5);
        tr3.add(tronconBis);

        intersection3.setSortants(tr3);

        /**Intersection 4**/
        Troncon troncon6 = new Troncon(intersection5, 1, 3, "5.1");


        List<Troncon> tr4 = new ArrayList<>();
        tr4.add(troncon6);


        intersection4.setSortants(tr4);

        /**Intersection 5**/
        Troncon troncon7 = new Troncon(intersection3, 1, 1, "3.2");
        Troncon troncon8 = new Troncon(intersection6, 1, 2, "6.1");
        Troncon troncon9 = new Troncon(intersection1, 1, 4, "6.2");


        List<Troncon> tr5 = new ArrayList<>();
        tr5.add(troncon7);
        tr5.add(troncon8);
        tr5.add(troncon9);

        intersection5.setSortants(tr5);
        // intersection6.setSortants(new ArrayList<Troncon>());


        Livraison livraison = new Livraison(intersection1);
        intersection1.setLivraison(livraison);

        Livraison livraison3 = new Livraison(intersection3);
        intersection3.setLivraison(livraison3);

        Livraison livraison5 = new Livraison(intersection5);
        intersection5.setLivraison(livraison5);

        Livraison livraison2 = new Livraison(intersection2);
        intersection2.setLivraison(livraison2);

        Livraison entrepot = new Livraison(intersection4);
        intersection4.setLivraison(entrepot);

        List<Livraison> f0 = new ArrayList<>();
        f0.add(entrepot);
        List<Livraison> f1 = new ArrayList<>();
        f1.add(livraison3);
        f1.add(livraison5);
        List<Livraison> f2 = new ArrayList<>();
        f2.add(livraison);
        f2.add(livraison2);
        List<Livraison> f3 = new ArrayList<>();
        f3.add(entrepot);

        FenetreLivraison fenetreLivraison0 = new FenetreLivraison(f0, 0, 8);
        FenetreLivraison fenetreLivraison1 = new FenetreLivraison(f1, 8, 20);
        FenetreLivraison fenetreLivraison2 = new FenetreLivraison(f2, 21, 35);
        FenetreLivraison fenetreLivraison3 = new FenetreLivraison(f3, 35, 50);

        List<Chemin> chemins = fenetreLivraison0.calculPCCSuivant(fenetreLivraison1);
        chemins.addAll(fenetreLivraison1.calculPCCInterne());
        chemins.addAll(fenetreLivraison1.calculPCCSuivant(fenetreLivraison2));
        chemins.addAll(fenetreLivraison2.calculPCCInterne());
        chemins.addAll(fenetreLivraison2.calculPCCSuivant(fenetreLivraison3));


        GraphePCC graphe = new GraphePCC(entrepot, chemins);
        List<Chemin> parcours = graphe.calculerItineraire();

        List<Integer> solution = new ArrayList<>();
        solution.add(4);
        solution.add(5);
        solution.add(3);
        solution.add(1);
        solution.add(2);

        int i = 0;
        for (Chemin c : parcours) {
            assertEquals((int) solution.get(i), c.getDepart().getIntersection().getAdresse());
            i++;
        }

    }


}