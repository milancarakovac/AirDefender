package data.aircraft.plane;

import data.person.Person;
import data.rocket.Rocket;

import java.util.HashMap;

public class Hunter extends MilitaryPlane {
    private boolean groundTarget;
    private boolean airTarget;
    public Hunter(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons, Rocket equipment, boolean groundTarget, boolean airTarget) {
        super(model, id, height, flightSpeed, caracteristics, persons, equipment);
        this.groundTarget = groundTarget;
        this.airTarget = airTarget;
    }

    public boolean isGroundTarget() {
        return groundTarget;
    }

    public void setGroundTarget(boolean groundTarget) {
        this.groundTarget = groundTarget;
    }

    public boolean isAirTarget() {
        return airTarget;
    }

    public void setAirTarget(boolean airTarget) {
        this.airTarget = airTarget;
    }

    @Override
    public String toString() {
        return "Ovo je lovac. \n" + super.toString();
    }
}
