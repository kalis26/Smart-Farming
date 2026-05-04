package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.zones.Zone;

public abstract class CapteurNumerique extends Capteur {
    private UniteMesure unite;
    private double seuilMin;
    private double seuilMax;

    protected CapteurNumerique() {
    }

    protected CapteurNumerique(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax) {
        super(id, nom, statut, zone);
        this.unite = unite;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public double getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(double seuilMin) {
        this.seuilMin = seuilMin;
    }

    public double getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(double seuilMax) {
        this.seuilMax = seuilMax;
    }
}
