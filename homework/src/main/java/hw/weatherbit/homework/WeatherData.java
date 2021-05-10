package hw.weatherbit.homework;

import java.util.List;

public class WeatherData{
    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public int dt;
    public Sys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;


    public class Coord{
        public double lon;
        public double lat;
        @Override
        public String toString() {
            return String.format("(%f ,%f) ",lat, lon );
        }
    }

    public class Weather{
        public int id;
        public String main;
        public String description;
        public String icon;

        @Override
        public String toString() {
            return "Weather: " + main;
        }
    }

    public class Main{
        public double temp = 0.0;
        public double feels_like = 0.0;
        public double temp_min = 0.0;
        public double temp_max = 0.0;
        public int pressure = 0;
        public int humidity = 0;


        public String temperatureData() {
            return String.format("Max: %.1f K,Min: %.1fK, Feels: %.1fK", temp_max,
                    temp_min, feels_like);
        }
        public String moreData() {
            return String.format("Pressure %d hPa, Humidity: %d%s", pressure, humidity,'%' );
        }

        public String temperature() {
            return String.format("%.1fºC",temp);
        }
    }

    public class Wind{
        public double speed;
        public int deg;
    }

    public class Clouds{
        public int all;
    }

    public class Sys{
        public int type;
        public int id;
        public double message;
        public String country;
        public int sunrise;
        public int sunset;
    }
}
