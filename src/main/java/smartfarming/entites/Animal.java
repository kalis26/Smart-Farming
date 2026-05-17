package smartfarming.entites;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.EtatSante;
import smartfarming.enums.TypeElevage;

public abstract class Animal {

    private int           id;
    private EspeceAnimale espece;
    private int           age;
    private double        poids;
    private EtatSante  etatSante;
    private List<EvenementSanitaire>  historiqueSante;

    public Animal(int id, EspeceAnimale espece, int age, double poids) {
        this.id        = id;
        this.espece    = espece;
        this.age       = age;
        this.poids     = poids;
        this.etatSante = EtatSante.SAIN;
        this.historiqueSante = new ArrayList<>();
        enregistrerEvenementSante("Creation de l'animal", EtatSante.SAIN, null, poids);
    }

    public int           getId()        { return id; }
    public EspeceAnimale getEspece()    { return espece; }
    public int           getAge()       { return age; }
    public double        getPoids()     { return poids; }
    public EtatSante  getEtatSante() { return etatSante; }
    public List<EvenementSanitaire> getHistoriqueSante() { return historiqueSante; }

    public abstract TypeElevage getTypeElevage();

    public void setAge(int age)     { this.age = age; }
    public void setPoids(double p)  { this.poids = p; }

    public void mettreAJourSante(EtatSante etat) {
        this.etatSante = etat;
        enregistrerEvenementSante("Etat de sante: " + etat, etat, null, null);
        System.out.println("Animal #" + id + " (" + espece + ")" +
                           " - Etat de sante mis a jour : " + etat);
    }

    public void enregistrerPoids(double nouveauPoids) {
        double ancien = this.poids;
        this.poids = nouveauPoids;
        enregistrerEvenementSante("Poids: " + ancien + " kg -> " + nouveauPoids + " kg",
                this.etatSante, ancien, nouveauPoids);
        System.out.println("Animal #" + id + " - Poids mis a jour : " +
                           ancien + " kg -> " + nouveauPoids + " kg");
    }

    public void enregistrerEvenementSante(String description, EtatSante etatSante,
            Double ancienPoids, Double nouveauPoids) {
        EvenementSanitaire evenement = new EvenementSanitaire(UUID.randomUUID().toString(),
                LocalDateTime.now(), this, description, etatSante, ancienPoids, nouveauPoids);
        historiqueSante.add(evenement);
    }

    @Override
    public String toString() {
        return "Animal{id=" + id +
               ", espece=" + espece +
               ", age=" + age + " mois" +
               ", poids=" + poids + " kg" +
             ", sante=" + etatSante + "}";
    }
}
