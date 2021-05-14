package hw.weatherbit.homework;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private LatLng coordinates = new LatLng();

    private String city;

    public Location(double latitude, double longitude, String city) {
        this.coordinates = new LatLng(latitude, longitude);
        this.city = city;
    }

    public Location() {

    }

    public Location(String name, LatLng latLng) {
        this.city = name;
        this.coordinates = latLng;
    }

    public LatLng getLatLng(){
        if (coordinates == null)
            throw new NullPointerException();
        return this.coordinates;
    }

    public double getLatitude() {
        return coordinates.getLatitude();
    }

    public void setLatitude(double latitude) {
        this.coordinates.setLatitude(latitude);
    }

    public double getLongitude() {
        return coordinates.getLongitude();
    }

    public void setLongitude(double longitude) {
        this.coordinates.setLongitude(longitude);
    }

    public String getCity() {
        if (city == null)
            throw new NullPointerException();
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static List<Location> getLocations(){
        ArrayList<Location> temp = new ArrayList<>();
        var l = new Location(51.5074, 0.1278,"London");
        var lisbon = new Location(38.7223, 9.1393, "Lisbon");
        var budapest = new Location(47.4979, 19.0402,"Budapest");
        var paris = new Location(48.8566, 2.3522,"Paris");
        temp.add(l);
        temp.add(lisbon);
        temp.add(budapest);
        temp.add(paris);
        return  temp;
    }
}
