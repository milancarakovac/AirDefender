package simulation;

import controller.AircraftController;
import data.FlyingCourse;
import controller.MillitaryAircraftController;
import data.AirSpace;
import data.aircraft.Aircraft;
import util.AircraftCreator;
import util.FileWatcher;
import util.Zip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Simulator extends Thread {
    private static final String CONFIG_PROPERTIES = "src/resources/config.properties";
    private Timer timer;
    private AirSpace airSpace;
    private int creationInterval;
    private int heightRange;
    private Random rand;
    private Radar radar;
    private TimerTask creationTask;
    private TimerTask zippingTask;
    private int numberOfForeign;
    private int numberOfDomestic;
    private ArrayList<MillitaryAircraftController> foreignAircrafts;
    private Zip zipper;
    private FileWatcher watcher;

    public Simulator(int width, int height, int creationInterval, int heightRange) {
        this.heightRange = heightRange;
        foreignAircrafts = new ArrayList<>();
        numberOfForeign = 0;
        numberOfDomestic = 0;
        this.creationInterval = creationInterval;
        airSpace = new AirSpace(width, height);
        timer = new Timer("Timer");
        rand = new Random();
        radar = new Radar(width, height, airSpace);
        radar.setDaemon(true);
        radar.start();
        zipper = new Zip();
        creationTask = getCreationTask();
        zippingTask = getZippingTask();
        timer.schedule( creationTask, 0, 1000*this.creationInterval);
        timer.schedule( zippingTask, 0, 60000);
        try{
            Path path = Paths.get("src/resources/");
            watcher = new FileWatcher(path, "config.properties", StandardWatchEventKinds.ENTRY_MODIFY);
            watcher.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void run(){
        while(true){

            if(watcher.isThereChange()){
                try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
                    Properties properties = new Properties();
                    properties.load(input);
                    numberOfForeign = Integer.parseInt(properties.getProperty("numberOfForeignAircrafts"));
                    numberOfDomestic = Integer.parseInt(properties.getProperty("numberOfDomesticAircrafts"));
                    airSpace.setBanned(Boolean.parseBoolean(properties.getProperty("isBanned")));
                }catch (Exception ex){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't load and read config.properties file", ex);
                }
            }

            if(numberOfDomestic > 0){
                while(numberOfDomestic > 0){
                    createMillitaryAircraft(false);
                    numberOfDomestic--;
                }
                Properties properties = new Properties();
                try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
                    properties.load(input);
                }catch (Exception ex){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't load config.properties file", ex);
                }
                try(OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)){
                    properties.setProperty("numberOfDomesticAircrafts","-1");
                    properties.store(output,null);
                }catch (Exception ex){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't write to config.properties file", ex);
                }
            }
            if(numberOfForeign > 0){
                airSpace.setThereForeignAircraft(true);
                while(numberOfForeign > 0){
                    createMillitaryAircraft(true);
                    numberOfForeign--;
                    try{
                        sleep(100);
                    }
                    catch (Exception ex){
                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't sleep", ex);
                    }
                }
                Properties properties = new Properties();
                try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
                    properties.load(input);
                }catch (Exception ex){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't load config.properties file", ex);
                }
                try(OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)){
                    properties.setProperty("numberOfForeignAircrafts","-1");
                    properties.store(output,null);
                }catch (Exception ex){
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't write to config.properties file", ex);
                }
            }
            if(!foreignAircrafts.isEmpty()) {
                for(int i = 0; i < foreignAircrafts.size();i++)
                    if(foreignAircrafts.get(0).getState() == State.TERMINATED) foreignAircrafts.remove(foreignAircrafts.get(0));
            }
            else airSpace.setThereForeignAircraft(false);

            if(radar.isAttackSignal() && !foreignAircrafts.isEmpty()){
                for(int i = 0; i < foreignAircrafts.size(); i++) {
                    if(!foreignAircrafts.get(i).isTracked() && radar.getForeignAircrafts().contains(foreignAircrafts.get(i).getAircraft().getId())) {
                        createDomesticAircraftsForPursuit(foreignAircrafts.get(i));
                        foreignAircrafts.get(i).setTracked(true);
                    }
                }
            }
            try{
                sleep(200);
            }
            catch (Exception ex){
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't sleep", ex);
            }
        }
    }

    private void createDomesticAircraftsForPursuit(MillitaryAircraftController target) {
        Aircraft leftAircraft = (new AircraftCreator(heightRange)).createMillitaryAircraft(true, target.getAircraft().getHeight());
        MillitaryAircraftController leftController = new MillitaryAircraftController(leftAircraft, airSpace, target, true);
        Aircraft rightAircraft = (new AircraftCreator(heightRange)).createMillitaryAircraft(true, target.getAircraft().getHeight());
        MillitaryAircraftController rightController = new MillitaryAircraftController(rightAircraft, airSpace, target, false);
        airSpace.setInAirspace(leftAircraft, leftController.getLattitude(), leftController.getLongitude());
        airSpace.setInAirspace(rightAircraft, rightController.getLattitude(), rightController.getLongitude());
        leftController.setDaemon(true);
        leftController.start();
        rightController.setDaemon(true);
        rightController.start();
    }

    private TimerTask getCreationTask(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!airSpace.isThereForeignAircraft() && !airSpace.isBanned()) {
                    int side = rand.nextInt(4);
                    AircraftController aircraft;
                    switch (side) {
                        case 0: {
                            aircraft = new AircraftController(0, rand.nextInt(airSpace.getHeight()), FlyingCourse.RIGHT, airSpace, heightRange);
                            airSpace.setInAirspace(aircraft.getAircraft(), aircraft.getLattitude(), aircraft.getLongitude());
                            break;
                        }
                        case 1: {
                            aircraft = new AircraftController(airSpace.getWidth() - 1, rand.nextInt(airSpace.getHeight()), FlyingCourse.LEFT, airSpace, heightRange);
                            airSpace.setInAirspace(aircraft.getAircraft(), aircraft.getLattitude(), aircraft.getLongitude());
                            break;
                        }
                        case 2: {
                            aircraft = new AircraftController(rand.nextInt(airSpace.getWidth()), 0, FlyingCourse.DOWN, airSpace, heightRange);
                            airSpace.setInAirspace(aircraft.getAircraft(), aircraft.getLattitude(), aircraft.getLongitude());
                            break;
                        }
                        case 3: {
                            aircraft = new AircraftController(rand.nextInt(airSpace.getWidth()), airSpace.getHeight() - 1, FlyingCourse.UP, airSpace, heightRange);
                            airSpace.setInAirspace(aircraft.getAircraft(), aircraft.getLattitude(), aircraft.getLongitude());
                            break;
                        }
                        default: {
                            aircraft = null;
                        }
                    }
                    if (aircraft != null) {
                        aircraft.setDaemon(true);
                        aircraft.start();
                    }
                }
            }
        };
        return task;
    }

    private TimerTask getZippingTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                zipper.compress("");
            }
        };
        return task;
    }

    private void createMillitaryAircraft(boolean isForeign) {
        int courseChoice = rand.nextInt(4);
        Aircraft aircraft = null;
        if (isForeign)
            aircraft = (new AircraftCreator(heightRange)).createForeignAircraft();
        else
            aircraft = (new AircraftCreator(heightRange)).createMillitaryAircraft(false,0);
        MillitaryAircraftController controller = null;
        switch (courseChoice) {
            case 0: {
                controller = new MillitaryAircraftController(aircraft, airSpace, FlyingCourse.RIGHT, 0, rand.nextInt(airSpace.getHeight()));
                airSpace.setInAirspace(aircraft, controller.getLattitude(), controller.getLongitude());
                break;
            }
            case 1: {
                controller = new MillitaryAircraftController(aircraft, airSpace, FlyingCourse.LEFT, airSpace.getWidth() - 1 , rand.nextInt(airSpace.getHeight()));
                airSpace.setInAirspace(aircraft, controller.getLattitude(), controller.getLongitude());
                break;
            }
            case 2: {
                controller = new MillitaryAircraftController(aircraft, airSpace, FlyingCourse.UP, rand.nextInt(airSpace.getWidth()), airSpace.getHeight() - 1);
                airSpace.setInAirspace(aircraft, controller.getLattitude(), controller.getLongitude());
                break;
            }
            case 3: {
                controller = new MillitaryAircraftController(aircraft, airSpace, FlyingCourse.DOWN, rand.nextInt(airSpace.getWidth()), 0);
                airSpace.setInAirspace(aircraft, controller.getLattitude(), controller.getLongitude());
                break;
            }

        }
        if (controller != null) {
            if (isForeign) foreignAircrafts.add(controller);
            controller.setDaemon(true);
            controller.start();
        }
    }
}
