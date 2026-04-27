package smartfarming.zones;

import java.util.List;

import smartfarming.alertes.Alerte;
import smartfarming.capteurs.Capteur;
import smartfarming.enums.StatutZone;
import smartfarming.mesures.Releve;

public abstract class Zone {
    private String code;
    private String nom;
    private StatutZone statut;
    private List<Capteur> capteurs;
    private List<Releve> releves;
    private List<Alerte> alertes;
}
