package smartfarming.system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.Capteur;
import smartfarming.capteurs.CapteurGPS;
import smartfarming.capteurs.CapteurNumerique;
import smartfarming.enums.NiveauGravite;
import smartfarming.mesures.PositionGPS;
import smartfarming.mesures.Releve;
import smartfarming.mesures.ReleveGPS;
import smartfarming.mesures.ReleveNumerique;
import smartfarming.rapports.RapportProduction;
import smartfarming.zones.Zone;
import smartfarming.zones.ZoneAquacole;
import smartfarming.zones.ZoneCulture;
import smartfarming.zones.ZoneElevage;

public class SmartFarmingSystem {
    private List<Zone> zones;
    private List<Releve> releves;
    private List<Alerte> alertes;
    private List<RapportProduction> rapports;

    public SmartFarmingSystem() {
        this.zones = new ArrayList<>();
        this.releves = new ArrayList<>();
        this.alertes = new ArrayList<>();
        this.rapports = new ArrayList<>();
    }

    public SmartFarmingSystem(List<Zone> zones, List<Releve> releves, List<Alerte> alertes,
            List<RapportProduction> rapports) {
        this.zones = new ArrayList<>();
        if (zones != null) {
            for (Zone zone : zones) {
                ajouterZone(zone);
            }
        }
        this.releves = (releves == null) ? new ArrayList<>() : releves;
        this.alertes = (alertes == null) ? new ArrayList<>() : alertes;
        this.rapports = (rapports == null) ? new ArrayList<>() : rapports;
    }

    public synchronized void ajouterZone(Zone zone) {
        Objects.requireNonNull(zone, "zone");
        if (zone.getCode() == null || zone.getCode().isBlank()) {
            throw new IllegalArgumentException("Le code de la zone est obligatoire.");
        }
        boolean codeExiste = zones.stream()
                .anyMatch(zoneExistante -> zone.getCode().equals(zoneExistante.getCode()));
        if (codeExiste) {
            throw new IllegalArgumentException("Une zone avec le code " + zone.getCode() + " existe deja.");
        }
        zones.add(zone);
    }

    public synchronized ReleveNumerique enregistrerReleveNumerique(CapteurNumerique capteur, double valeur) {
        Objects.requireNonNull(capteur, "capteur");
        ReleveNumerique releve = capteur.creerReleve(valeur);
        recevoirReleve(releve);
        return releve;
    }

    public synchronized ReleveGPS enregistrerReleveGPS(CapteurGPS capteur, double latitude, double longitude) {
        Objects.requireNonNull(capteur, "capteur");
        if (!capteur.estActif()) {
            throw new IllegalStateException("Capteur GPS inactif");
        }
        PositionGPS position = new PositionGPS(latitude, longitude);
        capteur.setLatitude(latitude);
        capteur.setLongitude(longitude);
        ReleveGPS releve = new ReleveGPS(UUID.randomUUID().toString(), LocalDateTime.now(), capteur, position);
        recevoirReleve(releve);
        return releve;
    }

    public synchronized void recevoirReleve(Releve releve) {
        Objects.requireNonNull(releve, "releve");
        releves.add(releve);
        Capteur capteur = releve.getCapteur();
        Zone zone = capteur == null ? null : capteur.getZone();
        if (zone != null) {
            zone.ajouterReleve(releve);
        }
        if (releve instanceof ReleveNumerique && capteur instanceof CapteurNumerique) {
            CapteurNumerique capteurNumerique = (CapteurNumerique) capteur;
            ReleveNumerique releveNumerique = (ReleveNumerique) releve;
            capteurNumerique.ajouterReleve(releveNumerique);
            Optional<Alerte> alerte = capteurNumerique.evaluerAlerte(releveNumerique);
            alerte.ifPresent(this::enregistrerAlerte);
        } else if (releve instanceof ReleveGPS && capteur instanceof CapteurGPS) {
            CapteurGPS capteurGPS = (CapteurGPS) capteur;
            ReleveGPS releveGPS = (ReleveGPS) releve;
            capteurGPS.ajouterReleve(releveGPS);
            PositionGPS position = releveGPS.getPosition();
            capteurGPS.setLatitude(position.getLatitude());
            capteurGPS.setLongitude(position.getLongitude());
            if (zone != null && !zone.contientPosition(position)) {
                releve.setNiveauGravite(NiveauGravite.Critique);
                String message = "Position GPS hors zone : " + position.getLatitude()
                        + ", " + position.getLongitude();
                enregistrerAlerte(Alerte.creerAlerte(zone, releve, message, NiveauGravite.Critique));
            } else {
                releve.setNiveauGravite(NiveauGravite.Normal);
            }
        }
    }

    public synchronized ResultatSimulation simulerCycleReleves(Random random) {
        Objects.requireNonNull(random, "random");
        int relevesAvant = releves.size();
        int alertesAvant = alertes.size();
        for (Zone zone : zones) {
            for (Capteur capteur : zone.getCapteurs()) {
                if (!capteur.estActif()) {
                    continue;
                }
                if (capteur instanceof CapteurNumerique) {
                    recevoirReleve(((CapteurNumerique) capteur).envoyerReleve(random));
                } else if (capteur instanceof CapteurGPS) {
                    recevoirReleve(((CapteurGPS) capteur).envoyerReleve(random));
                }
            }
        }
        return new ResultatSimulation(LocalDateTime.now(), releves.size() - relevesAvant,
                alertes.size() - alertesAvant);
    }

