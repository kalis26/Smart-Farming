package smartfarming.mesures;

import java.time.LocalDateTime;

import smartfarming.capteurs.Capteur;
import smartfarming.enums.UniteMesure;

public class Releve {
    private String id;
    private LocalDateTime dateHeure;
    private Capteur capteur;
    private Double valeur;
    private UniteMesure unite;
    private PositionGPS position;

    public Releve() {
    }

    public Releve(String id, LocalDateTime dateHeure, Capteur capteur, Double valeur,
            UniteMesure unite, PositionGPS position) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.capteur = capteur;
        this.valeur = valeur;
        this.unite = unite;
        this.position = position;
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

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public PositionGPS getPosition() {
        return position;
    }

    public void setPosition(PositionGPS position) {
        this.position = position;
    }
}
