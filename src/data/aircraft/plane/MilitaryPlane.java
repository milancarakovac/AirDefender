package data.aircraft.plane;

import data.person.Person;
import data.rocket.Rocket;

import java.util.HashMap;

public class MilitaryPlane extends Plane {
    private Rocket rocket;

    public MilitaryPlane(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, Rocket rocket) {
        super(model, id, height, flightSpeed, caracteristics, persons);
        this.rocket = rocket;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
    }

    @Override
    public String toString() {
        return "Ovo je vojni avion. \n" + super.toString();
    }
}
