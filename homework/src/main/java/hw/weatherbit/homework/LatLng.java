package hw.weatherbit.homework;

public class LatLng {
    private double latitude = 0.0;
    private double longitude = 0.0;

    public LatLng() {

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        if (latitude<=90 && latitude>=-90)
            this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        if (longitude<=180 && longitude>=-180)
            this.longitude = longitude;

    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) latitude;
        hash = 31 * hash + (int) longitude;
        return hash;
    }
}
