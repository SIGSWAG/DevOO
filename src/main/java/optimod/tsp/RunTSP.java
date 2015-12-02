package optimod.tsp;

public class RunTSP {

    public static void main(String[] args) {
        TSP tsp = new TSP3();
        for (int nbSommets = 8; nbSommets <= 28; nbSommets += 2) {
            System.out.println("Graphes de " + nbSommets + " sommets :");
            Graphe g = new GrapheComplet(nbSommets);
            long tempsDebut = System.currentTimeMillis();
            tsp.chercheSolution(60000, g);
            System.out.print("Solution de longueur " + nbSommets + " de long" + tsp.getCoutSolution() + " trouvee en " + (System.currentTimeMillis() - tempsDebut) + "ms : ");
            for (int i = 0; i < nbSommets; i++) {
                System.out.print(tsp.getSolution(i) + " ");
            }
        }
    }
}
