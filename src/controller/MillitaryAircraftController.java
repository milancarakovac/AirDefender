package controller;

import data.AirSpace;
import data.FlyingCourse;
import data.aircraft.Aircraft;
import data.aircraft.plane.MilitaryPlane;
import main.Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MillitaryAircraftController extends Thread {
    private Aircraft aircraft;
    private MillitaryAircraftController target;
    private int lattitude;
    private int longitude;
    private FlyingCourse course;
    private boolean isInCrash;
    private boolean isOnExit;
    private boolean isInPursuit;
    private boolean isTracked;
    private AirSpace airSpace;

    public MillitaryAircraftController(Aircraft aircraft, AirSpace airSpace, FlyingCourse course, int lattitude, int longitude) {
        this.airSpace = airSpace;
        this.aircraft = aircraft;
        this.target = null;
        this.course = course;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public MillitaryAircraftController(Aircraft aircraft, AirSpace airSpace, MillitaryAircraftController target, boolean isLeft) {
        course = target.course.getReverse();
        this.target = target;
        this.airSpace = airSpace;
        this.aircraft = aircraft;
        this.isInPursuit = true;
        if (course == FlyingCourse.UP) {
            this.longitude = airSpace.getHeight() - 1;
            if (isLeft) {
                if (target.getLattitude() > 0)
                    this.lattitude = target.getLattitude() - 1;
                else this.lattitude = target.getLattitude();
            } else {
                if (target.getLattitude() < airSpace.getWidth() - 1)
                    this.lattitude = target.getLattitude() + 1;
                else this.lattitude = target.getLattitude();
            }
        } else if (course == FlyingCourse.DOWN) {
            this.longitude = 0;
            if (isLeft) {
                if (target.getLattitude() < airSpace.getWidth() - 1)
                    this.lattitude = target.getLattitude() + 1;
                else this.lattitude = target.getLattitude();
            } else {
                if (target.getLattitude() > 0)
                    this.lattitude = target.getLattitude() - 1;
                else this.lattitude = target.getLattitude();
            }
        } else if (course == FlyingCourse.LEFT) {
            this.lattitude = airSpace.getWidth() - 1;
            if (isLeft) {
                if (target.getLongitude() < airSpace.getHeight() - 1)
                    this.longitude = target.getLongitude() + 1;
                else this.longitude = target.getLongitude();
            } else {
                if (target.getLongitude() > 0)
                    this.longitude = target.getLongitude() - 1;
                else this.longitude = target.getLongitude();
            }
        } else {
            this.lattitude = 0;
            if (isLeft) {
                if (target.getLongitude() > 0)
                    this.longitude = target.getLongitude() - 1;
                else this.longitude = target.getLongitude();
            } else {
                if (target.getLongitude() < airSpace.getHeight() - 1)
                    this.longitude = target.getLongitude() + 1;
                else this.longitude = target.getLongitude();
            }
        }
        //fireRocket();
    }

    private void fireRocket() {
        int lat= lattitude;
        int lon = longitude;
        if(course == FlyingCourse.RIGHT || course == FlyingCourse.LEFT) lon = target.getLongitude();
        else lat = target.getLattitude();
        (new RocketController(this, ((MilitaryPlane)aircraft).getRocket(),lon, longitude, course)).start();
    }

    @Override
    public void run() {
        if (isInPursuit) {
            while (!isInCrash && !isOnExit) {
                try {
                    sleep(aircraft.getFlightSpeed() * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Can't sleep", ex);
                }
                if (course == FlyingCourse.RIGHT) {
                    if(target.getLattitude() == lattitude || target.getLattitude() == (lattitude + 1))
                        target.setOnExit(true);
                    if (lattitude < airSpace.getWidth() - 1)
                        airSpace.move(aircraft, lattitude, longitude, ++lattitude, longitude);
                    else isOnExit = true;
                } else if (course == FlyingCourse.LEFT) {
                    if(target.getLattitude() == lattitude || target.getLattitude() == (lattitude - 1))
                        target.setOnExit(true);
                    if (lattitude > 0)
                        airSpace.move(aircraft, lattitude, longitude, --lattitude, longitude);
                    else isOnExit = true;
                } else if (course == FlyingCourse.UP) {
                    if(target.getLongitude() == longitude || target.getLongitude() == (longitude - 1))
                        target.setOnExit(true);
                    if(longitude > 0)
                        airSpace.move(aircraft, lattitude, longitude, lattitude, --longitude);
                    else isOnExit = true;
                } else {
                    if(target.getLongitude() == longitude || target.getLongitude() == (longitude + 1))
                        target.setOnExit(true);
                    if(longitude < airSpace.getHeight() - 1)
                        airSpace.move(aircraft, lattitude, longitude, lattitude, ++longitude);
                    else isOnExit = true;
                }
                isInCrash = aircraft.isInCrash();
                synchronized (airSpace.airSpace[lattitude][longitude]) {
                    if (isInCrash) {
                        airSpace.airSpace[lattitude][longitude].removeAircraft(aircraft);
                    } else if (isOnExit) {
                        airSpace.airSpace[lattitude][longitude].removeAircraft(aircraft);
                    }
                }
            }
        } else {
            while (!isInCrash && !isOnExit) {
                try {
                    sleep(aircraft.getFlightSpeed() * 1000);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Can't sleep", ex);
                }
                if (course == FlyingCourse.RIGHT) {
                    if (lattitude < airSpace.getWidth() - 1)
                        airSpace.move(aircraft, lattitude, longitude, ++lattitude, longitude);
                    else isOnExit = true;
                } else if (course == FlyingCourse.LEFT) {
                    if (lattitude > 0)
                        airSpace.move(aircraft, lattitude, longitude, --lattitude, longitude);
                    else isOnExit = true;
                } else if (course == FlyingCourse.UP) {
                    if (longitude > 0)
                        airSpace.move(aircraft, lattitude, longitude, lattitude, --longitude);
                    else isOnExit = true;
                } else {
                    if (longitude < airSpace.getHeight() - 1)
                        airSpace.move(aircraft, lattitude, longitude, lattitude, ++longitude);
                    else isOnExit = true;
                }
                isInCrash = aircraft.isInCrash();
                if (isInCrash) airSpace.airSpace[lattitude][longitude].removeAircraft(aircraft);
                else if (isOnExit) airSpace.airSpace[lattitude][longitude].removeAircraft(aircraft);
            }
        }
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public MillitaryAircraftController getTarget() {
        return target;
    }

    public void setTarget(MillitaryAircraftController target) {
        this.target = target;
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

    public FlyingCourse getCourse() {
        return course;
    }

    public void setCourse(FlyingCourse course) {
        this.course = course;
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

    public boolean isInPursuit() {
        return isInPursuit;
    }

    public void setInPursuit(boolean inPursuit) {
        isInPursuit = inPursuit;
    }

    public AirSpace getAirSpace() {
        return airSpace;
    }

    public void setAirSpace(AirSpace airSpace) {
        this.airSpace = airSpace;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public void setTracked(boolean tracked) {
        isTracked = tracked;
    }
}
