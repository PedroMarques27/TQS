package hw.weatherbit.homework;

import java.util.ArrayList;

public class Location {
    private double latitude;
    private double longitude;

    private String city;

    public Location(double latitude, double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public Location() {

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static ArrayList<Location> getLocations(){
        ArrayList<Location> temp = new ArrayList<>();
        Location l = new Location(51.5074, 0.1278,"London");
        Location lisbon = new Location(38.7223, 9.1393, "Lisbon");
        Location budapest = new Location(47.4979, 19.0402,"Budapest");
        Location paris = new Location(48.8566, 2.3522,"Paris");
        temp.add(l);
        temp.add(lisbon);
        temp.add(budapest);
        temp.add(paris);
        return  temp;
    }
}
