package optimod.es.txt;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import optimod.modele.Chemin;
import optimod.modele.Livraison;
import optimod.modele.Troncon;
import optimod.vue.es.OuvreurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de générer la feuille de route au format HTML
 */
public enum GenerateurDeFeuilleDeRoute { // Singleton
    INSTANCE;
    private List<FileChooser.ExtensionFilter> extensions = new ArrayList<>();
    private Stage fenetre;

    GenerateurDeFeuilleDeRoute() {
        extensions.add(new FileChooser.ExtensionFilter("HTML (*.html)", "*.html"));
        extensions.add(new FileChooser.ExtensionFilter("Toute extension (*.*)", "*.*"));
    }

    /**
     * Genere la feuille de route dasn un fichier HTML
     *
     * @param entrepot             L'entrepot de depart et d'arrivee de la demande de livraison
     * @param heureDebutItineraire l'heure de debut de l'itineraire, au depart de l'entrepot
     * @param itineraire           la liste des chemins à suivre pour effectuer la demande de livraison
     * @param tempsArret           le temps d'attente à une livraison
     * @return true si le fichier à bien ete mis à jour, false sinon.
     */
    public boolean genererFeuilleDeRoute(Livraison entrepot, int heureDebutItineraire, List<Chemin> itineraire, int tempsArret) throws IOException {
        File fichier = OuvreurDeFichier.INSTANCE.setExtensions(this.extensions).setMode(OuvreurDeFichier.MODE_ECRITURE).setTitre("Selectionner le fichier où sauvegarder la feuille de route").ouvre(fenetre);
        if (fichier == null) {
            return false;
        }
        PrintStream fluxFichier = new PrintStream(fichier);

        int etape = 1;
        int distanceTotale = 0;
        int dureeChemin;
        int heureDerniereLivraison = heureDebutItineraire;
        fluxFichier.println("<html>\n" +
                "<head>\n" +
                "<title>\n" +
                "Feuille de route\n" +
                "</title>\n" +
                "<style>" + recupererCSS() + "</style>" +
                "<link rel='stylesheet' type='text/css' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class='container'>\n" +
                "    <div class='page-header'>\n" +
                "        <h1 id='timeline'>Feuille de route</h1>\n" +
                "    </div>\n" +
                "    <ul class='timeline'>");
        fluxFichier.println("<li class='timeline-inverted'>\n" +
                "          <div class='timeline-badge warning'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                "          <div class='timeline-panel'>\n" +
                "            <div class='timeline-heading'>\n" +
                "              <h4 class='timeline-title'>Depart de l'entrepot</h4>\n" +
                "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape++, heureDebutItineraire) + "</small></p>\n" +
                "            </div>\n" +
                "            <div class='timeline-body'>\n" +
                "              <p> Depart de l'Entrepot : (" + entrepot.getIntersection().getAdresse() + ")" + "</p>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </li>");
        for (Chemin chemin : itineraire) {
            dureeChemin = 0;
            Troncon rueCourante = chemin.getTroncons().get(0);

            double distanceRue = 0;
            int dureeRue = 0;
            for (Troncon troncon : chemin.getTroncons()) {
                if (troncon.getNom().equals(rueCourante.getNom())) {
                    distanceRue += troncon.getLongueur();
                    dureeRue += troncon.getDuree();
                } else {
                    fluxFichier.println("<li>\n" +
                            "          <div class='timeline-badge success'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                            "          <div class='timeline-panel'>\n" +
                            "            <div class='timeline-heading'>\n" +
                            "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                            "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape++, heureDerniereLivraison + dureeChemin) + " </small></p>\n" +
                            "            </div>\n" +
                            "            <div class='timeline-body'>\n" +
                            "              <p> Prendre la rue " + rueCourante.getNom() + " et continuer sur " + (int) distanceRue + "m" + "</p>\n" +
                            "            </div>\n" +
                            "          </div>\n" +
                            "        </li>");
                    distanceTotale += distanceRue;
                    dureeChemin += dureeRue;
                    dureeRue = troncon.getDuree();
                    distanceRue = troncon.getLongueur();
                    rueCourante = troncon;
                }
            }

            fluxFichier.println("<li>\n" +
                    "          <div class='timeline-badge success'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                    "          <div class='timeline-panel'>\n" +
                    "            <div class='timeline-heading'>\n" +
                    "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                    "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape++, heureDerniereLivraison + dureeChemin) + " </small></p>\n" +
                    "            </div>\n" +
                    "            <div class='timeline-body'>\n" +
                    "              <p> Prendre la rue " + rueCourante.getNom() + " et continuer sur " + (int) distanceRue + "m" + "</p>\n" +
                    "            </div>\n" +
                    "          </div>\n" +
                    "        </li>");
            distanceTotale += distanceRue;
            dureeChemin += dureeRue;
            if (entrepot == chemin.getArrivee()) {
                fluxFichier.println("<li class='timeline-inverted'>\n" +
                        "          <div class='timeline-badge warning'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                        "          <div class='timeline-panel'>\n" +
                        "            <div class='timeline-heading'>\n" +
                        "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                        "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape++, chemin.getArrivee().getHeureLivraison()) + " </small></p>\n" +
                        "            </div>\n" +
                        "            <div class='timeline-body'>\n" +
                        "              <p> Retour a l'Entrepot en " + chemin.getArrivee().getIntersection().getAdresse() + "</p>\n" +
                        "            </div>\n" +
                        "          </div>\n" +
                        "        </li>");
                heureDerniereLivraison = chemin.getArrivee().getHeureLivraison() + tempsArret;
            } else {
                int attente = chemin.getArrivee().getHeureLivraison() - (heureDerniereLivraison + dureeChemin);
                if (attente > 0) {
                    fluxFichier.println("<li class='timeline-inverted'>\n" +
                            "          <div class='timeline-badge primary'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                            "          <div class='timeline-panel'>\n" +
                            "            <div class='timeline-heading'>\n" +
                            "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                            "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape, heureDerniereLivraison + dureeChemin) + " </small></p>\n" +
                            "            </div>\n" +
                            "            <div class='timeline-body'>\n" +
                            "              <p> " + "Patienter en " + chemin.getArrivee().getIntersection().getAdresse() + " pendant " +
                            tempsEnHeures(attente) + "h" +
                            (tempsEnMinutes(attente) < 10 ? "0" : "") + tempsEnMinutes(attente) + "m" + "</p>\n" +
                            "            </div>\n" +
                            "          </div>\n" +
                            "        </li>");
                }
                fluxFichier.println("<li class='timeline-inverted'>\n" +
                        "          <div class='timeline-badge warning'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                        "          <div class='timeline-panel'>\n" +
                        "            <div class='timeline-heading'>\n" +
                        "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                        "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape, chemin.getArrivee().getHeureLivraison()) + " </small></p>\n" +
                        "            </div>\n" +
                        "            <div class='timeline-body'>\n" +
                        "              <p> Effectuer la livraison en " + chemin.getArrivee().getIntersection().getAdresse() + "</p>\n" +
                        "            </div>\n" +
                        "          </div>\n" +
                        "        </li>");
                if (chemin.getArrivee().estEnRetard()) {
                    int retard = chemin.getArrivee().getHeureLivraison() - chemin.getArrivee().getHeureFinFenetre();
                    fluxFichier.println("<li class='timeline-inverted'>\n" +
                            "          <div class='timeline-badge danger'><i class='glyphicon glyphicon-credit-card' style='padding-top:15px'></i></div>\n" +
                            "          <div class='timeline-panel'>\n" +
                            "            <div class='timeline-heading'>\n" +
                            "              <h4 class='timeline-title'>Etape : " + etape + "</h4>\n" +
                            "              <p><small class='text-muted'><i class='glyphicon glyphicon-time'></i>" + horodateur(etape, chemin.getArrivee().getHeureLivraison()) + " </small></p>\n" +
                            "            </div>\n" +
                            "            <div class='timeline-body'>\n" +
                            "              <p> Cette livraison a un retard de " + tempsEnHeures(retard) + "h" + (tempsEnMinutes(retard) < 10 ? "0" : "") + tempsEnMinutes(retard) + "m !!" + "</p>\n" +
                            "            </div>\n" +
                            "          </div>\n" +
                            "        </li>");
                }
                etape++;
                fluxFichier.println();
                heureDerniereLivraison = chemin.getArrivee().getHeureLivraison() + tempsArret;
            }
        }

        int dureeTotale = heureDerniereLivraison - heureDebutItineraire;
        fluxFichier.println("</ul>\n" + "<br><div class='well'><h2>La demande de livraison a dure " +
                tempsEnHeures(dureeTotale) + "h" +
                (tempsEnMinutes(dureeTotale) < 10 ? "0" : "") + tempsEnMinutes(dureeTotale) + "m" +
                " pour une distance totale de " + distanceTotale / 1000 + "." + distanceTotale % 1000 + "km</div></div>" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n");

        fluxFichier.close();
        return true;
    }

    private String recupererCSS() {
        return ".timeline {\n" +
                "    list-style: none;\n" +
                "    padding: 20px 0 20px;\n" +
                "    position: relative;\n" +
                "}\n" +
                "\n" +
                "    .timeline:before {\n" +
                "        top: 0;\n" +
                "        bottom: 0;\n" +
                "        position: absolute;\n" +
                "        content: \" \";\n" +
                "        width: 3px;\n" +
                "        background-color: #eeeeee;\n" +
                "        left: 50%;\n" +
                "        margin-left: -1.5px;\n" +
                "    }\n" +
                "\n" +
                "    .timeline > li {\n" +
                "        margin-bottom: 20px;\n" +
                "        position: relative;\n" +
                "    }\n" +
                "\n" +
                "        .timeline > li:before,\n" +
                "        .timeline > li:after {\n" +
                "            content: \" \";\n" +
                "            display: table;\n" +
                "        }\n" +
                "\n" +
                "        .timeline > li:after {\n" +
                "            clear: both;\n" +
                "        }\n" +
                "\n" +
                "        .timeline > li:before,\n" +
                "        .timeline > li:after {\n" +
                "            content: \" \";\n" +
                "            display: table;\n" +
                "        }\n" +
                "\n" +
                "        .timeline > li:after {\n" +
                "            clear: both;\n" +
                "        }\n" +
                "\n" +
                "        .timeline > li > .timeline-panel {\n" +
                "            width: 46%;\n" +
                "            float: left;\n" +
                "            border: 1px solid #d4d4d4;\n" +
                "            border-radius: 2px;\n" +
                "            padding: 20px;\n" +
                "            position: relative;\n" +
                "            -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);\n" +
                "            box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);\n" +
                "        }\n" +
                "\n" +
                "            .timeline > li > .timeline-panel:before {\n" +
                "                position: absolute;\n" +
                "                top: 26px;\n" +
                "                right: -15px;\n" +
                "                display: inline-block;\n" +
                "                border-top: 15px solid transparent;\n" +
                "                border-left: 15px solid #ccc;\n" +
                "                border-right: 0 solid #ccc;\n" +
                "                border-bottom: 15px solid transparent;\n" +
                "                content: \" \";\n" +
                "            }\n" +
                "\n" +
                "            .timeline > li > .timeline-panel:after {\n" +
                "                position: absolute;\n" +
                "                top: 27px;\n" +
                "                right: -14px;\n" +
                "                display: inline-block;\n" +
                "                border-top: 14px solid transparent;\n" +
                "                border-left: 14px solid #fff;\n" +
                "                border-right: 0 solid #fff;\n" +
                "                border-bottom: 14px solid transparent;\n" +
                "                content: \" \";\n" +
                "            }\n" +
                "\n" +
                "        .timeline > li > .timeline-badge {\n" +
                "            color: #fff;\n" +
                "            width: 50px;\n" +
                "            height: 50px;\n" +
                "            line-height: 50px;\n" +
                "            font-size: 1.4em;\n" +
                "            text-align: center;\n" +
                "            position: absolute;\n" +
                "            top: 16px;\n" +
                "            left: 50%;\n" +
                "            margin-left: -25px;\n" +
                "            background-color: #999999;\n" +
                "            z-index: 100;\n" +
                "            border-top-right-radius: 50%;\n" +
                "            border-top-left-radius: 50%;\n" +
                "            border-bottom-right-radius: 50%;\n" +
                "            border-bottom-left-radius: 50%;\n" +
                "        }\n" +
                "\n" +
                "        .timeline > li.timeline-inverted > .timeline-panel {\n" +
                "            float: right;\n" +
                "        }\n" +
                "\n" +
                "            .timeline > li.timeline-inverted > .timeline-panel:before {\n" +
                "                border-left-width: 0;\n" +
                "                border-right-width: 15px;\n" +
                "                left: -15px;\n" +
                "                right: auto;\n" +
                "            }\n" +
                "\n" +
                "            .timeline > li.timeline-inverted > .timeline-panel:after {\n" +
                "                border-left-width: 0;\n" +
                "                border-right-width: 14px;\n" +
                "                left: -14px;\n" +
                "                right: auto;\n" +
                "            }\n" +
                "\n" +
                ".timeline-badge.primary {\n" +
                "    background-color: #2e6da4 !important;\n" +
                "}\n" +
                "\n" +
                ".timeline-badge.success {\n" +
                "    background-color: #3f903f !important;\n" +
                "}\n" +
                "\n" +
                ".timeline-badge.warning {\n" +
                "    background-color: #f0ad4e !important;\n" +
                "}\n" +
                "\n" +
                ".timeline-badge.danger {\n" +
                "    background-color: #d9534f !important;\n" +
                "}\n" +
                "\n" +
                ".timeline-badge.info {\n" +
                "    background-color: #5bc0de !important;\n" +
                "}\n" +
                "\n" +
                ".timeline-title {\n" +
                "    margin-top: 0;\n" +
                "    color: inherit;\n" +
                "}\n" +
                "\n" +
                ".timeline-body > p,\n" +
                ".timeline-body > ul {\n" +
                "    margin-bottom: 0;\n" +
                "}\n" +
                "\n" +
                "    .timeline-body > p + p {\n" +
                "        margin-top: 5px;\n" +
                "    }\n" +
                "\n" +
                "@media (max-width: 767px) {\n" +
                "    ul.timeline:before {\n" +
                "        left: 40px;\n" +
                "    }\n" +
                "\n" +
                "    ul.timeline > li > .timeline-panel {\n" +
                "        width: calc(100% - 90px);\n" +
                "        width: -moz-calc(100% - 90px);\n" +
                "        width: -webkit-calc(100% - 90px);\n" +
                "    }\n" +
                "\n" +
                "    ul.timeline > li > .timeline-badge {\n" +
                "        left: 15px;\n" +
                "        margin-left: 0;\n" +
                "        top: 16px;\n" +
                "    }\n" +
                "\n" +
                "    ul.timeline > li > .timeline-panel {\n" +
                "        float: right;\n" +
                "    }\n" +
                "\n" +
                "        ul.timeline > li > .timeline-panel:before {\n" +
                "            border-left-width: 0;\n" +
                "            border-right-width: 15px;\n" +
                "            left: -15px;\n" +
                "            right: auto;\n" +
                "        }\n" +
                "\n" +
                "        ul.timeline > li > .timeline-panel:after {\n" +
                "            border-left-width: 0;\n" +
                "            border-right-width: 14px;\n" +
                "            left: -14px;\n" +
                "            right: auto;\n" +
                "        }\n" +
                "}";
    }

    public void setFenetre(Stage fenetre) {
        this.fenetre = fenetre;
    }

    private int tempsEnHeures(int temps) {
        return temps / 3600;
    }

    private int tempsEnMinutes(int temps) {
        return (temps % 3600) / 60;
    }

    private int tempsEnSecondes(int temps) {
        return (temps % 3600) % 60;
    }

    private String horodateur(int etape, int temps) {
        return tempsEnHeures(temps) + "h" + (tempsEnMinutes(temps) < 10 ? "0" : "") + tempsEnMinutes(temps) + "m";
    }
}
