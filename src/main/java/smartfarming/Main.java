package smartfarming;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.Capteur;
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
import smartfarming.entites.Animal;
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
import smartfarming.mesures.Releve;
import smartfarming.mesures.ReleveGPS;
import smartfarming.mesures.ReleveNumerique;
import smartfarming.rapports.RapportProduction;
import smartfarming.system.ResultatSimulation;
import smartfarming.system.SmartFarmingSystem;
import smartfarming.zones.Zone;
import smartfarming.zones.ZoneAquacole;
import smartfarming.zones.ZoneCulture;
import smartfarming.zones.ZoneElevage;

public class Main {
    private static final int INTERVALLE_SECONDS = 10;

    private final Scanner scanner = new Scanner(System.in);
    private final SmartFarmingSystem system = new SmartFarmingSystem();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Random simulationRandom = new Random(2026);
    private ScheduledFuture<?> simulationFuture;
    private int numeroCycle = 0;

    public static void main(String[] args) {
        new Main().lancer();
    }

    private void lancer() {
        afficherTitre("SmartFarming ESI");
        creerDonneesDemo();
        System.out.println("Demo initialisee: " + system.getZones().size()
                + " zones, " + totalCapteurs() + " capteurs.");

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            int choix = lireInt("Choix: ");
            try {
                switch (choix) {
                    case 1: demarrerSimulationPeriodique(); break;
                    case 2: arreterSimulation(); break;
                    case 3: executerCycleManuel(); break;
                    case 4: ajouterZone(); break;
                    case 5: ajouterCulture(); break;
                    case 6: ajouterAnimal(); break;
                    case 7: ajouterBassin(); break;
                    case 8: ajouterCapteur(); break;
                    case 9: afficherResumeZones(); break;
                    case 10: afficherDerniersReleves(); break;
                    case 11: afficherAlertesActives(); break;
                    case 12: afficherRapports(); break;
                    case 13: demontrerValidationsEtMaintenance(); break;
                    case 0:
                        continuer = false;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Erreur: " + ex.getMessage());
            }
        }
        arreterSimulation();
        scheduler.shutdownNow();
        scanner.close();
        System.out.println("Au revoir.");
    }

    private void afficherMenu() {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("1.  Demarrer la simulation periodique");
        System.out.println("2.  Arreter la simulation periodique");
        System.out.println("3.  Lancer un cycle manuel");
        System.out.println("4.  Ajouter une zone");
        System.out.println("5.  Ajouter une culture");
        System.out.println("6.  Ajouter un animal");
        System.out.println("7.  Ajouter un bassin");
        System.out.println("8.  Ajouter un capteur");
        System.out.println("9.  Afficher les zones");
        System.out.println("10. Afficher les derniers releves");
        System.out.println("11. Afficher les alertes actives");
        System.out.println("12. Afficher les rapports de production");
        System.out.println("13. Demonstration validations/maintenance");
        System.out.println("0.  Quitter");
        System.out.println("--------------------------------------------------");
    }

    private void creerDonneesDemo() {
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

        zoneAquacole.ajouterBassin(new Bassin(1, EspeceAquacole.POISSON, 120));

        zoneCulture.ajouterCapteur(new CapteurTemperatureAir("TA-01", "Temperature Air",
                StatutCapteur.Actif, null, UniteMesure.Celsius, 10.0, 30.0));
        zoneCulture.ajouterCapteur(new CapteurHumiditeAir("HA-01", "Humidite Air",
                StatutCapteur.Actif, null, UniteMesure.Pourcentage, 35.0, 85.0));
        zoneCulture.ajouterCapteur(new CapteurPluviometrie("PL-01", "Pluviometrie",
                StatutCapteur.Actif, null, UniteMesure.mm, 0.0, 50.0));
        zoneCulture.ajouterCapteur(new CapteurPHSol("PHS-01", "pH Sol",
                StatutCapteur.Actif, null, UniteMesure.pH, 5.5, 7.5));
        zoneCulture.ajouterCapteur(new CapteurHumiditeSol("HS-01", "Humidite Sol",
                StatutCapteur.Actif, null, UniteMesure.Pourcentage, 30.0, 80.0));
        zoneCulture.ajouterCapteur(new CapteurAzote("N-01", "Azote Sol",
                StatutCapteur.Actif, null, UniteMesure.gKg, 0.5, 3.0));

        zoneRuminants.ajouterCapteur(new CapteurTemperatureCorporelle("TC-01",
                "Temperature Corporelle", StatutCapteur.Actif, null,
                UniteMesure.Celsius, 37.5, 39.5, vache));
        zoneRuminants.ajouterCapteur(new CapteurActivite("ACT-01", "Activite",
                StatutCapteur.Actif, null, UniteMesure.stepmin, 10.0, 80.0, vache));
        zoneRuminants.ajouterCapteur(new CapteurGPS("GPS-01", "Collier GPS",
                StatutCapteur.Actif, null, 36.7550, 3.0420));

        zoneAquacole.ajouterCapteur(new CapteurTemperatureEau("TE-01", "Temperature Eau",
                StatutCapteur.Actif, null, UniteMesure.Celsius, 12.0, 28.0));
        zoneAquacole.ajouterCapteur(new CapteurOxygeneDissous("O-01", "Oxygene Dissous",
                StatutCapteur.Actif, null, UniteMesure.mgL, 5.0, 12.0));
        zoneAquacole.ajouterCapteur(new CapteurPHEau("PHE-01", "pH Eau",
                StatutCapteur.Actif, null, UniteMesure.pH, 6.5, 8.5));
    }

