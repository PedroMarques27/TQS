package hw.weatherbit.homework;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
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
import java.util.Optional;
import java.util.Scanner;

import static hw.weatherbit.homework.ApiCallsMethods.*;

@RestController
@RequestMapping("/")
public class ApiRest {
    HashMap<Integer, ApiRequest> cache = new HashMap<>();
    ArrayList<Location> locHash = new ArrayList<>();


    private int usedCache = 0;

    @GetMapping("/api/v1/weather/location")
    public String postWeatherByLocation(@RequestParam Double lat,@RequestParam Double lng) throws IOException, ParseException {

            LatLng c = new LatLng(lat, lng);

            WeatherData data = new WeatherData();
            Location n = ApiCallsMethods.callGeolocationAPIByLatLng(c);


            if (cache.containsKey(c.hashCode()) && cache.get(c.hashCode()).isValid() ){
                usedCache++;
                data = cache.get(c.hashCode()).getData();

            }else{
                data = callWeatherAPI(n);
                cache.put(c.hashCode(),new ApiRequest(data));
                locHash.add(n);
            }
            data.location = n.getCity();
            return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/address")
    public String getWeatherByAddress(@RequestParam String q) throws IOException, ParseException {
        Location current = callGeolocationAPIByAddress(q);
        LatLng c = current.getLatLng();

        WeatherData data = new WeatherData();

        if (cache.containsKey(c.hashCode()) && cache.get(c.hashCode()).isValid() ){
            usedCache++;
            data = cache.get(c.hashCode()).getData();

        }else{
            data = callWeatherAPI(current);
            cache.put(c.hashCode(),new ApiRequest(data));
            locHash.add(current);
        }
        data.location = q;
        return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/cache")
    public String getCachedData() throws IOException, ParseException {
        HashMap<String, ApiRequest> data=  new HashMap<>();
        for (Location l : locHash){
            int ha =  l.getLatLng().hashCode();
            data.put(l.getCity(), cache.get(ha));
        }

        return new Gson().toJson(data);

    }



    @GetMapping("/api/v1/weather/statistics")
    public String getStatistics() throws IOException, ParseException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("WeatherApiCalls", weatherApiCalled);
        map.put("GeolocationApiCalls", geoApiCalled);
        map.put("hits", apiHits);
        map.put("misses", apiMisses);
        map.put("cacheUsage", usedCache);
        return new Gson().toJson(map);
    }




 

}



