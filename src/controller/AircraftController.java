package controller;

import data.AirSpace;
import data.FlyingCourse;
import data.aircraft.Aircraft;
import main.Main;
import util.AircraftCreator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AircraftController extends Thread {
    private Aircraft aircraft;
    private int lattitude;
    private int longitude;
    private FlyingCourse course;
    private boolean isInCrash;
    private boolean isOnExit;
    private AirSpace airSpace;

    public AircraftController(int lattitude, int longitude, FlyingCourse course, AirSpace airSpace, int heightRange){
        super();
        this.airSpace = airSpace;
        this.longitude = longitude;
        this.lattitude = lattitude;
        this.course = course;
        aircraft = (new AircraftCreator(heightRange)).createAircraft();
        isInCrash = aircraft.isInCrash();
        isOnExit = false;
    }

    @Override
    public void run() {
        while (!isInCrash && !isOnExit) {
            try {
                sleep(aircraft.getFlightSpeed() * 1000);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Can't sleep", ex);
            }
            if (airSpace.isBanned() || airSpace.isThereForeignAircraft()) {
                if(getShortestPath() == course.getReverse()){
                    course = getShortestSidePath();
                }
                else course = getShortestPath();
            }
            if (course == FlyingCourse.UP) {
                if (longitude > 0) {
                    airSpace.move(aircraft, lattitude, longitude, lattitude, --longitude);
                } else {
                    isOnExit = true;
                }
            } else if (course == FlyingCourse.DOWN) {
                if (longitude < airSpace.getHeight() - 1) {
                    airSpace.move(aircraft, lattitude, longitude, lattitude, ++longitude);
                } else {
                    isOnExit = true;
                }
            } else if (course == FlyingCourse.LEFT) {
                if (lattitude > 0) {
                    airSpace.move(aircraft, lattitude, longitude, --lattitude, longitude);
                } else {
                    isOnExit = true;
                }
            } else {
                if (lattitude < airSpace.getWidth() - 1) {
                    airSpace.move(aircraft, lattitude, longitude, ++lattitude, longitude);
                } else {
                    isOnExit = true;
                }
            }
            isInCrash = aircraft.isInCrash();
            synchronized (airSpace.airSpace[lattitude][longitude]) {
                if (isInCrash) {
                    airSpace.airSpace[lattitude][longitude].getAircrafts().remove(aircraft);
                }
                else if(isOnExit) {
                    airSpace.airSpace[lattitude][longitude].getAircrafts().remove(aircraft);
                }
            }
        }
    }

    private FlyingCourse getShortestPath() {
        int toLeft = lattitude;
        int toRight = airSpace.getWidth() - lattitude - 1;
        int toUp = longitude;
        int toDown = airSpace.getHeight() - longitude - 1;
        if (toRight < toLeft) {
            if (toRight < toUp && toRight < toDown) return FlyingCourse.RIGHT;
            else if (toUp < toDown) return FlyingCourse.UP;
            else return FlyingCourse.DOWN;
        } else {
            if (toLeft < toUp && toLeft < toDown) return FlyingCourse.LEFT;
            else if (toUp < toDown) return FlyingCourse.UP;
            else return FlyingCourse.DOWN;
        }
    }

    private FlyingCourse getShortestSidePath(){
        int toLeft = lattitude;
        int toRight = airSpace.getWidth() - lattitude - 1;
        int toUp = longitude;
        int toDown = airSpace.getHeight() - longitude - 1;
        if(course == FlyingCourse.RIGHT || course == FlyingCourse.LEFT){
            if(toDown > toUp) return FlyingCourse.UP;
            else return FlyingCourse.DOWN;
        }else{
            if(toLeft > toRight) return FlyingCourse.RIGHT;
            else return FlyingCourse.LEFT;
        }
    }

    public int getLattitude() {
        return lattitude;
    }

    public void setLattitude(int lattitude) {
        this.lattitude = lattitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public boolean isInCrash() {
        return isInCrash;
    }

    public void setInCrash(boolean inCrash) {
        isInCrash = inCrash;
    }

    public boolean isOnExit() {
        return isOnExit;
    }

    public void setOnExit(boolean onExit) {
        isOnExit = onExit;
    }

    public FlyingCourse getCourse() {
        return course;
    }

    public void setCourse(FlyingCourse course) {
        this.course = course;
    }

    public AirSpace getAirSpace() {
        return airSpace;
    }

    public void setAirSpace(AirSpace airSpace) {
        this.airSpace = airSpace;
    }

}
