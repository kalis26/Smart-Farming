package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.zones.Zone;

public abstract class Capteur {
    private String id;
    private String nom;
    private StatutCapteur statut;
    private Zone zone;
}
