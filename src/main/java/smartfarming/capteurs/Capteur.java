package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.zones.Zone;

public abstract class Capteur {
    private String id;
    private String nom;
    private StatutCapteur statut;
    private Zone zone;

    protected Capteur() {
    }

    protected Capteur(String id, String nom, StatutCapteur statut, Zone zone) {
        this.id = id;
        this.nom = nom;
        this.statut = statut;
        this.zone = zone;
    }

    public void activer() {
        this.statut = StatutCapteur.Actif;
    }

    public void suspendre() {
        this.statut = StatutCapteur.Suspendu;
    }

    public void signalerDefaillance() {
        this.statut = StatutCapteur.Defaillant;
    }

    public boolean estActif() {
        return this.statut == StatutCapteur.Actif;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public StatutCapteur getStatut() {
        return statut;
    }

    public void setStatut(StatutCapteur statut) {
        this.statut = statut;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
