package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.zones.Zone;

public class CapteurGPS extends Capteur {
    public CapteurGPS() {
    }

    public CapteurGPS(String id, String nom, StatutCapteur statut, Zone zone) {
        super(id, nom, statut, zone);
    }
}
