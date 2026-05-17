package smartfarming;

import java.time.LocalDate;
import java.util.Random;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.CapteurActivite;
import smartfarming.capteurs.CapteurAzote;
import smartfarming.capteurs.CapteurGPS;
import smartfarming.capteurs.CapteurHumidite;
import smartfarming.capteurs.CapteurNumerique;
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
		Random random = new Random(2026);

		ZoneCulture zoneCulture = new ZoneCulture("ZC-01", "Zone Culture 1",
				36.7300, 36.7400, 3.0000, 3.0200);
		ZoneElevage zoneElevage = new ZoneElevage("ZE-01", "Zone Elevage 1",
				36.7500, 36.7600, 3.0350, 3.0500);
		ZoneAquacole zoneAquacole = new ZoneAquacole("ZA-01", "Zone Aquacole 1",
				36.7200, 36.7280, 3.0250, 3.0400);
		system.ajouterZone(zoneCulture);
		system.ajouterZone(zoneElevage);
		system.ajouterZone(zoneAquacole);

		Culture culture = new Culture("C-01", TypeCulture.BLE,
				LocalDate.now().minusDays(10), LocalDate.now().plusDays(60),
				5.5, 7.0, 35.0, 70.0);
		zoneCulture.ajouterCulture(culture);

		Animal animal = new Animal(1, EspeceAnimale.VACHE, 24, 320.0);
		zoneElevage.ajouterAnimal(animal);
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

		zoneCulture.ajouterCapteur(tempAir);
		zoneCulture.ajouterCapteur(humidite);
		zoneCulture.ajouterCapteur(phSol);
		zoneCulture.ajouterCapteur(pluie);
		zoneCulture.ajouterCapteur(azote);

		CapteurActivite activite = new CapteurActivite("A-01", "Activite",
				StatutCapteur.Actif, null, UniteMesure.stepmin, 10.0, 80.0);
		CapteurPoids poids = new CapteurPoids("P-01", "Poids",
				StatutCapteur.Actif, null, UniteMesure.gKg, 200.0, 700.0);
		CapteurGPS gps = new CapteurGPS("G-01", "Collier GPS",
				StatutCapteur.Actif, null, 0.0, 0.0);

		zoneElevage.ajouterCapteur(activite);
		zoneElevage.ajouterCapteur(poids);
		zoneElevage.ajouterCapteur(gps);

		CapteurTemperature tempEau = new CapteurTemperature("T-02", "Temp Eau",
				StatutCapteur.Actif, null, UniteMesure.Celsius, 12.0, 28.0);
		CapteurOxygeneDissous oxygene = new CapteurOxygeneDissous("O-01", "Oxygene",
				StatutCapteur.Actif, null, UniteMesure.mgL, 5.0, 12.0);
		CapteurPH phEau = new CapteurPH("PH-02", "pH Eau",
				StatutCapteur.Actif, null, UniteMesure.pH, 6.5, 8.5);

		zoneAquacole.ajouterCapteur(tempEau);
		zoneAquacole.ajouterCapteur(oxygene);
		zoneAquacole.ajouterCapteur(phEau);

		enregistrerLectureAuto(system, random, tempAir, false);
		enregistrerLectureAuto(system, random, humidite, false);
		enregistrerLectureAuto(system, random, phSol, true);
		enregistrerLectureAuto(system, random, pluie, false);
		enregistrerLectureAuto(system, random, azote, true);

		enregistrerLectureAuto(system, random, activite, false);
		enregistrerLectureAuto(system, random, poids, false);
		enregistrerPositionAuto(system, random, gps, 36.7550, 3.0420, false);
		enregistrerPositionAuto(system, random, gps, 36.7550, 3.0420, true);

		enregistrerLectureAuto(system, random, tempEau, false);
		enregistrerLectureAuto(system, random, oxygene, true);
		enregistrerLectureAuto(system, random, phEau, false);

		System.out.println("Alertes actives: " + system.alertesActives().size());
		if (!system.alertesActives().isEmpty()) {
			Alerte alerte = system.alertesActives().get(0);
			alerte.acquitter();
		}
		System.out.println("Alertes actives apres acquittement: " + system.alertesActives().size());

		humidite.signalerDefaillance();
		System.out.println("Statut humidite avant suspension: " + humidite.getStatut());
		zoneCulture.suspendre();
		try {
			enregistrerLectureAuto(system, random, tempAir, false);
		} catch (IllegalStateException ex) {
			System.out.println("Lecture refusee (capteur suspendu): " + ex.getMessage());
		}
		zoneCulture.reactiver();
		System.out.println("Statut humidite apres reactivation zone: " + humidite.getStatut());
		humidite.reparer();
		System.out.println("Statut humidite apres maintenance: " + humidite.getStatut());
		enregistrerLectureAuto(system, random, humidite, false);

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

	private static void enregistrerLectureAuto(SmartFarmingSystem system, Random random,
			CapteurNumerique capteur, boolean horsSeuil) {
		double valeur = horsSeuil ? genererValeurHorsSeuil(random, capteur)
				: genererValeurNormale(random, capteur);
		system.enregistrerReleveNumerique(capteur, valeur);
		System.out.println("Lecture automatique " + capteur.getNom() + " = "
				+ arrondir(valeur) + " " + capteur.getUnite());
	}

	private static double genererValeurNormale(Random random, CapteurNumerique capteur) {
		double amplitude = capteur.getSeuilMax() - capteur.getSeuilMin();
		return capteur.getSeuilMin() + amplitude * (0.2 + random.nextDouble() * 0.6);
	}

	private static double genererValeurHorsSeuil(Random random, CapteurNumerique capteur) {
		double amplitude = capteur.getSeuilMax() - capteur.getSeuilMin();
		double marge = Math.max(amplitude * 0.1, 0.1);
		if (random.nextBoolean()) {
			return capteur.getSeuilMax() + marge;
		}
		return capteur.getSeuilMin() - marge;
	}

	private static void enregistrerPositionAuto(SmartFarmingSystem system, Random random,
			CapteurGPS gps, double latitudeBase, double longitudeBase, boolean horsZone) {
		double latitude = latitudeBase + (random.nextDouble() - 0.5) * 0.004;
		double longitude = longitudeBase + (random.nextDouble() - 0.5) * 0.004;
		if (horsZone) {
			latitude = gps.getZone().getLatitudeMax() + 0.01;
			longitude = gps.getZone().getLongitudeMax() + 0.01;
		}
		system.enregistrerReleveGPS(gps, latitude, longitude);
		System.out.println("Position GPS automatique = " + arrondir(latitude)
				+ ", " + arrondir(longitude));
	}

	private static double arrondir(double valeur) {
		return Math.round(valeur * 100.0) / 100.0;
	}
}
