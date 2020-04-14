package data.aircraft;


import data.person.Person;

import java.util.HashMap;

public class Aircraft {
    private String model;
    private String id;
    private int height;
    private int flightSpeed;
    private HashMap<String,String> caracteristics;
    private Person[] persons;
    private boolean foreignAircraft;
    private boolean isInCrash;
    private boolean isDetected;

    public Aircraft(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, Person[] persons) {
        this.model = model;
        this.id = id;
        this.height = height;
        this.flightSpeed = flightSpeed;
        this.caracteristics = caracteristics;
        caracteristics.put("Speed", "" + flightSpeed);
        caracteristics.put("Height", "" + height);
        caracteristics.put("Model", model);
        caracteristics.put("ID", id);
        this.persons = persons;
    }

    public String getModel() {
        return model;
    }

    public String getId() {
        return id;
    }

    public int getFlightSpeed() {
        return flightSpeed;
    }

    public int getHeight() {
        return height;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setFlightSpeed(int flightSpeed) {
        this.flightSpeed = flightSpeed;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public boolean isForeignAircraft() {
        return foreignAircraft;
    }

    public void setForeignAircraft(boolean foreignAircraft) {
        this.foreignAircraft = foreignAircraft;
    }

    public boolean isInCrash() {
        return isInCrash;
    }

    public void setInCrash(boolean inCrash) {
        isInCrash = inCrash;
    }

    public boolean isDetected() {
        return isDetected;
    }

    public void setDetected(boolean detected) {
        isDetected = detected;
    }

    @Override
    public String toString() {
        if (persons != null){
            String passengers = "";
        for (int i = 0; i < persons.length; i++) {
            passengers += "" + (i + 1) + ". " + persons[i] + "\n";
        }
        return "ID letjelice: " + getId() + "\nModel: " + getModel() + "\nVisina: " + getHeight() + "\nBrzina letenja: " + getFlightSpeed()
                + "\nPodaci o putnicima: \n" + passengers;
    }else {
            return "ID letjelice: " + getId() + "\nModel: " + getModel() + "\nVisina: " + getHeight() + "\nBrzina letenja: " + getFlightSpeed();
        }
    }
}
