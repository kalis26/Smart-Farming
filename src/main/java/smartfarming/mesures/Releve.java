package smartfarming.mesures;

import java.time.LocalDateTime;
import java.util.UUID;

import smartfarming.capteurs.Capteur;

public abstract class Releve {
    private String id;
    private LocalDateTime dateHeure;
    private Capteur capteur;

    public Releve() {
    }

    public Releve(String id, LocalDateTime dateHeure, Capteur capteur) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.dateHeure = (dateHeure == null) ? LocalDateTime.now() : dateHeure;
        this.capteur = capteur;
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

    public Capteur getCapteur() {
        return capteur;
    }

    public void setCapteur(Capteur capteur) {
        this.capteur = capteur;
    }
}
