package data;

import data.aircraft.Aircraft;
import data.rocket.Rocket;

public class AirSpace {
    public MatrixField[][] airSpace;
    private int width;
    private int height;
    private boolean isBanned;
    private boolean isThereForeignAircraft;

    public AirSpace(int width, int height){
        this.height = height;
        this.width = width;
        isBanned = false;
        isThereForeignAircraft = false;
        airSpace = new MatrixField[height][width];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++){
                airSpace[i][j] = new MatrixField(i,j);
            }
    }

    public void moveRocket(Rocket rocket, int oldLat, int oldLong, int newLat, int newLong){
        synchronized (airSpace[oldLat][oldLong]){
            synchronized (airSpace[newLat][newLong]){
                airSpace[oldLat][oldLong].getRockets().remove(rocket);
                addRocket(rocket, newLat, newLong);
            }
        }
    }

    public void addRocket(Rocket rocket, int lat, int lon){
        airSpace[lat][lon].setLatitude(lat);
        airSpace[lat][lon].setLongitude(lon);
        if(airSpace[lat][lon].getAircrafts().isEmpty() && airSpace[lat][lon].getRockets().isEmpty()) {
            airSpace[lat][lon] = new MatrixField(null);
            airSpace[lat][lon].getRockets().add(rocket);
        }
        else {
            airSpace[lat][lon].getRockets().add(rocket);
        }
    }

    public void move(Aircraft aircraft, int oldLat, int oldLong, int newLat, int newLong){
        synchronized (airSpace[oldLat][oldLong]) {
            synchronized (airSpace[newLat][newLong]) {
                airSpace[oldLat][oldLong].removeAircraft(aircraft);
                addNewAircraft(aircraft, newLat, newLong);
            }
        }
    }

    public void setInAirspace(Aircraft aircraft, int lat, int lon){
        synchronized (airSpace[lat][lon]) {
            addNewAircraft(aircraft, lat, lon);
            if (aircraft.isInCrash()) {
                airSpace[lat][lon].removeAircraft(aircraft);
            }
        }
    }

    public void addNewAircraft(Aircraft aircraft, int lat, int lon){
        airSpace[lat][lon].setLatitude(lat);
        airSpace[lat][lon].setLongitude(lon);
        if(airSpace[lat][lon].getAircrafts().isEmpty()) {
            airSpace[lat][lon] = new MatrixField(aircraft);
        }
        else {
            aircraft.setInCrash(airSpace[lat][lon].addAircraft(aircraft));
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isThereForeignAircraft() {
        return isThereForeignAircraft;
    }

    public void setThereForeignAircraft(boolean thereForeignAircraft) {
        isThereForeignAircraft = thereForeignAircraft;
    }
}
