package smartfarming.entites;


import smartfarming.enums.EspeceAquacole;

public class Bassin {

    private int        id;
    private EspeceAquacole espece;
    private int        nombreAnimaux;

    private String typeAliment;
    private double quantiteParRepas;

    public Bassin(int id, EspeceAquacole espece, int nombreAnimaux) {
        this.id            = id;
        this.espece        = espece;
        this.nombreAnimaux = nombreAnimaux;
        this.typeAliment   = "";
        this.quantiteParRepas = 0.0;
    }

    public int         getId()              { return id; }
    public EspeceAquacole getEspece()          { return espece; }
    public int         getNombreAnimaux()   { return nombreAnimaux; }
    public String      getTypeAliment()     { return typeAliment; }
    public double      getQuantiteParRepas(){ return quantiteParRepas; }

    public void setNombreAnimaux(int n) { this.nombreAnimaux = n; }

    public void definirProgrammeAlimentation(String typeAliment, double quantiteKg) {
        this.typeAliment      = typeAliment;
        this.quantiteParRepas = quantiteKg;
        System.out.println("Bassin #" + id + " - Programme alimentation : " +
                           typeAliment + " / " + quantiteKg + " kg par repas");
    }

    @Override
    public String toString() {
        return "Bassin{id=" + id +
               ", espece=" + espece +
               ", nbAnimaux=" + nombreAnimaux +
               ", aliment='" + typeAliment + "'" +
             ", qte/repas=" + quantiteParRepas + " kg}";
    }
}