    private void demarrerSimulationPeriodique() {
        if (simulationFuture != null && !simulationFuture.isCancelled() && !simulationFuture.isDone()) {
            System.out.println("Simulation deja demarree.");
            return;
        }
        simulationFuture = scheduler.scheduleAtFixedRate(() -> {
            try {
                ResultatSimulation resultat = system.simulerCycleReleves(simulationRandom);
                numeroCycle++;
                System.out.println("\nCycle #" + numeroCycle
                        + " | Releves: +" + resultat.getNombreRelevesGeneres()
                        + " | Alertes: +" + resultat.getNombreAlertesGenerees()
                        + " | Total alertes actives: " + system.alertesActives().size());
                System.out.print("Choix: ");
            } catch (RuntimeException ex) {
                System.out.println("\nErreur simulation: " + ex.getMessage());
                System.out.print("Choix: ");
            }
        }, INTERVALLE_SECONDS, INTERVALLE_SECONDS, TimeUnit.SECONDS);
        System.out.println("Simulation periodique demarree (" + INTERVALLE_SECONDS + " secondes).");
    }

    private void arreterSimulation() {
        if (simulationFuture != null) {
            simulationFuture.cancel(false);
            simulationFuture = null;
            System.out.println("Simulation periodique arretee.");
        }
    }

    private void executerCycleManuel() {
        ResultatSimulation resultat = system.simulerCycleReleves(simulationRandom);
        numeroCycle++;
        System.out.println("Cycle manuel #" + numeroCycle
                + " | Releves: +" + resultat.getNombreRelevesGeneres()
                + " | Alertes: +" + resultat.getNombreAlertesGenerees()
                + " | Total alertes actives: " + system.alertesActives().size());
    }

    private void ajouterZone() {
        afficherTitre("Ajouter une zone");
        System.out.println("1. Zone culture");
        System.out.println("2. Zone elevage");
        System.out.println("3. Zone aquacole");
        int type = lireInt("Type: ");
        String code = lireTexte("Code: ");
        String nom = lireTexte("Nom: ");
        double latitudeMin = lireDouble("Latitude min: ");
        double latitudeMax = lireDouble("Latitude max: ");
        double longitudeMin = lireDouble("Longitude min: ");
        double longitudeMax = lireDouble("Longitude max: ");

        Zone zone;
        if (type == 1) {
            zone = new ZoneCulture(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax);
        } else if (type == 2) {
            TypeElevage typeElevage = choisirEnum(TypeElevage.class, "Type elevage");
            ProgrammeAlimentation programme = lireProgrammeAlimentation();
            zone = new ZoneElevage(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax,
                    typeElevage, programme);
        } else if (type == 3) {
            ProgrammeAlimentation programme = lireProgrammeAlimentation();
            zone = new ZoneAquacole(code, nom, latitudeMin, latitudeMax, longitudeMin, longitudeMax, programme);
        } else {
            System.out.println("Type invalide.");
            return;
        }
        system.ajouterZone(zone);
        System.out.println("Zone ajoutee.");
    }