    public synchronized List<Releve> historiqueReleves(Zone zone) {
        Objects.requireNonNull(zone, "zone");
        return releves.stream()
                .filter(releve -> releve.getCapteur() != null && zone.equals(releve.getCapteur().getZone()))
                .collect(Collectors.toList());
    }

    public synchronized List<Releve> historiqueReleves(Capteur capteur) {
        Objects.requireNonNull(capteur, "capteur");
        return releves.stream()
                .filter(releve -> capteur.equals(releve.getCapteur()))
                .collect(Collectors.toList());
    }

    public synchronized List<Releve> historiqueReleves(LocalDateTime debut, LocalDateTime fin) {
        Objects.requireNonNull(debut, "debut");
        Objects.requireNonNull(fin, "fin");
        return releves.stream()
                .filter(releve -> releve.getDateHeure() != null)
                .filter(releve -> !releve.getDateHeure().isBefore(debut) && !releve.getDateHeure().isAfter(fin))
                .collect(Collectors.toList());
    }

    public synchronized List<Alerte> alertesActives() {
        return alertes.stream()
                .filter(alerte -> alerte.getStatut() != smartfarming.enums.StatutAlerte.Acquittee)
                .sorted(Comparator.comparing(Alerte::getDateHeure).reversed())
                .collect(Collectors.toList());
    }

    public synchronized List<Alerte> alertesFiltrees(Zone zone, LocalDateTime debut, LocalDateTime fin) {
        return alertes.stream()
                .filter(alerte -> zone == null || zone.equals(alerte.getZone()))
                .filter(alerte -> debut == null || (alerte.getDateHeure() != null
                        && !alerte.getDateHeure().isBefore(debut)))
                .filter(alerte -> fin == null || (alerte.getDateHeure() != null
                        && !alerte.getDateHeure().isAfter(fin)))
                .sorted(Comparator.comparing(Alerte::getDateHeure).reversed())
                .collect(Collectors.toList());
    }

    public synchronized RapportProduction genererRapportProduction(Zone zone, LocalDate debut, LocalDate fin) {
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(debut, "debut");
        Objects.requireNonNull(fin, "fin");
        RapportProduction rapport = new RapportProduction();
        StringBuilder contenu = new StringBuilder();
        contenu.append("Rapport de production pour la zone ")
            .append(zone.getClass().getSimpleName())
            .append(" [").append(zone.getCode()).append("] ")
            .append(zone.getNom())
            .append(" | Periode ")
            .append(debut).append(" -> ").append(fin);

        if (zone instanceof ZoneCulture) {
            ZoneCulture zoneCulture = (ZoneCulture) zone;
            contenu.append(" | Rendement: ").append(zoneCulture.getRendement()).append(" kg/ha")
                .append(" | Cultures: ").append(zoneCulture.getCultures().size());
        } else if (zone instanceof ZoneElevage) {
            ZoneElevage zoneElevage = (ZoneElevage) zone;
            contenu.append(" | Lait: ").append(zoneElevage.getRendementLaitier()).append(" L")
                .append(" | Oeufs: ").append(zoneElevage.getProductionOeufs())
                .append(" | Aliment: ").append(zoneElevage.getProgrammeAlimentation())
                .append(" | Animaux: ").append(zoneElevage.getAnimaux().size());
        } else if (zone instanceof ZoneAquacole) {
            ZoneAquacole zoneAquacole = (ZoneAquacole) zone;
            contenu.append(" | Recolte: ").append(zoneAquacole.getPoidsRecolte()).append(" kg")
                .append(" | Aliment: ").append(zoneAquacole.getProgrammeAlimentation())
                .append(" | Bassins: ").append(zoneAquacole.getBassins().size())
                .append(" | Total animaux: ").append(zoneAquacole.getNombreTotalAnimaux());
        }
        rapport.setId(UUID.randomUUID().toString());
        rapport.setDateGeneration(LocalDateTime.now());
        rapport.setDebutPeriode(debut);
        rapport.setFinPeriode(fin);
        rapport.setZone(zone);
        rapport.setContenu(contenu.toString());
        rapports.add(rapport);
        return rapport;
    }

    private synchronized void enregistrerAlerte(Alerte alerte) {
        alertes.add(alerte);
        Zone zone = alerte.getZone();
        if (zone != null) {
            zone.ajouterAlerte(alerte);
        }
    }

    public synchronized boolean supprimerAlerte(Alerte alerte) {
        if (alerte == null) {
            return false;
        }
        boolean removed = alertes.remove(alerte);
        Zone zone = alerte.getZone();
        if (zone != null) {
            zone.getAlertes().remove(alerte);
        }
        return removed;
    }

    public synchronized List<Zone> getZones() {
        return new ArrayList<>(zones);
    }

    public synchronized void setZones(List<Zone> zones) {
        this.zones = new ArrayList<>();
        if (zones != null) {
            for (Zone zone : zones) {
                ajouterZone(zone);
            }
        }
    }

    public synchronized List<Releve> getReleves() {
        return new ArrayList<>(releves);
    }

    public synchronized void setReleves(List<Releve> releves) {
        this.releves = releves;
    }

    public synchronized List<Alerte> getAlertes() {
        return new ArrayList<>(alertes);
    }

    public synchronized void setAlertes(List<Alerte> alertes) {
        this.alertes = alertes;
    }

    public synchronized List<RapportProduction> getRapports() {
        return new ArrayList<>(rapports);
    }

    public synchronized void setRapports(List<RapportProduction> rapports) {
        this.rapports = rapports;
    }
}
