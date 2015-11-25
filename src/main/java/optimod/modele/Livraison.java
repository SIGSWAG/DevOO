package optimod.modele;

import javafx.util.Pair;
import optimod.modele.Intersection;

import java.util.*;

public class Livraison {

    private int heureLivraison;

    private int heureDebutFenetre;

    private int heureFinFenetre;

    private Intersection intersection;

    private Chemin cheminVersSuivante;

    private Livraison precedente;


    public Livraison(Intersection intersection) {
        this.intersection = intersection;
    }

    public Livraison(Intersection intersection, int heureFinFenetre, int heureDebutFenetre) {
        this.intersection = intersection;
        this.heureFinFenetre = heureFinFenetre;
        this.heureDebutFenetre = heureDebutFenetre;
    }

    /**
     *
     * Cette méthode permet de calculer le plus court chemin entre la livraison
     * courante et une livraison quelconque. Utilise l'algorithme de Dijkstra.
     * @param destination la livraison vers laquelle on souhaite se diriger
     * @return le plus court chemin entre this et la livraison destination
     *
     *
     *
     */
    public Chemin calculPCC(Livraison destination) {

        Hashtable<Integer,Integer> distances = new Hashtable<Integer,Integer>(); //intersection, distance
        Hashtable<Integer,Intersection> parents = new Hashtable<Integer, Intersection>(); //parents
        PriorityQueue<DijkstraIntersection> tasBinaire = new PriorityQueue<DijkstraIntersection>();//distance, intersection

        boolean destinationTrouvee = false;

        distances.put(this.intersection.getAdresse(),0);
        tasBinaire.add(new DijkstraIntersection(0, this.intersection));
        parents.put(this.intersection.getAdresse(),this.intersection);

        while(!tasBinaire.isEmpty() && !destinationTrouvee) {

            DijkstraIntersection currDi = tasBinaire.poll(); // on prend l'intersection a la plus petite distance


            int currDist = currDi.getDistance();
            Intersection currIntersection = currDi.getIntersection();




            if (currIntersection.getAdresse() == destination.intersection.getAdresse()) {

                destinationTrouvee = true;
            } else {


                if (currIntersection.getSortants() != null) {
                    Iterator<Troncon> it = currIntersection.getSortants().iterator();


                    while (it.hasNext()) {

                        Troncon troncon = it.next();
                        int next = troncon.getArrivee().getAdresse(); //adresse de l'arrivee
                        Intersection intersection = troncon.getArrivee();
                        int w = troncon.getDuree();

                        Integer dist = distances.get(next);

                        if (dist == null || dist > w + currDist) { //relachement de l'arc

                            if (dist != null) {

                                DijkstraIntersection di = new DijkstraIntersection(dist, intersection);
                                tasBinaire.remove(di);
                            }


                            tasBinaire.add(new DijkstraIntersection(w + currDist, intersection));//mise a jour tas binaire
                            distances.put(next, w + currDist); // mise a jour distances
                            parents.put(next, currIntersection); //mise a jour des parents

                        }

                    }
                }
            }

        }
        Chemin chemin = null;
        if(destinationTrouvee) {
            //il faut à présent calculer le chemin
            int distanceArrivee = distances.get(destination.intersection.getAdresse());
            chemin = new Chemin();
            chemin.setDuree(distanceArrivee);
            chemin.setDepart(this);
            chemin.setArrivee(destination);
            boolean depart = false;

            List<Intersection> intersections = new ArrayList<Intersection>();


            Intersection currIntersection = destination.intersection;


            while (!depart) {
                currIntersection = parents.get(currIntersection.getAdresse());

                if (currIntersection.getAdresse() == this.intersection.getAdresse()) {
                    depart = true;
                } else {

                    intersections.add(currIntersection);
                }

            }

            Collections.reverse(intersections);
            chemin.setIntersections(intersections);
        }


        return chemin;
    }

    public int getHeureLivraison() {
        return heureLivraison;
    }

    public void setHeureLivraison(int heureLivraison) {
        this.heureLivraison = heureLivraison;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    public Chemin getCheminVersSuivante() {
        return cheminVersSuivante;
    }

    public void setCheminVersSuivante(Chemin cheminVersSuivante) {
        this.cheminVersSuivante = cheminVersSuivante;
    }

    public Livraison getPrecedente() {
        return precedente;
    }

    public void setPrecedente(Livraison precedente) {
        this.precedente = precedente;
    }

    public Livraison getSuivante(){
        return this.cheminVersSuivante.getArrivee();
    }

    public int getHeureDebutFenetre() {
        return heureDebutFenetre;
    }

    public void setHeureDebutFenetre(int heureDebutFenetre) {
        this.heureDebutFenetre = heureDebutFenetre;
    }

    public int getHeureFinFenetre() {
        return heureFinFenetre;
    }

    public void setHeureFinFenetre(int heureFinFenetre) {
        this.heureFinFenetre = heureFinFenetre;
    }

    public boolean estEnRetard(){
        return heureLivraison > heureFinFenetre;
    }

    public int getHeure() {
        return heureLivraison / 3600;
    }

    public int getMinute() {
        return (heureLivraison % 3600) / 60;
    }

    public int getSeconde() { return (heureLivraison % 3600) % 60; }

    private class DijkstraIntersection implements Comparable<DijkstraIntersection> {
        private int distance;
        private Intersection intersection;

        public DijkstraIntersection(int distance, Intersection intersection){
            this.distance = distance;
            this.intersection = intersection;
        }

        public int compareTo(DijkstraIntersection autre) {
            return Double.compare(this.distance, autre.distance);
        }
        @Override
        public boolean equals(Object obj) {

            if(obj instanceof DijkstraIntersection){
                DijkstraIntersection di = (DijkstraIntersection) obj;

                return di.distance==distance && di.intersection.getAdresse() == intersection.getAdresse();
            }
            return false;
        }


        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public Intersection getIntersection() {
            return intersection;
        }

        public void setIntersection(Intersection intersection) {
            this.intersection = intersection;
        }
    }


}