package simulation;

import data.AirSpace;
import data.MatrixField;
import data.aircraft.Aircraft;
import main.Main;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Radar extends Thread {
    private static final String MAP_TXT = "src/resources/map.txt";
    private static final String RADAR_PROPERTIES = "src/resources/radar.properties";
    private Properties properties;
    private String [][]matrix;
    private int width;
    private int height;
    private AirSpace airSpace;
    private int updatingInterval;
    private Timer timer;
    private boolean attackSignal;
    private ArrayList<String> foreignAircrafts;
    private DateFormat format;

    public Radar(int width, int height, AirSpace airSpace){
        try(InputStream input = new FileInputStream(RADAR_PROPERTIES)){
            properties = new Properties();
            properties.load(input);
            updatingInterval = Integer.parseInt(properties.getProperty("updatingInterval"));
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Error at reading radar.properties ",ex);
        }
        this.height = height;
        this.width = width;
        this.airSpace = airSpace;
        matrix = new String[width][height];
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                printMatrixToFile();
            }
        },0,updatingInterval*1000);
        foreignAircrafts = new ArrayList<>();
        format = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy");
    }

    @Override
    public void run(){
        while(true){
            try{
                sleep(200);
            }catch (Exception ex){
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Error while sleeping in radar ",ex);
            }
            fillMatrix(airSpace.airSpace);
        }
    }

    public void printMatrixToFile() {
        try (var writer = new PrintWriter(new FileOutputStream(MAP_TXT))) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    writer.print(matrix[j][i]);
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't write to file map.txt", e);
        }
    }


    public void fillMatrix(MatrixField[][] realMatrix) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                synchronized (realMatrix[i][j]) {
                    if (!realMatrix[i][j].getAircrafts().isEmpty()) {
                        var max = realMatrix[i][j].getAircrafts().stream().max(Comparator.comparing(Aircraft::getHeight)).get();
                        String tmp = max.getId() + "V" + max.getHeight();
                        matrix[i][j] = String.format("%8s\t",tmp);
                        for(int k = 0; k < realMatrix[i][j].getAircrafts().size(); k++) {
                            if (realMatrix[i][j].getAircrafts().get(k).isForeignAircraft() && !realMatrix[i][j].getAircrafts().get(k).isDetected()) {
                                foreignAircrafts.add(realMatrix[i][j].getAircrafts().get(k).getId());
                                realMatrix[i][j].getAircrafts().get(k).setDetected(true);
                                attackSignal = true;
                                createEventFile(realMatrix[i][j].getAircrafts().get(k).getId(), i, j);
                            }
                        }
                    } else matrix[i][j] = "        \t";
                }
            }
        }
    }

    private void createEventFile(String id, int lat, int lon) {
        Date date = new Date();
        try (var writer = new PrintWriter(new FileOutputStream("events/event" + format.format(date) + id + ".txt"))) {
            writer.println("Radar je detektovao letjelicu " + id + " na lokaciji [" + lat + "," + lon + "]" + " u " + format.format(date));
        } catch (FileNotFoundException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't write detecting event", e);
        }
    }

    public boolean isAttackSignal() {
        boolean tmp = attackSignal;
        attackSignal = false;
        return tmp;
    }

    public void setAttackSignal(boolean attackSignal) {
        this.attackSignal = attackSignal;
    }

    public ArrayList<String> getForeignAircrafts() {
        return foreignAircrafts;
    }

    public void setForeignAircrafts(ArrayList<String> foreignAircrafts) {
        this.foreignAircrafts = foreignAircrafts;
    }
}
