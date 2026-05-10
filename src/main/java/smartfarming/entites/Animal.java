package smartfarming.entites;


import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.EtatSante;

public class Animal {

    private int           id;
    private EspeceAnimale espece;
    private int           age;
    private double        poids;
    private EtatSante  etatSante;

    public Animal(int id, EspeceAnimale espece, int age, double poids) {
        this.id        = id;
        this.espece    = espece;
        this.age       = age;
        this.poids     = poids;
        this.etatSante = EtatSante.SAIN;
    }

    public int           getId()        { return id; }
    public EspeceAnimale getEspece()    { return espece; }
    public int           getAge()       { return age; }
    public double        getPoids()     { return poids; }
    public EtatSante  getEtatSante() { return etatSante; }

    public void setAge(int age)     { this.age = age; }
    public void setPoids(double p)  { this.poids = p; }

    public void mettreAJourSante(EtatSante etat) {
        this.etatSante = etat;
        System.out.println("Animal #" + id + " (" + espece + ")" +
                           " - État de santé mis à jour : " + etat);
    }

    public void enregistrerPoids(double nouveauPoids) {
        double ancien = this.poids;
        this.poids = nouveauPoids;
        System.out.println("Animal #" + id + " - Poids mis à jour : " +
                           ancien + " kg → " + nouveauPoids + " kg");
    }

    @Override
    public String toString() {
        return "Animal{id=" + id +
               ", espece=" + espece +
               ", age=" + age + " mois" +
               ", poids=" + poids + " kg" +
               ", santé=" + etatSante + "}";
    }
}