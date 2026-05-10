package smartfarming.model;

import smartfarming.enums.StatutZone;
import java.util.ArrayList;
import java.util.List;
public abstract class Zone {

    private String code;
    private String nom;
    private StatutZone status;
    public Zone(String code, String nom) {
        this.code   = code;
        this.nom    = nom;
        this.status = StatutZone.ACTIVE;
    }
    public String getCode()       { return code; }
    public String getNom()        { return nom; }
    public StatutZone getStatus() { return status; }
    public void setNom(String nom)          { this.nom = nom; }
    public void setStatus(StatutZone status) { this.status = status; }
    public void suspendre() {
        this.status = StatutZone.SUSPENDED;
        System.out.println("Zone [" + code + "] - " + nom + " : suspendue.");
    }
    public void reactiver() {
        this.status = StatutZone.ACTIVE;
        System.out.println("Zone [" + code + "] - " + nom + " : réactivée.");
    }
    public boolean isActive() {
        return this.status == StatutZone.ACTIVE;
    }
    public abstract void afficherResume();
    @Override
    public String toString() {
        return getClass().getSimpleName() +
               "{code='" + code + "', nom='" + nom + "', statut=" + status + "}";
    }
}
