package controller;

import data.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class EventWindowViewController {

    @FXML TableView<Event> eventsTable;
    @FXML TableColumn<Event, String> idColumn;
    @FXML TableColumn<Event, String> timeColumn;
    @FXML TableColumn<Event, String> locationColumn;


    public void timeCommit(TableColumn.CellEditEvent<Event, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+") )
            event.getRowValue().setFormattedTime(event.getNewValue());
        eventsTable.refresh();
    }

    public void idCommit(TableColumn.CellEditEvent<Event, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+") )
            event.getRowValue().setId(event.getNewValue());
        eventsTable.refresh();
    }

    public void locationCommit(TableColumn.CellEditEvent<Event, String> event) {
        if(event.getNewValue().matches("[0-9a-zA-Z]+") )
            event.getRowValue().setLocation(event.getNewValue());
        eventsTable.refresh();
    }

    public void setEvents(ArrayList<Event> list){
        eventsTable.setItems(FXCollections.observableList(list));
    }
}
