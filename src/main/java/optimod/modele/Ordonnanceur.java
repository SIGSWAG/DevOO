package optimod.modele;

public class Ordonnanceur {

    private DemandeLivraisons demandeLivraisons;

    private Plan plan;

    /**
     * Default constructor
     */
    public Ordonnanceur() {
        plan = new Plan();
        demandeLivraisons = new DemandeLivraisons(plan);
    }

    /**
     *
     */
    public void chargerPlan() {
        plan.chargerPlan();
    }

    /**
     * @param adresse l'identifiant de l'adresse à trouver
     * @return l'intersection correspondant à l'adresse
     */
    public Intersection trouverIntersection(int adresse) {
        return plan.trouverIntersection(adresse);
    }

    /**
     * contrat : trouve la 1ere intersection dans le cercle, même si il y en a plusieurs (attention au radius trop grand)
     * @param x la coordonnée x du cercle dans lequel trouver l'intersection
     * @param y la coordonnée y du cercle dans lequel trouver l'intersection
     * @param rayon le rayon du cercle dans lequel trouver l'intersection
     * @return la 1ere intersection dans le cercle, même si il y en a plusieurs
     */
    public Intersection trouverIntersection(int x, int y, int rayon) {
        return plan.trouverIntersection(x,y,rayon);
    }

    /**
     *
     */
    public void chargerDemandeLivraison() {
        demandeLivraisons.chargerDemandeLivraison();
    }

    /**
     * 
     */
    public void calculerItineraire() {
        demandeLivraisons.calculerItineraire();
    }

    /**
     * Contrat : ajoute la livraison sur l'intersection donnée en paramètre et AVANT la livraison donnée en paramètre
     * @param intersection l'intersection sur laquelle on ajoute la livraison
     * @param livr la Livraison avant laquelle on ajoute la nouvelle Livraison
     */
    public void ajouterLivraison(Intersection intersection, Livraison livr) {
        demandeLivraisons.ajouterLivraison(intersection, livr);
    }

    /**
     * supprime la livraison en parametre et recalcule l'itinéraire, nottament les horaires prévues d'arrivées
     * @param livr la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        demandeLivraisons.supprimerLivraison(livr);
    }

    /**
     * echange temporellement deux livraisons livr1 et livr2 et recalcule les PCC et les horaires
     * @param livr1 la 1ere livraison à échanger
     * @param livr2 la 2nde livraison à échanger
     */
    public void echangerLivraison(Livraison livr1, Livraison livr2) {
        demandeLivraisons.echangerLivraison(livr1, livr2);
    }

    public DemandeLivraisons getDemandeLivraisons() {
        return demandeLivraisons;
    }

    public void setDemandeLivraisons(DemandeLivraisons demandeLivraisons) {
        this.demandeLivraisons = demandeLivraisons;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}