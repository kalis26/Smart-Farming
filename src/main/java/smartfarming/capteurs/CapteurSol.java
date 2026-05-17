package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.zones.Zone;

public abstract class CapteurSol extends CapteurNumerique {
    protected CapteurSol() {
    }

    protected CapteurSol(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax) {
        super(id, nom, statut, zone, unite, seuilMin, seuilMax);
    }
}
