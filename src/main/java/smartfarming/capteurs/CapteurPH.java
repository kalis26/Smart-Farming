package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.zones.Zone;

public class CapteurPH extends CapteurNumerique {
    public CapteurPH() {
    }

    public CapteurPH(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax) {
        super(id, nom, statut, zone, unite, seuilMin, seuilMax);
    }
}
