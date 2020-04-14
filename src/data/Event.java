package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String id;
    private int lattitude;
    private int longitude;
    private String location;
    private Date eventTime;
    private String formattedTime;
    private String description;

    public Event(String message){
        description = message;
        String list[] = message.split(" ");
        id = list[4];
        location = list[7];
        formattedTime = list[9];
    }

    public Event(String id, int lattitude, int longitude, Date eventTime) {
        this.id = id;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.eventTime = eventTime;
        formattedTime = (new SimpleDateFormat()).format(eventTime);
        location = "["  + lattitude + "," + longitude + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLattitude() {
        return lattitude;
    }

    public void setLattitude(int lattitude) {
        this.lattitude = lattitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    @Override
    public String toString(){
        return description;
    }
}
