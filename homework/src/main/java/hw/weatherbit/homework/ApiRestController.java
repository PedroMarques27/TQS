package hw.weatherbit.homework;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import static hw.weatherbit.homework.ApiCallsMethods.*;

@RestController
@RequestMapping("/")
public class ApiRestController {
    HashMap<Integer, ApiRequest> cache = new HashMap<>();
    ArrayList<Location> locHash = new ArrayList<>();
    ApiCallsMethods acm = new ApiCallsMethods();


    @GetMapping("/api/v1/weather/location")
    public String getWeatherByLocation(@RequestParam Double lat,@RequestParam Double lng) throws IOException, ParseException {
            acm.addLog("Using REST API To Get Weather By Coordinates");
            LatLng c = new LatLng(lat, lng);

            WeatherData data = new WeatherData();
            ApiCallsMethods.geoApiCalled++;
            Location n = acm.callGeolocationAPIByLatLng(c);


            if (cache.containsKey(c.hashCode()) && cache.get(c.hashCode()).isValid() ){
                ApiCallsMethods.usedCache++;
                data = cache.get(c.hashCode()).getData();

            }else{
                ApiCallsMethods.weatherApiCalled++;
                data = acm.callWeatherAPI(n);
                cache.put(c.hashCode(),new ApiRequest(data));
                locHash.add(n);
            }
            data.location = n.getCity();
            return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/address")
    public String getWeatherByAddress(@RequestParam String q) throws IOException, ParseException {
        acm.addLog("Using REST API To Get Weather By Address");
        Location current = acm.callGeolocationAPIByAddress(q);
        ApiCallsMethods.geoApiCalled++;
        LatLng c = current.getLatLng();

        WeatherData data = new WeatherData();

        if (cache.containsKey(c.hashCode()) && cache.get(c.hashCode()).isValid() ){
            ApiCallsMethods.usedCache++;
            data = cache.get(c.hashCode()).getData();

        }else{
            ApiCallsMethods.weatherApiCalled++;
            data = acm.callWeatherAPI(current);
            cache.put(c.hashCode(),new ApiRequest(data));
            locHash.add(current);
        }
        data.location = q;
        return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/cache")
    public String getCachedData() throws IOException, ParseException {
        acm.addLog("Using REST API To Get Cached Weather Data");
        HashMap<String, ApiRequest> data=  new HashMap<>();
        for (Location l : locHash){
            int ha =  l.getLatLng().hashCode();
            data.put(l.getCity(), cache.get(ha));
        }

        return new Gson().toJson(data);

    }



    @GetMapping("/api/v1/weather/statistics")
    public String getStatistics() throws IOException, ParseException {
        acm.addLog("Using REST API To Get Statistics");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("WeatherApiCalls", ApiCallsMethods.weatherApiCalled);
        map.put("GeolocationApiCalls", ApiCallsMethods.geoApiCalled);
        map.put("hits", ApiCallsMethods.apiHits);
        map.put("misses", ApiCallsMethods.apiMisses);
        map.put("cacheUsage", ApiCallsMethods.usedCache);
        return new Gson().toJson(map);
    }




 

}



