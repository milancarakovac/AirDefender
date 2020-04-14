package data.rocket;

public class Rocket {
    private int range;
    private int height;
    private int speed;

    public Rocket(int range, int height, int speed) {
        this.range = range;
        this.height = height;
        this.speed = speed;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "Domet: " + getRange() + "\nBrzina: " + getSpeed();
    }
}