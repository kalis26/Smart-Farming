package smartfarming.capteurs;

import smartfarming.enums.StatutCapteur;
import smartfarming.zones.Zone;

public class CapteurGPS extends Capteur {
    private double latitude;
    private double longitude;

    public CapteurGPS() {
    }

    public CapteurGPS(String id, String nom, StatutCapteur statut, Zone zone, double latitude, double longitude) {
        super(id, nom, statut, zone);
        this.latitude = latitude;
        this.longitude = longitude;
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
}
