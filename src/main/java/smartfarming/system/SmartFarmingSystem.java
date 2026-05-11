package smartfarming.system;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import smartfarming.alertes.Alerte;
import smartfarming.capteurs.Capteur;
import smartfarming.capteurs.CapteurNumerique;
import smartfarming.entites.Animal;
import smartfarming.entites.Culture;
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
        this.zones = (zones == null) ? new ArrayList<>() : zones;
        this.releves = (releves == null) ? new ArrayList<>() : releves;
        this.alertes = (alertes == null) ? new ArrayList<>() : alertes;
        this.rapports = (rapports == null) ? new ArrayList<>() : rapports;
    }

    public void ajouterZone(Zone zone) {
        Objects.requireNonNull(zone, "zone");
        zones.add(zone);
    }

    public void ajouterCapteur(Zone zone, Capteur capteur) {
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(capteur, "capteur");
        zone.ajouterCapteur(capteur);
    }

    public void ajouterCulture(ZoneCulture zone, Culture culture) {
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(culture, "culture");
        zone.ajouterCulture(culture);
    }

    public void ajouterAnimal(ZoneElevage zone, Animal animal) {
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(animal, "animal");
        zone.ajouterAnimal(animal);
    }

    public ReleveNumerique enregistrerReleveNumerique(CapteurNumerique capteur, double valeur) {
        Objects.requireNonNull(capteur, "capteur");
        if (!capteur.estActif()) {
            throw new IllegalStateException("Capteur numerique inactif");
        }
        ReleveNumerique releve = capteur.creerReleve(valeur);
        releves.add(releve);
        Zone zone = capteur.getZone();
        if (zone != null) {
            zone.ajouterReleve(releve);
        }
        Optional<Alerte> alerte = capteur.evaluerAlerte(releve);
        alerte.ifPresent(this::enregistrerAlerte);
        return releve;
    }

    public ReleveGPS enregistrerReleveGPS(Capteur capteur, double latitude, double longitude) {
        Objects.requireNonNull(capteur, "capteur");
        if (!capteur.estActif()) {
            throw new IllegalStateException("Capteur GPS inactif");
        }
        PositionGPS position = new PositionGPS(latitude, longitude);
        ReleveGPS releve = new ReleveGPS(UUID.randomUUID().toString(), LocalDateTime.now(), capteur, position);
        releves.add(releve);
        Zone zone = capteur.getZone();
        if (zone != null) {
            zone.ajouterReleve(releve);
        }
        return releve;
    }

    public List<Releve> historiqueReleves(Zone zone) {
        Objects.requireNonNull(zone, "zone");
        return releves.stream()
                .filter(releve -> releve.getCapteur() != null && zone.equals(releve.getCapteur().getZone()))
                .collect(Collectors.toList());
    }

    public List<Releve> historiqueReleves(Capteur capteur) {
        Objects.requireNonNull(capteur, "capteur");
        return releves.stream()
                .filter(releve -> capteur.equals(releve.getCapteur()))
                .collect(Collectors.toList());
    }

    public List<Releve> historiqueReleves(LocalDateTime debut, LocalDateTime fin) {
        Objects.requireNonNull(debut, "debut");
        Objects.requireNonNull(fin, "fin");
        return releves.stream()
                .filter(releve -> releve.getDateHeure() != null)
                .filter(releve -> !releve.getDateHeure().isBefore(debut) && !releve.getDateHeure().isAfter(fin))
                .collect(Collectors.toList());
    }

    public List<Alerte> alertesActives() {
        return alertes.stream()
                .filter(alerte -> alerte.getStatut() != smartfarming.enums.StatutAlerte.Acquittee)
                .sorted(Comparator.comparing(Alerte::getDateHeure).reversed())
                .collect(Collectors.toList());
    }

    public List<Alerte> alertesFiltrees(Zone zone, LocalDateTime debut, LocalDateTime fin) {
        return alertes.stream()
                .filter(alerte -> zone == null || zone.equals(alerte.getZone()))
                .filter(alerte -> debut == null || (alerte.getDateHeure() != null
                        && !alerte.getDateHeure().isBefore(debut)))
                .filter(alerte -> fin == null || (alerte.getDateHeure() != null
                        && !alerte.getDateHeure().isAfter(fin)))
                .sorted(Comparator.comparing(Alerte::getDateHeure).reversed())
                .collect(Collectors.toList());
    }

    public RapportProduction genererRapportProduction(Zone zone, LocalDate debut, LocalDate fin) {
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
                .append(" | Animaux: ").append(zoneElevage.getAnimaux().size());
        } else if (zone instanceof ZoneAquacole) {
            ZoneAquacole zoneAquacole = (ZoneAquacole) zone;
            contenu.append(" | Recolte: ").append(zoneAquacole.getPoidsRecolte()).append(" kg")
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

    private void enregistrerAlerte(Alerte alerte) {
        alertes.add(alerte);
        Zone zone = alerte.getZone();
        if (zone != null) {
            zone.ajouterAlerte(alerte);
        }
    }

    public boolean supprimerAlerte(Alerte alerte) {
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

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Releve> getReleves() {
        return releves;
    }

    public void setReleves(List<Releve> releves) {
        this.releves = releves;
    }

    public List<Alerte> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<Alerte> alertes) {
        this.alertes = alertes;
    }

    public List<RapportProduction> getRapports() {
        return rapports;
    }

    public void setRapports(List<RapportProduction> rapports) {
        this.rapports = rapports;
    }
}
