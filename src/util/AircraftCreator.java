package util;

import data.aircraft.Aircraft;
import data.aircraft.UnmannedAircraft;
import data.aircraft.helicopter.FireHelicopter;
import data.aircraft.helicopter.PassengerHelicopter;
import data.aircraft.helicopter.TransportHelicopter;
import data.aircraft.plane.*;
import data.person.Passenger;
import data.person.Person;
import data.person.Pilot;
import data.rocket.Missile;

import java.util.HashMap;
import java.util.Random;

public class AircraftCreator {
    private Random rand = new Random();
    private static int counter;
    private int bound;
    private Pilot pilot1 = new Pilot("Vojna akademija","Marko","Markovic");
    private Pilot pilot2 = new Pilot("Fakultet vazduhoplovstva","Ivan","Ivanovic");
    private Pilot pilot3 = new Pilot("Aerodrom Nikola Tesla","Jovan","Jovanovic");
    private Pilot pilot4 = new Pilot("Vojna akademija","Mirko","Mirkovic");
    private Passenger passenger1 = new Passenger(123,"Nemanja","Nemanjic");
    private Passenger passenger2 = new Passenger(854,"Milan","Milanovic");
    private Passenger passenger3 = new Passenger(965,"Zeljko","Zeljic");
    private Passenger passenger4 = new Passenger(147,"Miroslav","Mirkovic");

    public AircraftCreator(int heightRange){
        bound = heightRange;
    }

    public Aircraft createAircraft(){
        int aircraftType = rand.nextInt(7);
        switch (aircraftType){
            case 0: {
                return new TransportPlane("Teret" + (++counter), 10000, "TP" + counter,
                        "TP" + counter, rand.nextInt(bound), rand.nextInt(3) + 1, new HashMap<>(), new Person[]{pilot1});
            }
            case 1: {
                return new Airliner("AR" + (++counter), "PP" + counter, rand.nextInt(bound), rand.nextInt(3) + 1, new HashMap<>()
                        , new Person[]{pilot3, passenger2, passenger4}, 150, 2000);
            }
            case 2: {
                return new FirePlane("FP" + (++counter), "FP" + counter, rand.nextInt(bound), rand.nextInt(3) + 1
                        , new HashMap<>(), new Person[]{pilot4}, true, 10000);
            }
            case 3: {
                return new TransportHelicopter("TH" + (++counter), "TH" + counter, rand.nextInt(bound),
                        rand.nextInt(3) + 1, new HashMap<>(), new Person[]{pilot2, passenger2,passenger1}, null, 5000);
            }
            case 4: {
                return new PassengerHelicopter("PH" + (++counter), "PH" + counter, rand.nextInt(bound),
                        rand.nextInt(3) + 1, new HashMap<>(), new Person[]{pilot3, passenger3, passenger2}, 10);
            }
            case 5: {
                return new FireHelicopter("FH" + (++counter), "FH" + counter, rand.nextInt(bound), rand.nextInt(3) + 1,
                        new HashMap<>(), new Person[]{pilot3}, 7500);
            }
            case 6: {
                return new UnmannedAircraft("UMA" + (++counter), "UA" + counter, rand.nextInt(bound),
                        rand.nextInt(3) + 1, new HashMap<>(), true);
            }
            default:return new Airliner("AR" + (++counter), "PP" + counter, rand.nextInt(bound), rand.nextInt(3) + 1, new HashMap<>()
                    , new Person[]{pilot3, passenger2, passenger4}, 150, 2000);
        }
    }

    public Aircraft createForeignAircraft(){
        int height = rand.nextInt(bound);
        Pilot pilot = new Pilot("Nepoznato","Nepoznato","Nepoznato");
        Hunter foreign = new Hunter("H-UNKNOWN","EA" + (++counter), height,rand.nextInt(3) + 1,new HashMap<>(), new Person[]{pilot},
                new Missile(80, height,1), true,true);
        foreign.setForeignAircraft(true);
        return foreign;
    }

    public Aircraft createMillitaryAircraft(boolean isForPursuit, int targetHeight) {
        boolean aircraftType = rand.nextBoolean();
        int height = rand.nextInt(bound);
        Pilot pilot = new Pilot("Vojna akademija - major", "Mitar", "Mitrovic");
        if(isForPursuit){
            return new Hunter("MH" + (++counter), "DH" + counter, targetHeight, rand.nextInt(3) + 1,
                    new HashMap<>(), new Pilot[]{pilot}, new Missile(80, height, 1), true, true);
        }
        if(aircraftType) {
            return new Hunter("MH" + (++counter), "DH" + counter, height, rand.nextInt(3) + 1,
                    new HashMap<>(), new Pilot[]{pilot}, new Missile(80, height, 1), true, true);
        }else {
            return new Bombarder("MH" + (++counter), "DB" + counter, height, rand.nextInt(3) + 1,
                    new HashMap<>(), new Pilot[]{pilot}, new Missile(80, height, 1), true);
        }
    }
}