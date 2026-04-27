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
}
