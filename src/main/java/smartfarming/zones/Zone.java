package smartfarming.zones;

import java.util.ArrayList;
import java.util.List;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.Capteur;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.StatutZone;
import smartfarming.mesures.PositionGPS;
import smartfarming.mesures.Releve;
public abstract class Zone {

    private String code;
    private String nom;
    private StatutZone status;
    private double latitudeMin;
    private double latitudeMax;
    private double longitudeMin;
    private double longitudeMax;
    private List<Capteur> capteurs;
    private List<Capteur> capteursSuspendusParZone;
    private List<Releve> releves;
    private List<Alerte> alertes;
    public Zone(String code, String nom, double latitudeMin, double latitudeMax,
            double longitudeMin, double longitudeMax) {
        this.code   = code;
        this.nom    = nom;
        this.status = StatutZone.ACTIVE;
        this.latitudeMin = latitudeMin;
        this.latitudeMax = latitudeMax;
        this.longitudeMin = longitudeMin;
        this.longitudeMax = longitudeMax;
        this.capteurs = new ArrayList<>();
        this.capteursSuspendusParZone = new ArrayList<>();
        this.releves = new ArrayList<>();
        this.alertes = new ArrayList<>();
    }
    public String getCode()       { return code; }
    public String getNom()        { return nom; }
    public StatutZone getStatus() { return status; }
    public double getLatitudeMin() { return latitudeMin; }
    public double getLatitudeMax() { return latitudeMax; }
    public double getLongitudeMin() { return longitudeMin; }
    public double getLongitudeMax() { return longitudeMax; }
    public List<Capteur> getCapteurs() { return capteurs; }
    public List<Releve> getReleves() { return releves; }
    public List<Alerte> getAlertes() { return alertes; }
    public void setNom(String nom)          { this.nom = nom; }
    public void setStatus(StatutZone status) { this.status = status; }
    public void ajouterCapteur(Capteur capteur) {
        capteur.setZone(this);
        capteurs.add(capteur);
    }
    public void ajouterReleve(Releve releve) {
        releves.add(releve);
    }
    public void ajouterAlerte(Alerte alerte) {
        alertes.add(alerte);
    }
    public boolean contientPosition(PositionGPS position) {
        if (position == null) {
            return false;
        }
        return position.getLatitude() >= latitudeMin
                && position.getLatitude() <= latitudeMax
                && position.getLongitude() >= longitudeMin
                && position.getLongitude() <= longitudeMax;
    }
    public void suspendre() {
        if (this.status == StatutZone.SUSPENDED) {
            System.out.println("Zone [" + code + "] - " + nom + " : deja suspendue.");
            return;
        }
        this.status = StatutZone.SUSPENDED;
        capteursSuspendusParZone.clear();
        for (Capteur capteur : capteurs) {
            if (capteur.getStatut() == StatutCapteur.Actif) {
                capteur.suspendre();
                capteursSuspendusParZone.add(capteur);
            }
        }
        System.out.println("Zone [" + code + "] - " + nom + " : suspendue.");
    }
    public void reactiver() {
        this.status = StatutZone.ACTIVE;
        for (Capteur capteur : capteursSuspendusParZone) {
            capteur.activer();
        }
        capteursSuspendusParZone.clear();
        System.out.println("Zone [" + code + "] - " + nom + " : reactivee.");
    }
    public boolean isActive() {
        return this.status == StatutZone.ACTIVE;
    }
    public abstract void afficherResume();
   
}
