package data.aircraft.plane;

import data.aircraft.FireAircraft;
import data.person.Person;

import java.util.HashMap;

public class FirePlane extends Plane implements FireAircraft {
    private double quantityOfWater;

    public FirePlane(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, boolean puttingOutAFire, double quantityOfWater) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.quantityOfWater = quantityOfWater;
    }

    public void setQuantityOfWater(double quantityOfWater) {
        this.quantityOfWater = quantityOfWater;
    }

    public double getQuantityOfWater() {
        return quantityOfWater;
    }

    @Override
    public String toString() {
        return puttingOutAFire() + super.toString() + "\nKolicina vode: " + getQuantityOfWater();
    }

    @Override
    public String puttingOutAFire() {
        return "Ovo je protivpozarni avion.\n";
    }
}
