package smartfarming.zones;

import java.util.ArrayList;
import java.util.List;
import smartfarming.entites.Animal;
import smartfarming.entites.ProgrammeAlimentation;
import smartfarming.enums.TypeElevage;

public class ZoneElevage extends Zone {

    private List<Animal> animaux;

    private TypeElevage typeElevage;
    private ProgrammeAlimentation programmeAlimentation;

    private double rendementLaitier;
    private double productionOeufs;

    public ZoneElevage(String code, String nom, double latitudeMin, double latitudeMax,
            double longitudeMin, double longitudeMax, TypeElevage typeElevage,
            ProgrammeAlimentation programmeAlimentation) {
        super(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax);
        this.animaux          = new ArrayList<>();
        this.typeElevage = typeElevage;
        this.programmeAlimentation = programmeAlimentation;
        this.rendementLaitier = 0.0;
        this.productionOeufs  = 0.0;
    }

    public List<Animal> getAnimaux()           { return animaux; }
    public TypeElevage getTypeElevage() { return typeElevage; }
    public ProgrammeAlimentation getProgrammeAlimentation() { return programmeAlimentation; }
    public double       getRendementLaitier()  { return rendementLaitier; }
    public double       getProductionOeufs()   { return productionOeufs; }

    public void ajouterAnimal(Animal a) {
        if (a.getTypeElevage() != typeElevage) {
            throw new IllegalArgumentException("Animal de type " + a.getTypeElevage()
                    + " incompatible avec la zone " + typeElevage);
        }
        animaux.add(a);
        System.out.println("Animal #" + a.getId() + " (" + a.getEspece() +
                           ") ajoute a la zone " + getNom());
    }

    public void supprimerAnimal(Animal a) {
        animaux.remove(a);
        System.out.println("Animal #" + a.getId() + " retire de la zone " + getNom());
    }

    public void setProgrammeAlimentation(ProgrammeAlimentation programmeAlimentation) {
        this.programmeAlimentation = programmeAlimentation;
    }

    public void definirProgrammeAlimentation(String typeAliment, double quantiteKg) {
        this.programmeAlimentation = new ProgrammeAlimentation(typeAliment, quantiteKg);
        System.out.println("Zone [" + getCode() + "] - Programme alimentation : " +
                           programmeAlimentation);
    }

    public void enregistrerProduction(double laitLitres, double oeufs) {
        if (typeElevage == TypeElevage.RUMINANTS && oeufs != 0.0) {
            throw new IllegalArgumentException("Une zone de ruminants ne produit pas d'oeufs.");
        }
        if (typeElevage == TypeElevage.VOLAILLE && laitLitres != 0.0) {
            throw new IllegalArgumentException("Une zone de volaille ne produit pas de lait.");
        }
        this.rendementLaitier = laitLitres;
        this.productionOeufs  = oeufs;
        System.out.println("Zone [" + getCode() + "] - Production enregistree : " +
                           laitLitres + " L lait, " + oeufs + " oeufs");
    }

    @Override
    public void afficherResume() {
        System.out.println("--- Zone Elevage : " + getNom() +
                           " [" + getCode() + "] | Statut : " + getStatus() +
                           " | Type : " + typeElevage +
                           " | Animaux : " + animaux.size() +
                           " | Aliment : " + programmeAlimentation + " ---");
    }
}
