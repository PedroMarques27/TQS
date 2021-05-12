package hw.weatherbit.homework;

public class LatLng {
    private double latitude;
    private double longitude;

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
