package smartfarming.capteurs;

import smartfarming.enums.UniteMesure;

public abstract class CapteurNumerique extends Capteur {
    private UniteMesure unite;
    private double seuilMin;
    private double seuilMax;
}
