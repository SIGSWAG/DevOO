package optimod.modele;

import javafx.stage.Stage;
import optimod.es.xml.DeserialiseurXML;

import java.util.*;

public class DemandeLivraison {

    private List<Chemin> itineraire;

    private Plan plan;

    private List<FenetreLivraison> fenetres;

    private Livraison entrepot;

    /**
     * Default constructor
     */
    public DemandeLivraison(Plan pl) {
        this.plan = pl;
        this.itineraire = new ArrayList<Chemin>();
        this.fenetres = new ArrayList<FenetreLivraison>();
    }


    public void chargerDemandeLivraison() {
        // TODO implement here

        try {
            DeserialiseurXML.INSTANCE.chargerDemandeLivraison(this);
        }
        catch( Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * récupère tous les chemins entres les intersections de chaque fenetre horaire et d'une fenetre horaire vers la suivante
     * c'est à dire créé le graphe dans lequel on veut trouver le plus court chemin.
     * Puis appelle la méthode de GraphePCC permettant de trouver le + court parcours dans ce graphe.
     */
    public void calculerItineraire() {
        List<Chemin> graphe = new ArrayList<Chemin>();
        // toutes les fenetres sauf la derniere
        for(int i = 0 ; i < fenetres.size() - 1 ; i++){
            FenetreLivraison fenetre = fenetres.get(i);
            graphe.addAll(fenetre.calculPCCInterne());
            graphe.addAll(fenetre.calculPCCSuivant(fenetres.get(i+1)));
        }
        graphe.addAll(fenetres.get(fenetres.size()-1).calculPCCInterne());
        GraphePCC leGrapheAResoudre = new GraphePCC(entrepot,graphe);
        this.itineraire = leGrapheAResoudre.calculerItineraire();
    }

    /**
     * met à jour toutes les horaires de livraison prévues à partir d'une livraison donnée et jusqu'à l'entrepot
     * @param livr la livraison à partir de laquelle on recalcule les heures
     */
    private void mettreAJourLesHeuresAPartirDe(Livraison livr) {
        while(!livr.equals(entrepot)){
            livr.setHeureLivraison(livr.getPrecedente().getHeureLivraison() + livr.getPrecedente().getCheminVersSuivante().getDuree());
            livr = livr.getSuivante();
        }
    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     * @param intersection l'intersection sur laquelle on ajoute la livraison
     * @param livr la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Intersection intersection, Livraison livr) {
        Livraison nouvelleLivraison = new Livraison(intersection);
        nouvelleLivraison.setPrecedente(livr.getPrecedente());
        livr.setPrecedente(nouvelleLivraison);
        Chemin nouveauPCC1 = nouvelleLivraison.getPrecedente().calculPCC(nouvelleLivraison);
        nouvelleLivraison.getPrecedente().setCheminVersSuivante(nouveauPCC1);
        Chemin nouveauPCC2 = nouvelleLivraison.calculPCC(livr);
        nouvelleLivraison.setCheminVersSuivante(nouveauPCC2);
        mettreAJourLesHeuresAPartirDe(nouvelleLivraison);
    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        if(livr == entrepot){
            System.out.println("erreur, action impossible sur l'entrepot");
            return;
        }
        livr.getSuivante().setPrecedente(livr.getPrecedente());
        Chemin nouveauPCC = livr.getPrecedente().calculPCC(livr.getSuivante());
        livr.getPrecedente().setCheminVersSuivante(nouveauPCC);
        mettreAJourLesHeuresAPartirDe(livr.getPrecedente());
        livr.getIntersection().setLivraison(null);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        if(livr1 == entrepot || livr2 == entrepot){
            System.out.println("erreur, action impossible sur l'entrepot");
            return;
        }
        Livraison livr1PrecTemp = livr1.getPrecedente();
        Livraison livr1SuivTemp = livr1.getSuivante();

        livr1.setPrecedente(livr2.getPrecedente());
        livr1.calculPCC(livr2.getSuivante());
        livr1.getPrecedente().calculPCC(livr1);

        livr2.setPrecedente(livr1PrecTemp);
        livr2.calculPCC(livr1SuivTemp);
        livr2.getPrecedente().calculPCC(livr2);

        if(livr1.getHeureLivraison() < livr2.getHeureLivraison())
            mettreAJourLesHeuresAPartirDe(livr1);
        else
            mettreAJourLesHeuresAPartirDe(livr2);
    }

    /**
     * réalloue tous les attributs à des attributs vides
     * Supprime tous les liens qu'ont les intersections vers les livraisons existantes
     */
    public void reset(){
        itineraire = new ArrayList<Chemin>();

        // suppression de tous les liens Intersection -> Livraison
        for (FenetreLivraison f : fenetres){
            for (Livraison l : f.getLivraisons()){
                l.getIntersection().setLivraison(null);
            }
        }

        fenetres = new ArrayList<FenetreLivraison>();

        if(entrepot != null){
            entrepot.getIntersection().setLivraison(null);
            entrepot = null;
        }
    }

    public List<Chemin> getItineraire() {
        return itineraire;
    }

    public void setItineraire(List<Chemin> itineraire) {
        this.itineraire = itineraire;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public List<FenetreLivraison> getFenetres() {
        return fenetres;
    }

    public void setFenetres(List<FenetreLivraison> fenetres) {
        this.fenetres = fenetres;
    }

    public Livraison getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Livraison entrepot) {
        this.entrepot = entrepot;
    }
}