    private void ajouterCulture() {
        ZoneCulture zone = choisirZone(ZoneCulture.class, "Choisir une zone culture");
        if (zone == null) {
            return;
        }
        String id = lireTexte("Id culture: ");
        TypeCulture type = choisirEnum(TypeCulture.class, "Type culture");
        int planteeDepuis = lireInt("Plantee depuis combien de jours: ");
        int recolteDans = lireInt("Recolte prevue dans combien de jours: ");
        double phMin = lireDouble("pH min: ");
        double phMax = lireDouble("pH max: ");
        double humiditeMin = lireDouble("Humidite min: ");
        double humiditeMax = lireDouble("Humidite max: ");
        zone.ajouterCulture(new Culture(id, type, LocalDate.now().minusDays(planteeDepuis),
                LocalDate.now().plusDays(recolteDans), phMin, phMax, humiditeMin, humiditeMax));
    }

    private void ajouterAnimal() {
        ZoneElevage zone = choisirZone(ZoneElevage.class, "Choisir une zone elevage");
        if (zone == null) {
            return;
        }
        int id = lireInt("Id animal: ");
        EspeceAnimale espece = choisirEspeceCompatible(zone.getTypeElevage());
        int age = lireInt("Age en mois: ");
        double poids = lireDouble("Poids en kg: ");
        Animal animal = zone.getTypeElevage() == TypeElevage.RUMINANTS
                ? new Ruminant(id, espece, age, poids)
                : new Volaille(id, espece, age, poids);
        zone.ajouterAnimal(animal);
    }

    private void ajouterBassin() {
        ZoneAquacole zone = choisirZone(ZoneAquacole.class, "Choisir une zone aquacole");
        if (zone == null) {
            return;
        }
        int id = lireInt("Id bassin: ");
        EspeceAquacole espece = choisirEnum(EspeceAquacole.class, "Espece aquacole");
        int nombre = lireInt("Nombre d'animaux: ");
        zone.ajouterBassin(new Bassin(id, espece, nombre));
    }

    private void ajouterCapteur() {
        Zone zone = choisirZone(Zone.class, "Choisir une zone");
        if (zone == null) {
            return;
        }
        Capteur capteur = null;
        if (zone instanceof ZoneCulture) {
            capteur = creerCapteurCulture((ZoneCulture) zone);
        } else if (zone instanceof ZoneElevage) {
            capteur = creerCapteurElevage((ZoneElevage) zone);
        } else if (zone instanceof ZoneAquacole) {
            capteur = creerCapteurAquacole((ZoneAquacole) zone);
        }
        if (capteur != null) {
            zone.ajouterCapteur(capteur);
            System.out.println("Capteur ajoute.");
        }
    }

    private Capteur creerCapteurCulture(ZoneCulture zone) {
        System.out.println("1. Temperature air");
        System.out.println("2. Humidite air");
        System.out.println("3. Pluviometrie");
        System.out.println("4. pH sol");
        System.out.println("5. Humidite sol");
        System.out.println("6. Azote sol");
        int choix = lireInt("Type capteur: ");
        String id = lireTexte("Id capteur: ");
        String nom = lireTexte("Nom capteur: ");
        double min = lireDouble("Seuil min: ");
        double max = lireDouble("Seuil max: ");
        switch (choix) {
            case 1: return new CapteurTemperatureAir(id, nom, StatutCapteur.Actif, null, UniteMesure.Celsius, min, max);
            case 2: return new CapteurHumiditeAir(id, nom, StatutCapteur.Actif, null, UniteMesure.Pourcentage, min, max);
            case 3: return new CapteurPluviometrie(id, nom, StatutCapteur.Actif, null, UniteMesure.mm, min, max);
            case 4: return new CapteurPHSol(id, nom, StatutCapteur.Actif, null, UniteMesure.pH, min, max);
            case 5: return new CapteurHumiditeSol(id, nom, StatutCapteur.Actif, null, UniteMesure.Pourcentage, min, max);
            case 6: return new CapteurAzote(id, nom, StatutCapteur.Actif, null, UniteMesure.gKg, min, max);
            default:
                System.out.println("Type invalide.");
                return null;
        }
    }

