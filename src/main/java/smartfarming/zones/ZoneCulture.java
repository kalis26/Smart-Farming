package smartfarming.zones;

import java.util.ArrayList;
import java.util.List;
import smartfarming.entites.Culture;

public class ZoneCulture extends Zone {

    private List<Culture> cultures;
    private double rendement;

    public ZoneCulture(String code, String nom, double latitudeMin, double latitudeMax,
            double longitudeMin, double longitudeMax) {
        super(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax);
        this.cultures  = new ArrayList<>();
        this.rendement = 0.0;
    }

    public List<Culture> getCultures()  { return cultures; }
    public double        getRendement() { return rendement; }

    public void ajouterCulture(Culture c) {
        cultures.add(c);
        System.out.println("Culture [" + c.getId() + "] ajoutee a la zone " + getNom());
    }

    public void supprimerCulture(Culture c) {
        cultures.remove(c);
        System.out.println("Culture [" + c.getId() + "] retiree de la zone " + getNom());
    }

    public void enregistrerProduction(double rendementKgHa) {
        this.rendement = rendementKgHa;
        System.out.println("Zone [" + getCode() + "] - Rendement enregistre : " +
                           rendementKgHa + " kg/ha");
    }

    public void genererRapportCultures() {
        System.out.println("=== Rapport Cultures - Zone : " + getNom() + " ===");
        if (cultures.isEmpty()) {
            System.out.println("  Aucune culture enregistree.");
        } else {
            for (Culture c : cultures) {
                System.out.println("  " + c);
            }
        }
    }

    @Override
    public void afficherResume() {
        System.out.println("--- Zone Culture : " + getNom() +
                           " [" + getCode() + "] | Statut : " + getStatus() +
                           " | Cultures : " + cultures.size() +
                           " | Rendement : " + rendement + " kg/ha ---");
    }
}
