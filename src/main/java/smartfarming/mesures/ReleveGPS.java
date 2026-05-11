package smartfarming.mesures;

import java.time.LocalDateTime;

import smartfarming.capteurs.Capteur;

public class ReleveGPS extends Releve {

    private PositionGPS position;

    public ReleveGPS() {
    }

    public ReleveGPS(String id, LocalDateTime dateHeure, Capteur capteur, PositionGPS position) {
        super(id, dateHeure, capteur);
        this.position = position;
    }

    public PositionGPS getPosition() {
        return position;
    }

    public void setPosition(PositionGPS position) {
        this.position = position;
    }
}
