package hw.weatherbit.homework;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@RestController
@RequestMapping("/")
public class ApiRest {
    public static final String API_KEY = "87d54ca4f1f15bfd6e6592f1d7456571";
    private static final String GEO_API_KEY = "04d52af6c36e4f2bbf04224f96cfcbcc";

    HashMap<Integer, ApiRequest> cache = new HashMap<>();
    private int weatherApiCalled = 0;
    private int geoApiCalled = 0;
    private int apiHits = 0;
    private int apiMisses = 0;
    private int usedCache = 0;

    @GetMapping("/api/v1/weather/location")
    public String postWeatherByLocation(@RequestParam Double lat,@RequestParam Double lng) throws IOException, ParseException {

            LatLng c = new LatLng(lat, lng);

            WeatherData data = new WeatherData();

            if (cache.containsKey(c.hashCode()) && cache.get(c.hashCode()).isValid() ){
                usedCache++;
                data = cache.get(c.hashCode()).getData();

            }else{
                data = callAPI(c.getLatitude(), c.getLongitude());
                cache.put(c.hashCode(),new ApiRequest(data));
            }

            return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/address")
    public String postWather(@RequestParam String q) throws IOException, ParseException {
        LatLng current = callGeolocationAPI(q);
        return postWeatherByLocation(current.getLatitude(), current.getLongitude());

    }

    @GetMapping("/api/v1/statistics")
    public String getStatistics() throws IOException, ParseException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("WeatherApiCalls", weatherApiCalled);
        map.put("GeolocationApiCalls", geoApiCalled);
        map.put("hits", apiHits);
        map.put("misses", apiMisses);
        map.put("cacheUsage", usedCache);
        return new Gson().toJson(map);
    }

    @Async
    public LatLng callGeolocationAPI(String name) throws IOException, ParseException {
        geoApiCalled++;
        String url_str = String.format("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s&pretty=1",
                name, GEO_API_KEY);

        URL url = new URL(url_str);

        //Make GET Request
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(GEO_API_KEY);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(name);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        LatLng finalLocation = new LatLng(firstResultLatLng.getLng(),firstResultLatLng.getLat());

        return finalLocation;

    }

    @Async
    public WeatherData callAPI(Double lat, Double lon) throws IOException, ParseException {
        weatherApiCalled++;
        String url_str = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s", lat, lon, API_KEY);
        URL url = new URL(url_str);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Get response code
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            apiMisses ++;
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
            apiHits++;
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();
            Gson gson = new Gson();
            WeatherData wd = gson.fromJson(inline, WeatherData.class);

            return wd;
        }
    }

}



