package main;

import simulation.Simulator;

import java.io.*;
import java.util.Properties;
import java.util.logging.*;

public class Main {
    private static final String CONFIG_PROPERTIES = "src/resources/config.properties";
    private static final String LOGS = "./logs/exceptionLogs.log";
    static Properties properties;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Logger root=Logger.getLogger("");
        root.addHandler(new ConsoleHandler());
        try{
            FileHandler fileHandler=new FileHandler(LOGS,true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            root.addHandler(fileHandler);
            root.setUseParentHandlers(false);
            root.setLevel(Level.ALL);
        }catch (IOException ex){
            root.log(Level.INFO,"File logging doesn't work!!!",ex);
        }


        setDefaultProperties();


        int width = Integer.parseInt(properties.getProperty("matrixWidth"));
        int height = Integer.parseInt(properties.getProperty("matrixHeight"));
        Simulator simulator = new Simulator(width, height, Integer.parseInt(properties.getProperty("creationInterval")),
                Integer.parseInt(properties.getProperty("heightRange")));
        simulator.start();
    }

    public static void setDefaultProperties(){
        try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
            properties = new Properties();
            properties.load(input);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Input error!",ex);
        }

        try(OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)){
            properties.setProperty("numberOfForeignAircrafts","-1");
            properties.setProperty("numberOfDomesticAircrafts", "-1");
            properties.setProperty("isBanned","false");
            properties.store(output, null);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Output error!",ex);
        }
    }
}
