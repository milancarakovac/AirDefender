package data.aircraft.plane;

import data.aircraft.TransportAircraft;
import data.person.Person;

import java.util.HashMap;

public class TransportPlane extends Plane implements TransportAircraft {
    private String cargo;
    private double maxWeight;

    public TransportPlane(String cargo, double maxWeight, String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.cargo = cargo;
        this.maxWeight = maxWeight;
    }

    public String getCargo() {
        return cargo;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString() {
        return transport() + super.toString() + "\nMaksimalna kolicina tereta: " + getMaxWeight();
    }

    @Override
    public String transport() { return "Ovo je transportni avion.\n"; }
}