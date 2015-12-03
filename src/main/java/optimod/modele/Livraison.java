package optimod.modele;

import java.util.*;

/**
 * Adresse à laquelle il faut s’arrêter pour une fenêtre.
 */
public class Livraison {

    private int heureLivraison;

    private int heureDebutFenetre;

    private int heureFinFenetre;

    private Intersection intersection;

    private Chemin cheminVersSuivante;

    private Livraison precedente;

    private int idClient;

    /**
     * Constructeur de Livraison
     *
     * @param intersection l'Intersection sur laquelle se situe la Livraison.
     */
    public Livraison(Intersection intersection) {
        this.intersection = intersection;
    }

    /**
     * Constructeur Livraison
     *
     * @param intersection      l'Intersection sur laquelle se situe la Livraison.
     * @param heureDebutFenetre l'heure de début de la Fenetre de Livraison dans laquelle se situe la Livraison.
     * @param heureFinFenetre   l'heure de fin de la Fenetre de Livraison dans laquelle se situe la Livraison.
     * @param idClient          l'identifiant du client à livrer.
     */
    public Livraison(Intersection intersection, int heureDebutFenetre, int heureFinFenetre, int idClient) {
        this.intersection = intersection;
        this.heureDebutFenetre = heureDebutFenetre;
        this.heureFinFenetre = heureFinFenetre;
        this.idClient = idClient;
        this.heureLivraison = -1;
    }


    /**
     * Cette méthode permet de calculer le plus court chemin entre la livraison
     * courante et une livraison quelconque. Utilise l'algorithme de Dijkstra.
     *
     * @param destination la livraison vers laquelle on souhaite se diriger
     * @return le plus court chemin entre this et la livraison destination
     */
    public Chemin calculPCC(Livraison destination) {

        if (destination == this) {
            return null;
        }

        Hashtable<Integer, Integer> distances = new Hashtable<>(); //intersection, distance
        Hashtable<Integer, Intersection> parents = new Hashtable<>(); //parents
        PriorityQueue<DijkstraIntersection> tasBinaire = new PriorityQueue<>();//distance, intersection

        boolean destinationTrouvee = false;

        distances.put(this.intersection.getAdresse(), 0);
        tasBinaire.add(new DijkstraIntersection(0, this.intersection));
        parents.put(this.intersection.getAdresse(), this.intersection);

        while (!tasBinaire.isEmpty() && !destinationTrouvee) {

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
        if (destinationTrouvee) {
            //il faut à présent calculer le chemin
            int distanceArrivee = distances.get(destination.intersection.getAdresse());
            chemin = new Chemin();
            chemin.setDuree(distanceArrivee);
            chemin.setDepart(this);
            chemin.setArrivee(destination);
            boolean depart = false;

            List<Troncon> troncons = new ArrayList<>();

            Intersection currIntersection = destination.intersection;

            while (!depart) {
                Intersection parent = parents.get(currIntersection.getAdresse());

                if (currIntersection.getAdresse() == this.intersection.getAdresse()) {
                    depart = true;
                } else {

                    Troncon troncon = parent.getTronconVers(currIntersection);

                    troncons.add(troncon);
                }
                currIntersection = parent;

            }

            Collections.reverse(troncons);
            chemin.setTroncons(troncons);
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

    public Livraison getSuivante() {
        return this.cheminVersSuivante.getArrivee();
    }

    public int getHeureDebutFenetre() {
        return heureDebutFenetre;
    }

    public void setHeureDebutFenetre(int heureDebutFenetre) {
        this.heureDebutFenetre = heureDebutFenetre;
    }

    public int getHeureDebutFenetreHeure() {
        return heureDebutFenetre / 3600;
    }

    public int getHeureDebutFenetreMinute() {
        return (heureDebutFenetre % 3600) / 60;
    }

    public int getHeureDebutFentreSeconde() {
        return (heureDebutFenetre % 3600) % 60;
    }

    public int getHeureFinFenetre() {
        return heureFinFenetre;
    }

    public void setHeureFinFenetre(int heureFinFenetre) {
        this.heureFinFenetre = heureFinFenetre;
    }

    public int getHeureFinFenetreHeure() {
        return heureFinFenetre / 3600;
    }

    public int getHeureFinFenetreMinute() {
        return (heureFinFenetre % 3600) / 60;
    }

    public int getHeureFinFentreSeconde() {
        return (heureFinFenetre % 3600) % 60;
    }

    public boolean estEnRetard() {
        return heureLivraison > heureFinFenetre;
    }

    public int getHeure() {
        return heureLivraison / 3600;
    }

    public int getMinute() {
        return (heureLivraison % 3600) / 60;
    }

    public int getSeconde() {
        return (heureLivraison % 3600) % 60;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public boolean initeraireCalcule() {
        return heureLivraison >= 0;
    }

    private class DijkstraIntersection implements Comparable<DijkstraIntersection> {
        private int distance;
        private Intersection intersection;

        public DijkstraIntersection(int distance, Intersection intersection) {
            this.distance = distance;
            this.intersection = intersection;
        }

        public int compareTo(DijkstraIntersection autre) {
            return Double.compare(this.distance, autre.distance);
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof DijkstraIntersection) {
                DijkstraIntersection di = (DijkstraIntersection) obj;

                return di.distance == distance && di.intersection.getAdresse() == intersection.getAdresse();
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