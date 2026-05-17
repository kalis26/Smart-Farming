package smartfarming.capteurs;

import smartfarming.entites.Animal;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.zones.Zone;

public abstract class CapteurBiometrique extends CapteurNumerique {
    private Animal animal;

    protected CapteurBiometrique() {
    }

    protected CapteurBiometrique(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax, Animal animal) {
        super(id, nom, statut, zone, unite, seuilMin, seuilMax);
        this.animal = animal;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
