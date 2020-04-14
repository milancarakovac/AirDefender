package controller;

import data.Warning;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class CrashWindowViewController {
    @FXML TableView<Warning> crashesTable;
    @FXML TableColumn<Warning, String> locationColumn;
    @FXML TableColumn<Warning, String> timeColumn;
    @FXML TableColumn<Warning, String> firstAircraftColumn;
    @FXML TableColumn<Warning, String> secondAircraftColumn;

//    public void initialize(){
//        setWarnings();
//    }

    public void timeCommit(TableColumn.CellEditEvent<Warning, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+"))
            event.getRowValue().setTime(event.getNewValue());
        crashesTable.refresh();
    }

    public void locationCommit(TableColumn.CellEditEvent<Warning, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+"))
            event.getRowValue().setLocation(event.getNewValue());
        crashesTable.refresh();
    }

    public void firstCommit(TableColumn.CellEditEvent<Warning, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+"))
            event.getRowValue().setFirst(event.getNewValue());
        crashesTable.refresh();
    }

    public void secondCommit(TableColumn.CellEditEvent<Warning, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+"))
            event.getRowValue().setSecond(event.getNewValue());
        crashesTable.refresh();
    }

//    public ArrayList<Warning> getWarnings(){
//        ArrayList<Warning> list = new ArrayList<>();
//        Object[] pathList = null;
//        try {
//            pathList = Files.walk(Path.of("alert")).toArray();
//            for(int i = 1; i < pathList.length; i++){
//                try(var input = new ObjectInputStream(new FileInputStream(pathList[i].toString()))){
//                    Warning warning = (Warning)input.readObject();
//                    list.add(warning);
//                }catch (Exception ex) {
//                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read alert(crash) from a file", ex);
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't read alerts", ex);
//        }
//        return list;
//    }

    public void setWarnings(ArrayList<Warning> list){
        crashesTable.setItems(FXCollections.observableList(list));
    }
}
