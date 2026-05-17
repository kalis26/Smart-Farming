package smartfarming.entites;

import smartfarming.enums.EspeceAnimale;
import smartfarming.enums.TypeElevage;

public class Volaille extends Animal {
    public Volaille(int id, EspeceAnimale espece, int age, double poids) {
        super(id, espece, age, poids);
        if (!estEspeceVolaille(espece)) {
            throw new IllegalArgumentException("Espece non compatible avec une volaille: " + espece);
        }
    }

    @Override
    public TypeElevage getTypeElevage() {
        return TypeElevage.VOLAILLE;
    }

    private static boolean estEspeceVolaille(EspeceAnimale espece) {
        return espece == EspeceAnimale.POULET
                || espece == EspeceAnimale.DINDE;
    }
}
