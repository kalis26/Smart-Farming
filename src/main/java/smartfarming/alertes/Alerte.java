package smartfarming.alertes;

import java.time.LocalDateTime;

import smartfarming.enums.NiveauGravite;
import smartfarming.enums.StatutAlerte;
import smartfarming.mesures.Releve;
import smartfarming.zones.Zone;

public class Alerte {
    private String id;
    private LocalDateTime dateHeure;
    private NiveauGravite gravite;
    private String message;
    private StatutAlerte statut;
    private Zone zone;
    private Releve releveDeclencheur;
}
