package smartfarming.zones;

import java.util.List;

import smartfarming.entites.Culture;

import java.util.ArrayList;

public class ZoneCulture extends Zone {

    private List<Culture> cultures;
    private double rendement;

    public ZoneCulture(String code, String nom) {
        super(code, nom);
        this.cultures  = new ArrayList<>();
        this.rendement = 0.0;
    }

    public List<Culture> getCultures()  { return cultures; }
    public double        getRendement() { return rendement; }

    public void ajouterCulture(Culture c) {
        cultures.add(c);
        System.out.println("Culture [" + c.getId() + "] ajoutée à la zone " + getNom());
    }

    public void supprimerCulture(Culture c) {
        cultures.remove(c);
        System.out.println("Culture [" + c.getId() + "] retirée de la zone " + getNom());
    }

    public void enregistrerProduction(double rendementKgHa) {
        this.rendement = rendementKgHa;
        System.out.println("Zone [" + getCode() + "] - Rendement enregistré : " +
                           rendementKgHa + " kg/ha");
    }

    public void genererRapportCultures() {
        System.out.println("=== Rapport Cultures - Zone : " + getNom() + " ===");
        if (cultures.isEmpty()) {
            System.out.println("  Aucune culture enregistrée.");
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