    private Capteur creerCapteurElevage(ZoneElevage zone) {
        System.out.println("1. Temperature corporelle");
        System.out.println("2. Activite");
        System.out.println("3. GPS");
        int choix = lireInt("Type capteur: ");
        String id = lireTexte("Id capteur: ");
        String nom = lireTexte("Nom capteur: ");
        if (choix == 3) {
            double latitude = lireDouble("Latitude initiale: ");
            double longitude = lireDouble("Longitude initiale: ");
            return new CapteurGPS(id, nom, StatutCapteur.Actif, null, latitude, longitude);
        }
        Animal animal = choisirAnimal(zone);
        if (animal == null) {
            return null;
        }
        double min = lireDouble("Seuil min: ");
        double max = lireDouble("Seuil max: ");
        if (choix == 1) {
            return new CapteurTemperatureCorporelle(id, nom, StatutCapteur.Actif, null,
                    UniteMesure.Celsius, min, max, animal);
        }
        if (choix == 2) {
            return new CapteurActivite(id, nom, StatutCapteur.Actif, null,
                    UniteMesure.stepmin, min, max, animal);
        }
        System.out.println("Type invalide.");
        return null;
    }

    private Capteur creerCapteurAquacole(ZoneAquacole zone) {
        System.out.println("1. Temperature eau");
        System.out.println("2. Oxygene dissous");
        System.out.println("3. pH eau");
        int choix = lireInt("Type capteur: ");
        String id = lireTexte("Id capteur: ");
        String nom = lireTexte("Nom capteur: ");
        double min = lireDouble("Seuil min: ");
        double max = lireDouble("Seuil max: ");
        switch (choix) {
            case 1: return new CapteurTemperatureEau(id, nom, StatutCapteur.Actif, null, UniteMesure.Celsius, min, max);
            case 2: return new CapteurOxygeneDissous(id, nom, StatutCapteur.Actif, null, UniteMesure.mgL, min, max);
            case 3: return new CapteurPHEau(id, nom, StatutCapteur.Actif, null, UniteMesure.pH, min, max);
            default:
                System.out.println("Type invalide.");
                return null;
        }
    }

    private void afficherResumeZones() {
        afficherTitre("Zones");
        for (Zone zone : system.getZones()) {
            zone.afficherResume();
            System.out.println("   Capteurs: " + zone.getCapteurs().size()
                    + " | Releves: " + zone.getReleves().size()
                    + " | Alertes: " + zone.getAlertes().size());
        }
    }

    private void afficherDerniersReleves() {
        afficherTitre("Derniers releves");
        List<Releve> releves = system.getReleves();
        int debut = Math.max(0, releves.size() - 12);
        for (int i = debut; i < releves.size(); i++) {
            Releve releve = releves.get(i);
            String capteur = releve.getCapteur() == null ? "N/A" : releve.getCapteur().getId();
            if (releve instanceof ReleveNumerique) {
                ReleveNumerique rn = (ReleveNumerique) releve;
                System.out.println(releve.getDateHeure() + " | " + capteur + " | "
                        + arrondir(rn.getValeur()) + " " + rn.getUnite()
                        + " | " + releve.getNiveauGravite());
            } else if (releve instanceof ReleveGPS) {
                ReleveGPS rg = (ReleveGPS) releve;
                System.out.println(releve.getDateHeure() + " | " + capteur + " | GPS "
                        + arrondir(rg.getPosition().getLatitude()) + ", "
                        + arrondir(rg.getPosition().getLongitude())
                        + " | " + releve.getNiveauGravite());
            }
        }
        if (releves.isEmpty()) {
            System.out.println("Aucun releve.");
        }
    }

    private void afficherAlertesActives() {
        afficherTitre("Alertes actives");
        List<Alerte> alertes = system.alertesActives();
        if (alertes.isEmpty()) {
            System.out.println("Aucune alerte active.");
            return;
        }
        int index = 1;
        for (Alerte alerte : alertes) {
            System.out.println(index++ + ". " + alerte.getStatut()
                    + " | " + alerte.getMessage()
                    + " | Zone: " + (alerte.getZone() == null ? "N/A" : alerte.getZone().getCode()));
        }
    }

    private void afficherRapports() {
        afficherTitre("Rapports");
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        for (Zone zone : system.getZones()) {
            RapportProduction rapport = system.genererRapportProduction(zone, debut, fin);
            System.out.println(rapport.getContenu());
        }
    }

