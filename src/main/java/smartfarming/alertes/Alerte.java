package smartfarming.alertes;

import java.time.LocalDateTime;
import smartfarming.enums.NiveauGravite;
import smartfarming.enums.StatutAlerte;
import smartfarming.mesures.Releve;
import smartfarming.zones.Zone;

public class Alerte {
    private String id;
    private LocalDateTime dateHeure;
    private NiveauGravite gravite;
    private String message;
    private StatutAlerte statut;
    private Zone zone;
    private Releve releveDeclencheur;

    public Alerte() {
    }

    public Alerte(String id, LocalDateTime dateHeure, NiveauGravite gravite, String message,
        StatutAlerte statut, Zone zone, Releve releveDeclencheur) {
            this.id = id;
            this.dateHeure = dateHeure;
            this.gravite = gravite;
            this.message = message;
            this.statut = statut;
            this.zone = zone;
            this.releveDeclencheur = releveDeclencheur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public NiveauGravite getGravite() {
        return gravite;
    }

    public void setGravite(NiveauGravite gravite) {
        this.gravite = gravite;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatutAlerte getStatut() {
        return statut;
    }

    public void setStatut(StatutAlerte statut) {
        this.statut = statut;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Releve getReleveDeclencheur() {
        return releveDeclencheur;
    }

    public void setReleveDeclencheur(Releve releveDeclencheur) {
        this.releveDeclencheur = releveDeclencheur;
    }
}
