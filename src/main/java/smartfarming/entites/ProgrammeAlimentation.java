package smartfarming.entites;

public class ProgrammeAlimentation {
    private String typeAliment;
    private double quantiteParRepas;

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas) {
        this.typeAliment = typeAliment;
        this.quantiteParRepas = quantiteParRepas;
    }

    public String getTypeAliment() {
        return typeAliment;
    }

    public void setTypeAliment(String typeAliment) {
        this.typeAliment = typeAliment;
    }

    public double getQuantiteParRepas() {
        return quantiteParRepas;
    }

    public void setQuantiteParRepas(double quantiteParRepas) {
        this.quantiteParRepas = quantiteParRepas;
    }

    @Override
    public String toString() {
        return typeAliment + " / " + quantiteParRepas + " kg par repas";
    }
}
