package smartfarming.mesures;

import java.time.LocalDateTime;

import smartfarming.capteurs.Capteur;
import smartfarming.enums.UniteMesure;

public class Releve {
    private String id;
    private LocalDateTime dateHeure;
    private Capteur capteur;
    private Double valeur;
    private UniteMesure unite;
    private PositionGPS position;
}
