package data.person;

public class Pilot extends Person {
    private String license;

    public Pilot(String license, String name, String surname) {
        super(name, surname);
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return super.toString() + "\nLicenca: " + getLicense();
    }
}