    private void demontrerValidationsEtMaintenance() {
        afficherTitre("Validations et maintenance");
        try {
            system.ajouterZone(new ZoneCulture("ZC-01", "Zone Duplicate",
                    36.7000, 36.7100, 3.0000, 3.0100));
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation zone: " + ex.getMessage());
        }

        ZoneCulture zoneCulture = choisirZone(ZoneCulture.class, "Choisir une zone culture pour tester les capteurs");
        if (zoneCulture != null && !zoneCulture.getCapteurs().isEmpty()) {
            Capteur capteur = zoneCulture.getCapteurs().get(0);
            try {
                zoneCulture.ajouterCapteur(new CapteurTemperatureAir(capteur.getId(),
                        "Capteur duplicate", StatutCapteur.Actif, null, UniteMesure.Celsius, 10.0, 30.0));
            } catch (IllegalArgumentException ex) {
                System.out.println("Validation capteur: " + ex.getMessage());
            }

            capteur.signalerDefaillance();
            System.out.println("Statut avant suspension zone: " + capteur.getStatut());
            zoneCulture.suspendre();
            zoneCulture.reactiver();
            System.out.println("Statut apres reactivation zone: " + capteur.getStatut());
            capteur.reparer();
            System.out.println("Statut apres maintenance: " + capteur.getStatut());
        }
    }

    private ProgrammeAlimentation lireProgrammeAlimentation() {
        String typeAliment = lireTexte("Type aliment: ");
        double quantite = lireDouble("Quantite par repas: ");
        return new ProgrammeAlimentation(typeAliment, quantite);
    }

    private Animal choisirAnimal(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) {
            System.out.println("Aucun animal dans cette zone.");
            return null;
        }
        for (int i = 0; i < zone.getAnimaux().size(); i++) {
            Animal animal = zone.getAnimaux().get(i);
            System.out.println((i + 1) + ". #" + animal.getId() + " " + animal.getEspece());
        }
        int choix = lireInt("Animal: ") - 1;
        if (choix < 0 || choix >= zone.getAnimaux().size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return zone.getAnimaux().get(choix);
    }

    private EspeceAnimale choisirEspeceCompatible(TypeElevage typeElevage) {
        EspeceAnimale[] especes = typeElevage == TypeElevage.RUMINANTS
                ? new EspeceAnimale[] { EspeceAnimale.VACHE, EspeceAnimale.MOUTON, EspeceAnimale.CHEVRE }
                : new EspeceAnimale[] { EspeceAnimale.POULET, EspeceAnimale.DINDE };
        for (int i = 0; i < especes.length; i++) {
            System.out.println((i + 1) + ". " + especes[i]);
        }
        int choix = lireInt("Espece: ") - 1;
        if (choix < 0 || choix >= especes.length) {
            throw new IllegalArgumentException("Espece invalide.");
        }
        return especes[choix];
    }

    private <T extends Zone> T choisirZone(Class<T> type, String titre) {
        afficherTitre(titre);
        List<Zone> zones = system.getZones();
        int count = 0;
        for (Zone zone : zones) {
            if (type.isInstance(zone)) {
                count++;
                System.out.println(count + ". [" + zone.getCode() + "] " + zone.getNom()
                        + " (" + zone.getClass().getSimpleName() + ")");
            }
        }
        if (count == 0) {
            System.out.println("Aucune zone compatible.");
            return null;
        }
        int choix = lireInt("Zone: ");
        count = 0;
        for (Zone zone : zones) {
            if (type.isInstance(zone)) {
                count++;
                if (count == choix) {
                    return type.cast(zone);
                }
            }
        }
        System.out.println("Choix invalide.");
        return null;
    }

    private <E extends Enum<E>> E choisirEnum(Class<E> enumClass, String label) {
        E[] valeurs = enumClass.getEnumConstants();
        for (int i = 0; i < valeurs.length; i++) {
            System.out.println((i + 1) + ". " + valeurs[i]);
        }
        int choix = lireInt(label + ": ") - 1;
        if (choix < 0 || choix >= valeurs.length) {
            throw new IllegalArgumentException(label + " invalide.");
        }
        return valeurs[choix];
    }

    private int totalCapteurs() {
        int total = 0;
        for (Zone zone : system.getZones()) {
            total += zone.getCapteurs().size();
        }
        return total;
    }

    private int lireInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Entier attendu.");
            }
        }
    }

    private double lireDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException ex) {
                System.out.println("Nombre attendu.");
            }
        }
    }

    private String lireTexte(String prompt) {
        String texte;
        do {
            System.out.print(prompt);
            texte = scanner.nextLine().trim();
        } while (texte.isEmpty());
        return texte;
    }

    private void afficherTitre(String titre) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(titre);
        System.out.println("==================================================");
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
