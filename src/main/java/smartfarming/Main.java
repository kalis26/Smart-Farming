package smartfarming;

import java.time.LocalDate;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.CapteurActivite;
import smartfarming.capteurs.CapteurAzote;
import smartfarming.capteurs.CapteurGPS;
import smartfarming.capteurs.CapteurHumidite;
import smartfarming.capteurs.CapteurOxygeneDissous;
import smartfarming.capteurs.CapteurPH;
import smartfarming.capteurs.CapteurPluviometrie;
import smartfarming.capteurs.CapteurPoids;
import smartfarming.capteurs.CapteurTemperature;
import smartfarming.entites.Animal;
import smartfarming.entites.Bassin;
import smartfarming.entites.Culture;
import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.EspeceAquacole;
import smartfarming.enums.EtatSante;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.TypeCulture;
import smartfarming.enums.UniteMesure;
import smartfarming.rapports.RapportProduction;
import smartfarming.system.SmartFarmingSystem;
import smartfarming.zones.ZoneAquacole;
import smartfarming.zones.ZoneCulture;
import smartfarming.zones.ZoneElevage;

public class Main {
	public static void main(String[] args) {
		SmartFarmingSystem system = new SmartFarmingSystem();

		ZoneCulture zoneCulture = new ZoneCulture("ZC-01", "Zone Culture 1");
		ZoneElevage zoneElevage = new ZoneElevage("ZE-01", "Zone Elevage 1");
		ZoneAquacole zoneAquacole = new ZoneAquacole("ZA-01", "Zone Aquacole 1");
		system.ajouterZone(zoneCulture);
		system.ajouterZone(zoneElevage);
		system.ajouterZone(zoneAquacole);

		Culture culture = new Culture("C-01", TypeCulture.BLE,
				LocalDate.now().minusDays(10), LocalDate.now().plusDays(60),
				5.5, 7.0, 35.0, 70.0);
		system.ajouterCulture(zoneCulture, culture);

		Animal animal = new Animal(1, EspeceAnimale.VACHE, 24, 320.0);
		system.ajouterAnimal(zoneElevage, animal);
		animal.mettreAJourSante(EtatSante.MALADE);
		animal.enregistrerPoids(315.0);

		Bassin bassin = new Bassin(1, EspeceAquacole.POISSON, 120);
		zoneAquacole.ajouterBassin(bassin);

		CapteurTemperature tempAir = new CapteurTemperature("T-01", "Temp Air",
				StatutCapteur.Actif, null, UniteMesure.Celsius, 10.0, 30.0);
		CapteurHumidite humidite = new CapteurHumidite("H-01", "Humidite Sol",
				StatutCapteur.Actif, null, UniteMesure.Pourcentage, 30.0, 80.0);
		CapteurPH phSol = new CapteurPH("PH-01", "pH Sol",
				StatutCapteur.Actif, null, UniteMesure.pH, 5.5, 7.5);
		CapteurPluviometrie pluie = new CapteurPluviometrie("PL-01", "Pluie",
				StatutCapteur.Actif, null, UniteMesure.mm, 0.0, 50.0);
		CapteurAzote azote = new CapteurAzote("N-01", "Azote Sol",
				StatutCapteur.Actif, null, UniteMesure.gKg, 0.5, 3.0);

		system.ajouterCapteur(zoneCulture, tempAir);
		system.ajouterCapteur(zoneCulture, humidite);
		system.ajouterCapteur(zoneCulture, phSol);
		system.ajouterCapteur(zoneCulture, pluie);
		system.ajouterCapteur(zoneCulture, azote);

		CapteurActivite activite = new CapteurActivite("A-01", "Activite",
				StatutCapteur.Actif, null, UniteMesure.stepmin, 10.0, 80.0);
		CapteurPoids poids = new CapteurPoids("P-01", "Poids",
				StatutCapteur.Actif, null, UniteMesure.gKg, 200.0, 700.0);
		CapteurGPS gps = new CapteurGPS("G-01", "Collier GPS",
				StatutCapteur.Actif, null, 0.0, 0.0);

		system.ajouterCapteur(zoneElevage, activite);
		system.ajouterCapteur(zoneElevage, poids);
		system.ajouterCapteur(zoneElevage, gps);

		CapteurTemperature tempEau = new CapteurTemperature("T-02", "Temp Eau",
				StatutCapteur.Actif, null, UniteMesure.Celsius, 12.0, 28.0);
		CapteurOxygeneDissous oxygene = new CapteurOxygeneDissous("O-01", "Oxygene",
				StatutCapteur.Actif, null, UniteMesure.mgL, 5.0, 12.0);
		CapteurPH phEau = new CapteurPH("PH-02", "pH Eau",
				StatutCapteur.Actif, null, UniteMesure.pH, 6.5, 8.5);

		system.ajouterCapteur(zoneAquacole, tempEau);
		system.ajouterCapteur(zoneAquacole, oxygene);
		system.ajouterCapteur(zoneAquacole, phEau);

		system.enregistrerReleveNumerique(tempAir, 22.5);
		system.enregistrerReleveNumerique(humidite, 60.0);
		system.enregistrerReleveNumerique(phSol, 7.8);
		system.enregistrerReleveNumerique(pluie, 12.0);
		system.enregistrerReleveNumerique(azote, 4.0);

		system.enregistrerReleveNumerique(activite, 35.0);
		system.enregistrerReleveNumerique(poids, 310.0);
		system.enregistrerReleveGPS(gps, 36.7525, 3.0419);

		system.enregistrerReleveNumerique(tempEau, 18.0);
		system.enregistrerReleveNumerique(oxygene, 3.0);
		system.enregistrerReleveNumerique(phEau, 7.2);

		System.out.println("Alertes actives: " + system.alertesActives().size());
		if (!system.alertesActives().isEmpty()) {
			Alerte alerte = system.alertesActives().get(0);
			alerte.acquitter();
		}
		System.out.println("Alertes actives apres acquittement: " + system.alertesActives().size());

		zoneCulture.suspendre();
		try {
			system.enregistrerReleveNumerique(tempAir, 18.0);
		} catch (IllegalStateException ex) {
			System.out.println("Lecture refusee (capteur suspendu): " + ex.getMessage());
		}
		zoneCulture.reactiver();

		RapportProduction rc = system.genererRapportProduction(zoneCulture,
				LocalDate.now().minusDays(7), LocalDate.now());
		RapportProduction re = system.genererRapportProduction(zoneElevage,
				LocalDate.now().minusDays(7), LocalDate.now());
		RapportProduction ra = system.genererRapportProduction(zoneAquacole,
				LocalDate.now().minusDays(7), LocalDate.now());

		System.out.println(rc.getContenu());
		System.out.println(re.getContenu());
		System.out.println(ra.getContenu());
	}
}
