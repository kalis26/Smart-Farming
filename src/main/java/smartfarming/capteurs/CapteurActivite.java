package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.entites.Animal;
import smartfarming.zones.Zone;

public class CapteurActivite extends CapteurBiometrique {
	public CapteurActivite() {
	}

	public CapteurActivite(String id, String nom, StatutCapteur statut, Zone zone,
			UniteMesure unite, double seuilMin, double seuilMax) {
		super(id, nom, statut, zone, unite, seuilMin, seuilMax, null);
	}

	public CapteurActivite(String id, String nom, StatutCapteur statut, Zone zone,
			UniteMesure unite, double seuilMin, double seuilMax, Animal animal) {
		super(id, nom, statut, zone, unite, seuilMin, seuilMax, animal);
	}
}
