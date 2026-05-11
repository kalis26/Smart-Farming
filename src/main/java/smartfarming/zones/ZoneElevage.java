package smartfarming.zones;

import java.util.ArrayList;
import java.util.List;
import smartfarming.entites.Animal;

public class ZoneElevage extends Zone {

    private List<Animal> animaux;

    private String typeAliment;
    private double quantiteParRepas;

    private double rendementLaitier;
    private double productionOeufs;

    public ZoneElevage(String code, String nom) {
        super(code, nom);
        this.animaux          = new ArrayList<>();
        this.typeAliment      = "";
        this.quantiteParRepas = 0.0;
        this.rendementLaitier = 0.0;
        this.productionOeufs  = 0.0;
    }

    public List<Animal> getAnimaux()           { return animaux; }
    public String       getTypeAliment()       { return typeAliment; }
    public double       getQuantiteParRepas()  { return quantiteParRepas; }
    public double       getRendementLaitier()  { return rendementLaitier; }
    public double       getProductionOeufs()   { return productionOeufs; }

    public void ajouterAnimal(Animal a) {
        animaux.add(a);
        System.out.println("Animal #" + a.getId() + " (" + a.getEspece() +
                           ") ajoute a la zone " + getNom());
    }

    public void supprimerAnimal(Animal a) {
        animaux.remove(a);
        System.out.println("Animal #" + a.getId() + " retire de la zone " + getNom());
    }

    public void definirProgrammeAlimentation(String typeAliment, double quantiteKg) {
        this.typeAliment      = typeAliment;
        this.quantiteParRepas = quantiteKg;
        System.out.println("Zone [" + getCode() + "] - Programme alimentation : " +
                           typeAliment + " / " + quantiteKg + " kg par repas");
    }

    public void enregistrerProduction(double laitLitres, double oeufs) {
        this.rendementLaitier = laitLitres;
        this.productionOeufs  = oeufs;
        System.out.println("Zone [" + getCode() + "] - Production enregistree : " +
                           laitLitres + " L lait, " + oeufs + " oeufs");
    }

    @Override
    public void afficherResume() {
        System.out.println("--- Zone Elevage : " + getNom() +
                           " [" + getCode() + "] | Statut : " + getStatus() +
                           " | Animaux : " + animaux.size() +
                           " | Aliment : " + typeAliment +
                           " (" + quantiteParRepas + " kg/repas) ---");
    }
}
