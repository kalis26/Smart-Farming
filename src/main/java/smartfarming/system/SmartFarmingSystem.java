package smartfarming.system;

import java.util.List;

import smartfarming.alertes.Alerte;
import smartfarming.mesures.Releve;
import smartfarming.rapports.RapportProduction;
import smartfarming.zones.Zone;

public class SmartFarmingSystem {
    private List<Zone> zones;
    private List<Releve> releves;
    private List<Alerte> alertes;
    private List<RapportProduction> rapports;

    public SmartFarmingSystem() {
    }

    public SmartFarmingSystem(List<Zone> zones, List<Releve> releves, List<Alerte> alertes,
            List<RapportProduction> rapports) {
        this.zones = zones;
        this.releves = releves;
        this.alertes = alertes;
        this.rapports = rapports;
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
