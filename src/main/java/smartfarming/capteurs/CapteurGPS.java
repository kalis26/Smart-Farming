package smartfarming.capteurs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import smartfarming.enums.StatutCapteur;
import smartfarming.mesures.PositionGPS;
import smartfarming.mesures.ReleveGPS;
import smartfarming.zones.Zone;

public class CapteurGPS extends Capteur {
    private double latitude;
    private double longitude;
    private List<ReleveGPS> historiqueGPS;

    public CapteurGPS() {
        this.historiqueGPS = new ArrayList<>();
    }

    public CapteurGPS(String id, String nom, StatutCapteur statut, Zone zone, double latitude, double longitude) {
        super(id, nom, statut, zone);
        this.latitude = latitude;
        this.longitude = longitude;
        this.historiqueGPS = new ArrayList<>();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ReleveGPS envoyerReleve(Random random) {
        if (!estActif()) {
            throw new IllegalStateException("Capteur GPS inactif");
        }
        Zone zone = getZone();
        double nouvelleLatitude = latitude;
        double nouvelleLongitude = longitude;
        if (zone != null) {
            boolean horsZone = random.nextDouble() < 0.20;
            if (horsZone) {
                nouvelleLatitude = zone.getLatitudeMax() + 0.01;
                nouvelleLongitude = zone.getLongitudeMax() + 0.01;
            } else {
                nouvelleLatitude = zone.getLatitudeMin()
                        + random.nextDouble() * (zone.getLatitudeMax() - zone.getLatitudeMin());
                nouvelleLongitude = zone.getLongitudeMin()
                        + random.nextDouble() * (zone.getLongitudeMax() - zone.getLongitudeMin());
            }
        }
        this.latitude = nouvelleLatitude;
        this.longitude = nouvelleLongitude;
        ReleveGPS releve = new ReleveGPS(UUID.randomUUID().toString(), LocalDateTime.now(), this,
                new PositionGPS(nouvelleLatitude, nouvelleLongitude));
        ajouterReleve(releve);
        return releve;
    }

    public void ajouterReleve(ReleveGPS releve) {
        if (!historiqueGPS.contains(releve)) {
            historiqueGPS.add(releve);
        }
    }

    public List<ReleveGPS> getHistoriqueGPS() {
        return historiqueGPS;
    }
}
