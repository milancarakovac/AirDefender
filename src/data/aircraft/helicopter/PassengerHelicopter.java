package data.aircraft.helicopter;

import data.aircraft.PassengerAircraft;
import data.person.Person;

import java.util.HashMap;

public class PassengerHelicopter extends Helicopter implements PassengerAircraft {
    private int numberOfSeats;

    public PassengerHelicopter(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, int numberOfSeats) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return passengerAircraft() + super.toString() + "\nBroj sjedista je: " + getNumberOfSeats();
    }

    @Override
    public String passengerAircraft() {
        return "Ovo je putnicki helikopter.\n";
    }
}
