package smartfarming.capteurs;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import smartfarming.alertes.Alerte;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.mesures.ReleveNumerique;
import smartfarming.zones.Zone;

public abstract class CapteurNumerique extends Capteur {
    private UniteMesure unite;
    private double seuilMin;
    private double seuilMax;

    protected CapteurNumerique() {
    }

    protected CapteurNumerique(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax) {
        super(id, nom, statut, zone);
        this.unite = unite;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
    }

    public boolean estHorsSeuil(double valeur) {
        return valeur < seuilMin || valeur > seuilMax;
    }

    public ReleveNumerique creerReleve(double valeur) {
        if (!estActif()) {
            throw new IllegalStateException("Capteur numerique inactif");
        }
        return new ReleveNumerique(UUID.randomUUID().toString(), LocalDateTime.now(), this, valeur, this.getUnite());
    }

    public Optional<Alerte> evaluerAlerte(ReleveNumerique releve) {
        if (estHorsSeuil(releve.getValeur())) {
            String message = "Valeur hors seuil : " + releve.getValeur() + " " + getUnite();
            return Optional.of(Alerte.creerAlerte(this.getZone(), releve, message));
        }
        return Optional.empty();
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public double getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(double seuilMin) {
        this.seuilMin = seuilMin;
    }

    public double getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(double seuilMax) {
        this.seuilMax = seuilMax;
    }
}
