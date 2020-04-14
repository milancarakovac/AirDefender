package controller;

import data.Event;
import data.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import main.Main;
import util.FileWatcher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewController {
    private static final String MAP_TXT = "src/resources/map.txt";
    private static final String CONFIG_PROPERTIES = "src/resources/config.properties";
    private static final String POPUP_FXML = "../view/WarningPopUpView.fxml";
    private static final String EVENT_FXML = "../view/EventWindowView.fxml";
    private static final String CRASH_FXML = "../view/CrashWindowView.fxml";
    private Timer timer;
    private String matrix;
    private Properties properties;
    private FileWatcher eventWatcher;
    private FileWatcher alertWatcher;
    private Stage stage;
    private Stage eventStage;
    private Stage crashStage;
    private ArrayList<Warning> crashes;
    private ArrayList<Event> events;

    @FXML TextArea textArea;
    @FXML Label label;
    @FXML Button legendButton;

    public void initialize(){
        crashes = getWarnings();
        events = getEvents();
        stage = new Stage();
        eventStage = new Stage();
        crashStage = new Stage();
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Legenda:\nFH-protivpozarnihelikopter\nPH-putnicki helikopter\nTH-transportni helikopter\nPP-putnicki avion\n" +
                "TP-transportni avion\nFP-protivpozarni avion\nUA-bespilotna letjelica\nDH-domaci lovac\nDB-domaci bombarder\n" +
                "EA-neprijateljska letjelica");
        tooltip.setStyle("-fx-base: #AE3522; " + "-fx-text-fill: blue;");
        legendButton.setTooltip(tooltip);
        try{
            Path path1 = Paths.get("alert");
            alertWatcher = new FileWatcher(path1, "", StandardWatchEventKinds.ENTRY_MODIFY);
            alertWatcher.start();
            Path path2 = Paths.get("events");
            eventWatcher = new FileWatcher(path2, "", StandardWatchEventKinds.ENTRY_MODIFY);
            eventWatcher.start();
        }catch (Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't create event and/or alert watcher", ex);
        }
        timer = new Timer();
        properties = new Properties();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                readFromFile();
                textArea.setText(matrix);
            }
        },0,1000);
        (new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't sleep", ex);
                }
                    if (alertWatcher.isThereChange()) {
                        try(var input = new ObjectInputStream(new FileInputStream(alertWatcher.getAffectedFile().toFile()))){
                            Warning warning = (Warning)input.readObject();
                            crashes.add(warning);
                            Platform.runLater(
                                   () -> {
                                        createPopUpWindow(warning.toString());
                                    }
                            );
                        }catch (Exception ex) {
                            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read warning from a file", ex);
                        }
                    }
                if (eventWatcher.isThereChange()) {
                    Platform.runLater(() -> {
                        try (var br = new BufferedReader(new FileReader(eventWatcher.getAffectedFile().toFile()))) {
                            Event event = new Event(br.readLine());
                            label.setText(event.toString());
                            //events = getEvents();
							events.add(event);
                        } catch (Exception ex) {
                            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read from event file", ex);
                        }
                    });
                }
            }
        })).start();
    }

    public void readFromFile() {
        File file = new File(MAP_TXT);
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            matrix = "";
            String st;
            while ((st = br.readLine()) != null) {
                matrix += st + "\n";
            }
        }catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.WARNING,"Can't read from file!", ex);
        }
    }

    @FXML
    public void setBan(ActionEvent actionEvent) {
        try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
            properties.load(input);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Input error!",ex);
        }

        try(OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)){
            properties.setProperty("isBanned","true");
            properties.store(output, null);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Output error!",ex);
        }
    }

    @FXML
    public void removeBan(ActionEvent actionEvent) {
        try(InputStream input = new FileInputStream(CONFIG_PROPERTIES)){
            properties.load(input);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Input error!",ex);
        }

        try(OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)){
            properties.setProperty("isBanned","false");
            properties.store(output, null);
        }catch(IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,"Output error!",ex);
        }
    }

    public void createPopUpWindow(String message) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(POPUP_FXML));
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.WARNING,"Can't open pop up window!", ex);
        }
        var controller = loader.<WarningPopUpViewController>getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Upozorenje");
        controller.setText(message);
        stage.show();
    }

    public void showCrashes(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CRASH_FXML));
        Parent root = null;
        try{
            root = loader.load();
        } catch (IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.WARNING,"Can't open crash window!", ex);
        }
        var controller = loader.<CrashWindowViewController>getController();
        controller.setWarnings(crashes);
        Scene scene = new Scene(root);
        crashStage.setScene(scene);
        crashStage.setTitle("Tabela sudara");
        crashStage.show();
    }

    public void showEvents(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(EVENT_FXML));
        Parent root = null;
        try{
            root = loader.load();
        } catch (IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.WARNING,"Can't open event window!", ex);
        }
        var controller = loader.<EventWindowViewController>getController();
        Scene scene = new Scene(root);
        eventStage.setScene(scene);
        eventStage.setTitle("Tabela dogadjaja");
        controller.setEvents(events);
        eventStage.show();
    }

    public ArrayList<Warning> getWarnings(){
        ArrayList<Warning> list = new ArrayList<>();
        Object[] pathList = null;
        try {
            pathList = Files.walk(Path.of("alert")).toArray();
            for(int i = 1; i < pathList.length; i++){
                try(var input = new ObjectInputStream(new FileInputStream(pathList[i].toString()))){
                    Warning warning = (Warning)input.readObject();
                    list.add(warning);
                }catch (Exception ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read alert(crash) from a file", ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read alerts", ex);
        }
        return list;
    }

    public ArrayList<Event> getEvents(){
        ArrayList<Event> list = new ArrayList<>();
        Object[] pathList = null;
        try {
            pathList = Files.walk(Path.of("events")).toArray();
            for(int i = 1; i < pathList.length; i++){
                Event event = new Event(Files.readString(Path.of(pathList[i].toString())));
                list.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
