package smartfarming.capteurs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import smartfarming.alertes.Alerte;
import smartfarming.enums.NiveauGravite;
import smartfarming.enums.StatutCapteur;
import smartfarming.enums.UniteMesure;
import smartfarming.mesures.ReleveNumerique;
import smartfarming.zones.Zone;

public abstract class CapteurNumerique extends Capteur {
    private UniteMesure unite;
    private double seuilMin;
    private double seuilMax;
    private List<ReleveNumerique> historique;

    protected CapteurNumerique() {
        this.historique = new ArrayList<>();
    }

    protected CapteurNumerique(String id, String nom, StatutCapteur statut, Zone zone,
            UniteMesure unite, double seuilMin, double seuilMax) {
        super(id, nom, statut, zone);
        this.unite = unite;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.historique = new ArrayList<>();
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

    public ReleveNumerique envoyerReleve(Random random) {
        ReleveNumerique releve = creerReleve(genererValeur(random));
        ajouterReleve(releve);
        return releve;
    }

    public void ajouterReleve(ReleveNumerique releve) {
        if (!historique.contains(releve)) {
            historique.add(releve);
        }
    }

    public NiveauGravite evaluerNiveau(ReleveNumerique releve) {
        double valeur = releve.getValeur();
        if (!estHorsSeuil(valeur)) {
            return NiveauGravite.Normal;
        }
        double amplitude = Math.max(seuilMax - seuilMin, 0.0001);
        double depassement = valeur < seuilMin ? seuilMin - valeur : valeur - seuilMax;
        return depassement <= amplitude * 0.10 ? NiveauGravite.Avertissement : NiveauGravite.Critique;
    }

    public Optional<Alerte> evaluerAlerte(ReleveNumerique releve) {
        NiveauGravite niveau = evaluerNiveau(releve);
        releve.setNiveauGravite(niveau);
        if (niveau != NiveauGravite.Normal) {
            String message = "Valeur hors seuil : " + releve.getValeur() + " " + getUnite();
            return Optional.of(Alerte.creerAlerte(this.getZone(), releve, message, niveau));
        }
        return Optional.empty();
    }

    private double genererValeur(Random random) {
        double amplitude = seuilMax - seuilMin;
        double choix = random.nextDouble();
        if (choix < 0.70) {
            return seuilMin + amplitude * (0.20 + random.nextDouble() * 0.60);
        }
        if (choix < 0.85) {
            double marge = amplitude * (0.02 + random.nextDouble() * 0.08);
            return random.nextBoolean() ? seuilMax + marge : seuilMin - marge;
        }
        double marge = amplitude * (0.12 + random.nextDouble() * 0.20);
        return random.nextBoolean() ? seuilMax + marge : seuilMin - marge;
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

    public List<ReleveNumerique> getHistorique() {
        return historique;
    }
}
