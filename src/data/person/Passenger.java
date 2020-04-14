package data.person;

public class Passenger extends Person {
    private int passportNumber;

    public Passenger(int passportNumber, String name, String surname) {
        super(name, surname);
        this.passportNumber = passportNumber;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        return super.toString() + "\nBroj pasosa: " + getPassportNumber();
    }
}
