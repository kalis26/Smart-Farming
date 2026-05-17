package smartfarming.entites;

import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.TypeElevage;

public class Ruminant extends Animal {
    public Ruminant(int id, EspeceAnimale espece, int age, double poids) {
        super(id, espece, age, poids);
        if (!estEspeceRuminant(espece)) {
            throw new IllegalArgumentException("Espece non compatible avec un ruminant: " + espece);
        }
    }

    @Override
    public TypeElevage getTypeElevage() {
        return TypeElevage.RUMINANTS;
    }

    private static boolean estEspeceRuminant(EspeceAnimale espece) {
        return espece == EspeceAnimale.VACHE
                || espece == EspeceAnimale.MOUTON
                || espece == EspeceAnimale.CHEVRE;
    }
}
