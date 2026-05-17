package smartfarming.zones;

import java.util.ArrayList;
import java.util.List;
import smartfarming.entites.Bassin;
import smartfarming.entites.ProgrammeAlimentation;

public class ZoneAquacole extends Zone {

    private List<Bassin> bassins;
    private ProgrammeAlimentation programmeAlimentation;
    private double poidsRecolte;   
    public ZoneAquacole(String code, String nom, double latitudeMin, double latitudeMax,
            double longitudeMin, double longitudeMax, ProgrammeAlimentation programmeAlimentation) {
        super(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax);
        this.bassins     = new ArrayList<>();
        this.programmeAlimentation = programmeAlimentation;
        this.poidsRecolte = 0.0;
    }
    public List<Bassin> getBassins()      { return bassins; }
    public ProgrammeAlimentation getProgrammeAlimentation() { return programmeAlimentation; }
    public double       getPoidsRecolte() { return poidsRecolte; }
    public void setProgrammeAlimentation(ProgrammeAlimentation programmeAlimentation) {
        this.programmeAlimentation = programmeAlimentation;
    }
    public void ajouterBassin(Bassin b) {
        bassins.add(b);
        System.out.println("Bassin #" + b.getId() + " ajoute a la zone " + getNom());
    }
    public void supprimerBassin(Bassin b) {
        bassins.remove(b);
        System.out.println("Bassin #" + b.getId() + " retire de la zone " + getNom());
    }
    public void enregistrerProduction(double poidsKg) {
        this.poidsRecolte = poidsKg;
        System.out.println("Zone [" + getCode() + "] - Recolte enregistree : " +
                           poidsKg + " kg");
    }
    public int getNombreTotalAnimaux() {
        int total = 0;
        for (Bassin b : bassins) {
            total += b.getNombreAnimaux();
        }
        return total;
    }

    @Override
    public void afficherResume() {
        System.out.println("--- Zone Aquacole : " + getNom() +
                           " [" + getCode() + "] | Statut : " + getStatus() +
                           " | Bassins : " + bassins.size() +
                           " | Total animaux : " + getNombreTotalAnimaux() +
                           " | Aliment : " + programmeAlimentation +
                           " | Recolte : " + poidsRecolte + " kg ---");
    }
}

