package smartfarming.entites;


import smartfarming.enums.EspeceAquacole;

public class Bassin {

    private int        id;
    private EspeceAquacole espece;
    private int        nombreAnimaux;

    public Bassin(int id, EspeceAquacole espece, int nombreAnimaux) {
        this.id            = id;
        this.espece        = espece;
        this.nombreAnimaux = nombreAnimaux;
    }

    public int         getId()              { return id; }
    public EspeceAquacole getEspece()          { return espece; }
    public int         getNombreAnimaux()   { return nombreAnimaux; }

    public void setNombreAnimaux(int n) { this.nombreAnimaux = n; }

    @Override
    public String toString() {
        return "Bassin{id=" + id +
               ", espece=" + espece +
               ", nbAnimaux=" + nombreAnimaux + "}";
    }
}
