package data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Warning implements Serializable {
    private String description;
    private String time;
    private int lattitude;
    private int longitude;
    private DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd.MM.yyyy");
    private String first;
    private String second;
    private String location;

    public Warning(String description, Date date, int lattitude, int longitude){
        time = dateFormat.format(date);
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.description = description;
        String list[] = description.split(" ");
        first = list[2];
        second = list[4];
        location = "[" + lattitude + "," + longitude + "]";
    }

    @Override
    public String toString(){
        return "Desio se sudar na lokaciji [" + lattitude + ", " + longitude + "] u vrijeme: " + time + "\nOpis sudara: " + description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time + lattitude + longitude;
    }

    public void setTime(String time) {
        this.time = time;
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

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
