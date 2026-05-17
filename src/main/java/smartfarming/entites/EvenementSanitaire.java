package smartfarming.entites;

import java.time.LocalDateTime;
import smartfarming.enums.EtatSante;

public class EvenementSanitaire {
    private String id;
    private LocalDateTime dateHeure;
    private Animal animal;
    private String description;
    private EtatSante etatSante;
    private Double ancienPoids;
    private Double nouveauPoids;

    public EvenementSanitaire(String id, LocalDateTime dateHeure, Animal animal, String description,
            EtatSante etatSante, Double ancienPoids, Double nouveauPoids) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.animal = animal;
        this.description = description;
        this.etatSante = etatSante;
        this.ancienPoids = ancienPoids;
        this.nouveauPoids = nouveauPoids;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public Animal getAnimal() {
        return animal;
    }

    public String getDescription() {
        return description;
    }

    public EtatSante getEtatSante() {
        return etatSante;
    }

    public Double getAncienPoids() {
        return ancienPoids;
    }

    public Double getNouveauPoids() {
        return nouveauPoids;
    }

    @Override
    public String toString() {
        return dateHeure + " | " + description;
    }
}
