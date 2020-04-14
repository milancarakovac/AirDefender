package data.aircraft.plane;

import data.person.Person;
import data.rocket.Rocket;

import java.util.HashMap;

public class Bombarder extends MilitaryPlane {
    private boolean groundTarget;
    public Bombarder(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, Rocket equipment, boolean groundTarget) {
        super(model, id, height, flightSpeed, caracteristics, persons, equipment);
        this.groundTarget = groundTarget;
    }

    public boolean isGroundTarget() {
        return groundTarget;
    }

    public void setGroundTarget(boolean groundTarget) {
        this.groundTarget = groundTarget;
    }

    @Override
    public String toString() {
        return "Ovo je bombarder.\n" + super.toString();
    }
}
