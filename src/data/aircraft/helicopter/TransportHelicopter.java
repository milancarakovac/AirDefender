package data.aircraft.helicopter;

import data.aircraft.TransportAircraft;
import data.person.Person;

import java.util.HashMap;

public class TransportHelicopter extends Helicopter implements TransportAircraft {
    private String cargo;
    private double maxWeight;

    public TransportHelicopter(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, String cargo, double maxWeight) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.cargo = cargo;
        this.maxWeight = maxWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return transport() + super.toString() + "\nMaksimalna tezina: " + getMaxWeight();
    }

    @Override
    public String transport() {
        return "Ovo je transportni helikopter.\n";
    }
}
