package smartfarming.rapports;

import java.time.LocalDate;
import java.time.LocalDateTime;

import smartfarming.zones.Zone;

public class RapportProduction {
    private String id;
    private LocalDateTime dateGeneration;
    private LocalDate debutPeriode;
    private LocalDate finPeriode;
    private Zone zone;
    private String contenu;
}
