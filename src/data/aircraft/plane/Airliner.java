package data.aircraft.plane;

import data.aircraft.PassengerAircraft;
import data.person.Person;

import java.util.HashMap;

public class Airliner extends Plane implements PassengerAircraft {
    private int numberOfSeats;
    private double maxLuggageWeight;

    public Airliner(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, int numberOfSeats, double maxLuggageWeight) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.maxLuggageWeight = maxLuggageWeight;
        this.numberOfSeats = numberOfSeats;
    }

    public double getMaxLuggageWeight() {
        return maxLuggageWeight;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setMaxLuggageWeight(double maxLuggageWeight) {
        this.maxLuggageWeight = maxLuggageWeight;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return passengerAircraft() + super.toString() + "\nBroj sjedista: " + getNumberOfSeats() + "\nMaksimalna kolicina prtljaga: " + getMaxLuggageWeight();
    }

    @Override
    public String passengerAircraft() {
        return "Ovo je putnicki avion.\n";
    }
}
