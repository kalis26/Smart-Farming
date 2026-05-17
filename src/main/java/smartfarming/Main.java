package smartfarming;

import java.time.LocalDate;
import java.util.Random;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.CapteurActivite;
import smartfarming.capteurs.CapteurAzote;
import smartfarming.capteurs.CapteurGPS;
import smartfarming.capteurs.CapteurHumiditeAir;
import smartfarming.capteurs.CapteurHumiditeSol;
import smartfarming.capteurs.CapteurOxygeneDissous;
import smartfarming.capteurs.CapteurPHEau;
import smartfarming.capteurs.CapteurPHSol;
import smartfarming.capteurs.CapteurPluviometrie;
import smartfarming.capteurs.CapteurTemperatureAir;
import smartfarming.capteurs.CapteurTemperatureCorporelle;
import smartfarming.capteurs.CapteurTemperatureEau;
import smartfarming.entites.Bassin;
import smartfarming.entites.Culture;
import smartfarming.entites.ProgrammeAlimentation;
import smartfarming.entites.Ruminant;
import smartfarming.entites.Volaille;
import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.EspeceAquacole;
import smartfarming.enums.EtatSante;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.TypeCulture;
import smartfarming.enums.TypeElevage;
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
        ZoneElevage zoneRuminants = new ZoneElevage("ZR-01", "Zone Ruminants",
                36.7500, 36.7600, 3.0350, 3.0500, TypeElevage.RUMINANTS,
                new ProgrammeAlimentation("Foin", 6.0));
        ZoneAquacole zoneAquacole = new ZoneAquacole("ZA-01", "Zone Aquacole 1",
                36.7200, 36.7280, 3.0250, 3.0400,
                new ProgrammeAlimentation("Granules", 0.5));

        system.ajouterZone(zoneCulture);
        system.ajouterZone(zoneRuminants);
        system.ajouterZone(zoneAquacole);

        Culture culture = new Culture("C-01", TypeCulture.BLE,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(60),
                5.5, 7.0, 35.0, 70.0);
        zoneCulture.ajouterCulture(culture);

        Ruminant vache = new Ruminant(1, EspeceAnimale.VACHE, 24, 320.0);
        zoneRuminants.ajouterAnimal(vache);
        vache.mettreAJourSante(EtatSante.MALADE);
        vache.enregistrerPoids(315.0);
        System.out.println("Evenements sanitaires enregistres: " + vache.getHistoriqueSante().size());

        try {
            zoneRuminants.ajouterAnimal(new Volaille(2, EspeceAnimale.POULET, 6, 2.4));
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation elevage: " + ex.getMessage());
        }

        Bassin bassin = new Bassin(1, EspeceAquacole.POISSON, 120);
        zoneAquacole.ajouterBassin(bassin);

        CapteurTemperatureAir tempAir = new CapteurTemperatureAir("TA-01", "Temperature Air",
                StatutCapteur.Actif, null, UniteMesure.Celsius, 10.0, 30.0);
        CapteurHumiditeAir humiditeAir = new CapteurHumiditeAir("HA-01", "Humidite Air",
                StatutCapteur.Actif, null, UniteMesure.Pourcentage, 35.0, 85.0);
        CapteurPluviometrie pluie = new CapteurPluviometrie("PL-01", "Pluviometrie",
                StatutCapteur.Actif, null, UniteMesure.mm, 0.0, 50.0);
        CapteurPHSol phSol = new CapteurPHSol("PHS-01", "pH Sol",
                StatutCapteur.Actif, null, UniteMesure.pH, 5.5, 7.5);
        CapteurHumiditeSol humiditeSol = new CapteurHumiditeSol("HS-01", "Humidite Sol",
                StatutCapteur.Actif, null, UniteMesure.Pourcentage, 30.0, 80.0);
        CapteurAzote azote = new CapteurAzote("N-01", "Azote Sol",
                StatutCapteur.Actif, null, UniteMesure.gKg, 0.5, 3.0);

        zoneCulture.ajouterCapteur(tempAir);
        zoneCulture.ajouterCapteur(humiditeAir);
        zoneCulture.ajouterCapteur(pluie);
        zoneCulture.ajouterCapteur(phSol);
        zoneCulture.ajouterCapteur(humiditeSol);
        zoneCulture.ajouterCapteur(azote);

        CapteurTemperatureCorporelle temperatureCorporelle =
                new CapteurTemperatureCorporelle("TC-01", "Temperature Corporelle",
                        StatutCapteur.Actif, null, UniteMesure.Celsius, 37.5, 39.5, vache);
        CapteurActivite activite = new CapteurActivite("ACT-01", "Activite",
                StatutCapteur.Actif, null, UniteMesure.stepmin, 10.0, 80.0, vache);
        CapteurGPS gps = new CapteurGPS("GPS-01", "Collier GPS",
                StatutCapteur.Actif, null, 36.7550, 3.0420);

        zoneRuminants.ajouterCapteur(temperatureCorporelle);
        zoneRuminants.ajouterCapteur(activite);
        zoneRuminants.ajouterCapteur(gps);

        CapteurTemperatureEau tempEau = new CapteurTemperatureEau("TE-01", "Temperature Eau",
                StatutCapteur.Actif, null, UniteMesure.Celsius, 12.0, 28.0);
        CapteurOxygeneDissous oxygene = new CapteurOxygeneDissous("O-01", "Oxygene Dissous",
                StatutCapteur.Actif, null, UniteMesure.mgL, 5.0, 12.0);
        CapteurPHEau phEau = new CapteurPHEau("PHE-01", "pH Eau",
                StatutCapteur.Actif, null, UniteMesure.pH, 6.5, 8.5);

        zoneAquacole.ajouterCapteur(tempEau);
        zoneAquacole.ajouterCapteur(oxygene);
        zoneAquacole.ajouterCapteur(phEau);

        System.out.println("\n--- Cycle periodique automatique ---");
        system.simulerCycleReleves(random);
        System.out.println("Releves systeme: " + system.getReleves().size());
        System.out.println("Historique capteur pH sol: " + phSol.getHistorique().size());

        system.recevoirReleve(phSol.creerReleve(7.6));
        system.recevoirReleve(azote.creerReleve(4.0));
        system.enregistrerReleveGPS(gps, zoneRuminants.getLatitudeMax() + 0.01,
                zoneRuminants.getLongitudeMax() + 0.01);

        System.out.println("Alertes actives: " + system.alertesActives().size());
        afficherAlertes(system);
        if (!system.alertesActives().isEmpty()) {
            Alerte alerte = system.alertesActives().get(0);
            alerte.acquitter();
        }
        System.out.println("Alertes actives apres acquittement: " + system.alertesActives().size());

        humiditeSol.signalerDefaillance();
        System.out.println("Statut humidite sol avant suspension: " + humiditeSol.getStatut());
        zoneCulture.suspendre();
        try {
            system.recevoirReleve(tempAir.envoyerReleve(random));
        } catch (IllegalStateException ex) {
            System.out.println("Lecture refusee (capteur suspendu): " + ex.getMessage());
        }
        zoneCulture.reactiver();
        System.out.println("Statut humidite sol apres reactivation zone: " + humiditeSol.getStatut());
        humiditeSol.reparer();
        System.out.println("Statut humidite sol apres maintenance: " + humiditeSol.getStatut());

        zoneCulture.enregistrerProduction(48.0);
        zoneRuminants.enregistrerProduction(26.0, 0.0);
        zoneAquacole.enregistrerProduction(180.0);

        RapportProduction rc = system.genererRapportProduction(zoneCulture,
                LocalDate.now().minusDays(7), LocalDate.now());
        RapportProduction rr = system.genererRapportProduction(zoneRuminants,
                LocalDate.now().minusDays(7), LocalDate.now());
        RapportProduction ra = system.genererRapportProduction(zoneAquacole,
                LocalDate.now().minusDays(7), LocalDate.now());

        System.out.println(rc.getContenu());
        System.out.println(rr.getContenu());
        System.out.println(ra.getContenu());
    }

    private static void afficherAlertes(SmartFarmingSystem system) {
        for (Alerte alerte : system.alertesActives()) {
            System.out.println("- " + alerte.getStatut() + " | " + alerte.getMessage());
        }
    }
}
