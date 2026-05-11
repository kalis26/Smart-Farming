package smartfarming.entites;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.EtatSante;

public class Animal {

    private int           id;
    private EspeceAnimale espece;
    private int           age;
    private double        poids;
    private EtatSante  etatSante;
    private List<String>  historiqueSante;

    public Animal(int id, EspeceAnimale espece, int age, double poids) {
        this.id        = id;
        this.espece    = espece;
        this.age       = age;
        this.poids     = poids;
        this.etatSante = EtatSante.SAIN;
        this.historiqueSante = new ArrayList<>();
        this.historiqueSante.add(LocalDateTime.now() + " | Creation de l'animal");
    }

    public int           getId()        { return id; }
    public EspeceAnimale getEspece()    { return espece; }
    public int           getAge()       { return age; }
    public double        getPoids()     { return poids; }
    public EtatSante  getEtatSante() { return etatSante; }
    public List<String> getHistoriqueSante() { return historiqueSante; }

    public void setAge(int age)     { this.age = age; }
    public void setPoids(double p)  { this.poids = p; }

    public void mettreAJourSante(EtatSante etat) {
        this.etatSante = etat;
        enregistrerEvenementSante("Etat de sante: " + etat);
        System.out.println("Animal #" + id + " (" + espece + ")" +
                           " - Etat de sante mis a jour : " + etat);
    }

    public void enregistrerPoids(double nouveauPoids) {
        double ancien = this.poids;
        this.poids = nouveauPoids;
        enregistrerEvenementSante("Poids: " + ancien + " kg -> " + nouveauPoids + " kg");
        System.out.println("Animal #" + id + " - Poids mis a jour : " +
                           ancien + " kg -> " + nouveauPoids + " kg");
    }

    public void enregistrerEvenementSante(String evenement) {
        String entree = LocalDateTime.now() + " | " + evenement;
        historiqueSante.add(entree);
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