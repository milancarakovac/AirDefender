package controller;

import data.AirSpace;
import data.FlyingCourse;
import data.rocket.Rocket;
import main.Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RocketController extends Thread {
    private MillitaryAircraftController plane;
    private Rocket rocket;
    private int lattitude;
    private int longitude;
    private FlyingCourse course;
    private AirSpace airspace;
    private boolean isOnExit;
    private boolean hasExploded;

    public RocketController(MillitaryAircraftController plane, Rocket rocket, int lattitude, int longitude, FlyingCourse course){
        this.plane  = plane;
        this.rocket = rocket;
        this.course = course;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.airspace = plane.getAirSpace();
    }

    @Override
    public void run(){
        while(!isOnExit && !hasExploded) {
            try {
                sleep(rocket.getSpeed() * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Can't sleep", ex);
            }
            if (course == FlyingCourse.RIGHT)
                if (lattitude < airspace.getWidth() - 1)
                    airspace.moveRocket(rocket, lattitude, longitude, ++lattitude, longitude);
                else isOnExit = true;
            else if (course == FlyingCourse.LEFT)
                if (lattitude > 0) airspace.moveRocket(rocket, lattitude, longitude, --lattitude, longitude);
                else isOnExit = true;
            else if (course == FlyingCourse.DOWN)
                if (longitude < airspace.getHeight() - 1)
                    airspace.moveRocket(rocket, lattitude, longitude, lattitude, ++longitude);
                else isOnExit = true;
            else if (longitude > 0) airspace.moveRocket(rocket, lattitude, longitude, lattitude, --longitude);
            else isOnExit = true;
            synchronized (airspace.airSpace[lattitude][longitude].getAircrafts()) {
                for (var aircraft : airspace.airSpace[lattitude][longitude].getAircrafts()) {
                    if (aircraft != null && aircraft.getHeight() == rocket.getHeight() && aircraft.isForeignAircraft()) {
                        aircraft.setInCrash(true);
                        airspace.airSpace[lattitude][longitude].getAircrafts().remove(aircraft);
                        hasExploded = true;
                    }
                }
            }
        }
    }
}
