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

    public RapportProduction() {
    }

    public RapportProduction(String id, LocalDateTime dateGeneration, LocalDate debutPeriode,
            LocalDate finPeriode, Zone zone, String contenu) {
        this.id = id;
        this.dateGeneration = dateGeneration;
        this.debutPeriode = debutPeriode;
        this.finPeriode = finPeriode;
        this.zone = zone;
        this.contenu = contenu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public LocalDate getDebutPeriode() {
        return debutPeriode;
    }

    public void setDebutPeriode(LocalDate debutPeriode) {
        this.debutPeriode = debutPeriode;
    }

    public LocalDate getFinPeriode() {
        return finPeriode;
    }

    public void setFinPeriode(LocalDate finPeriode) {
        this.finPeriode = finPeriode;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
