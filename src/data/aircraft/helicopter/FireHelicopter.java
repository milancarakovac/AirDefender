package data.aircraft.helicopter;

import data.aircraft.FireAircraft;
import data.person.Person;

import java.util.HashMap;

public class FireHelicopter extends Helicopter implements FireAircraft {
    private double quantityOfWater;

    public FireHelicopter(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, double quantityOfWater) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.quantityOfWater = quantityOfWater;
    }

    public double getQuantityOfWater() {
        return quantityOfWater;
    }

    public void setQuantityOfWater(double quantityOfWater) {
        this.quantityOfWater = quantityOfWater;
    }

    @Override
    public String toString() {
        return puttingOutAFire() + super.toString() + "\nMaksimalna kolicina vode: " + getQuantityOfWater();
    }

    @Override
    public String puttingOutAFire() {
        return "Ovo je protivpozarni helikopter.\n";
    }
}
