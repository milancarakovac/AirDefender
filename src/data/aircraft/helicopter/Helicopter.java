package data.aircraft.helicopter;

import data.aircraft.Aircraft;
import data.person.Person;

import java.util.HashMap;

public class Helicopter extends Aircraft {
    public Helicopter(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons) {
        super(model, id, height, flightSpeed, caracteristics, persons);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
