package smartfarming.capteurs;

import smartfarming.entites.Animal;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.zones.Zone;

public class CapteurTemperatureCorporelle extends CapteurBiometrique {
    public CapteurTemperatureCorporelle() {
    }

    public CapteurTemperatureCorporelle(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax, Animal animal) {
        super(id, nom, statut, zone, unite, seuilMin, seuilMax, animal);
    }
}
