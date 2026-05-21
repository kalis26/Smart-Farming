package smartfarming.system;

import java.time.LocalDateTime;

public class ResultatSimulation {
    private LocalDateTime dateHeure;
    private int nombreRelevesGeneres;
    private int nombreAlertesGenerees;

    public ResultatSimulation(LocalDateTime dateHeure, int nombreRelevesGeneres, int nombreAlertesGenerees) {
        this.dateHeure = dateHeure;
        this.nombreRelevesGeneres = nombreRelevesGeneres;
        this.nombreAlertesGenerees = nombreAlertesGenerees;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public int getNombreRelevesGeneres() {
        return nombreRelevesGeneres;
    }

    public int getNombreAlertesGenerees() {
        return nombreAlertesGenerees;
    }
}
