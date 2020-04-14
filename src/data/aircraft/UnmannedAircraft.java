package data.aircraft;

import java.util.HashMap;

public class UnmannedAircraft extends Aircraft {
    private boolean shootTheTerrain;
    public UnmannedAircraft(String model, String id, int height, int flightSpeed, HashMap<String, String> caracteristics, boolean shootTheTerrain) {
        super(model, id, height, flightSpeed, caracteristics, null);
        this.shootTheTerrain = shootTheTerrain;
    }

    public boolean isShootTheTerrain() {
        return shootTheTerrain;
    }

    public void setShootTheTerrain(boolean shootTheTerrain) {
        this.shootTheTerrain = shootTheTerrain;
    }

    @Override
    public String toString() {
        return "Ovo je bespilotna letjelica.\n" + super.toString();
    }
}
