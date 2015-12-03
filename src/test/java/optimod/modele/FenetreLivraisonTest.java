package optimod.modele;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FenetreLivraisonTest {

    @Test
    public void testCalculPCCInterne() throws Exception {
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

        List<Troncon> tr3 = new ArrayList<>();
        tr3.add(troncon4);
        tr3.add(troncon5);

        intersection3.setSortants(tr3);

        /**Intersection 4**/
        Troncon troncon6 = new Troncon(intersection5, 1, 3, "5.1");


        List<Troncon> tr4 = new ArrayList<>();
        tr4.add(troncon6);


        intersection4.setSortants(tr4);

        /**Intersection 5**/
        Troncon troncon7 = new Troncon(intersection3, 1, 1, "3.2");
        Troncon troncon8 = new Troncon(intersection6, 1, 2, "6.1");
        Troncon troncon9 = new Troncon(intersection1, 1, 4, "6.1");


        List<Troncon> tr5 = new ArrayList<>();
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


        List<Intersection> intersections = new ArrayList<>();
        intersections.add(intersection1);
        intersections.add(intersection2);
        intersections.add(intersection3);
        intersections.add(intersection4);
        intersections.add(intersection5);
        intersections.add(intersection6);


        List<Livraison> f1 = new ArrayList<>();
        f1.add(livraison3);
        f1.add(livraison5);

        FenetreLivraison fenetreLivraison1 = new FenetreLivraison(f1,8,20);

        List<Chemin> chemins = fenetreLivraison1.calculPCCInterne();

        List<Chemin> chemins2 = new ArrayList<>();
        for(Livraison depart : f1) {
            for (Livraison arrivee : f1) {
                if (!depart.equals(arrivee)) {
                    Chemin chemin = depart.calculPCC(arrivee);
                    if(chemin != null) {
                        chemins2.add(chemin); // on ajoute le plus court chemin entre depart et arrivee
                    }
                }
            }
        }

        boolean sameLists = true;
        for (Chemin c : chemins){
            boolean exist = false;
            for(Chemin c2 : chemins2){
                if(CheminTest.comparerCheminsBool(c,c2)){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                sameLists = false;
                break;
            }
        }
        assertTrue(sameLists);

    }

    @Test
    public void testCalculerPCCSuivant() throws Exception {
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

        List<Troncon> tr3 = new ArrayList<>();
        tr3.add(troncon4);
        tr3.add(troncon5);

        intersection3.setSortants(tr3);

        /**Intersection 4**/
        Troncon troncon6 = new Troncon(intersection5, 1, 3, "5.1");


        List<Troncon> tr4 = new ArrayList<>();
        tr4.add(troncon6);


        intersection4.setSortants(tr4);

        /**Intersection 5**/
        Troncon troncon7 = new Troncon(intersection3, 1, 1, "3.2");
        Troncon troncon8 = new Troncon(intersection6, 1, 2, "6.1");
        Troncon troncon9 = new Troncon(intersection1, 1, 4, "6.1");


        List<Troncon> tr5 = new ArrayList<>();
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

        List<Livraison> f1 = new ArrayList<>();
        f1.add(livraison3);
        f1.add(livraison5);
        List<Livraison> f2 = new ArrayList<>();
        f2.add(livraison);
        f2.add(livraison2);

        FenetreLivraison fenetreLivraison1 = new FenetreLivraison(f1,8,20);
        FenetreLivraison fenetreLivraison2 = new FenetreLivraison(f2,21,35);

        List<Chemin> chemins = fenetreLivraison1.calculPCCSuivant(fenetreLivraison2);

        List<Chemin> chemins2 = new ArrayList<>();
        for(Livraison depart : f1) {
            for (Livraison arrivee : f2) {
                Chemin chemin = depart.calculPCC(arrivee);
                if(chemin != null) {
                    chemins2.add(chemin); // on ajoute le plus court chemin entre depart et arrivee
                }
            }
        }

        boolean sameLists = true;
        for (Chemin c : chemins){
            boolean exist = false;
            for(Chemin c2 : chemins2){
                if(CheminTest.comparerCheminsBool(c,c2)){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                sameLists = false;
                break;
            }
        }
        assertTrue(sameLists);
    }
}