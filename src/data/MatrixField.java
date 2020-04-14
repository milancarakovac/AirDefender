package data;

import data.aircraft.Aircraft;
import data.rocket.Rocket;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatrixField {
    private ArrayList<Aircraft> aircrafts;
    private ArrayList<Rocket> rockets;//new
    private int latitude;
    private int longitude;

    public MatrixField(int latitude, int longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        aircrafts = new ArrayList<>();
        rockets = new ArrayList<>();//new
    }

    public MatrixField(Aircraft aircraft){
        aircrafts = new ArrayList<>();
        rockets = new ArrayList<>();
        addAircraft(aircraft);
    }

    public boolean addAircraft(Aircraft newAircraft) {
        for (var x : aircrafts) {
            if (newAircraft.getHeight() == x.getHeight()) {
                x.setInCrash(true);
                createCrashReport(newAircraft, x);
                return true;
            }
        }
        aircrafts.add(0, newAircraft);
        return false;
    }

    public void removeAircraft(Aircraft aircraft) {
            if (aircrafts.contains(aircraft)) {
                aircrafts.remove(aircraft);
            }
    }

    public void createCrashReport(Aircraft first, Aircraft second){
        Warning warning = new Warning("Sudar letjelica: " + first.getId() + " i " + second.getId(), new Date(), latitude, longitude);
        String path = "./alert/crash" + warning.getTime() + ".ser";
        try(var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)))){
            out.writeObject(warning);
            out.flush();
        }catch(IOException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't create crash.ser file", ex);
        }
    }

    public ArrayList<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(ArrayList<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Rocket> getRockets() {//new
        return rockets;//new
    }//new

    public void setRockets(ArrayList<Rocket> rockets) {//new
        this.rockets = rockets;//new
    }//new
}
