package smartfarming.mesures;

import java.time.LocalDateTime;

import smartfarming.capteurs.Capteur;
import smartfarming.enums.UniteMesure;

public class ReleveNumerique extends Releve {

    private Double valeur;
    private UniteMesure unite;

    public ReleveNumerique() {
    }

    public ReleveNumerique(String id, LocalDateTime dateHeure, Capteur capteur, Double valeur,
            UniteMesure unite) {
        super(id, dateHeure, capteur);
        this.valeur = valeur;
        this.unite = unite;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }
}
