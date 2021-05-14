package hw.weatherbit.homework;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class ApiRestController {

    ArrayList<Location> locHash = new ArrayList<>();
    ApiCallsMethods acm = new ApiCallsMethods();


    @GetMapping("/api/v1/weather/location")
    public String getWeatherByLocation(@RequestParam Double lat,@RequestParam Double lng) throws IOException, ParseException {
            acm.addLog("Using REST API To Get Weather By Coordinates");
            LatLng c = new LatLng(lat, lng);

            WeatherData data;
            Location n = null;
            for (Location loc: ApiCallsMethods.locations)
                if(loc.getLatitude() == lat && loc.getLongitude() == lng){
                    n=loc;
                    ApiCallsMethods.geoCache++;
                    break;
                }
            if (n==null){
                n = acm.callGeolocationAPIByLatLng(c);
            }






            if (ApiCallsMethods.cache.containsKey(n.getCity()) && ApiCallsMethods.cache.get(n.getCity()).isValid() ){
                ApiCallsMethods.usedCache++;
                data = ApiCallsMethods.cache.get(n.getCity()).getData();

            }else{
                data = acm.callWeatherAPI(n);
                ApiCallsMethods.cache.put(n.getCity(),new ApiRequest(data));
                locHash.add(n);
            }
            data.location = n.getCity();
        if (!ApiCallsMethods.cities.contains(n.getCity())){
            ApiCallsMethods.locations.add(n);
            ApiCallsMethods.cities.add(n.getCity());
        }
            return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/address")
    public String getWeatherByAddress(@RequestParam String q) throws IOException, ParseException {
        acm.addLog("Using REST API To Get Weather By Address");
        Location current = null;
        if (ApiCallsMethods.locationCache.containsKey(q)){
            current = new Location(q, ApiCallsMethods.locationCache.get(q));
            ApiCallsMethods.geoCache++;
        }

        else{
            current = acm.callGeolocationAPIByAddress(q);

        }

        WeatherData data;

        if (ApiCallsMethods.cache.containsKey(current.getCity()) && ApiCallsMethods.cache.get(current.getCity()).isValid() ){
            ApiCallsMethods.usedCache++;
            data = ApiCallsMethods.cache.get(current.getCity()).getData();

        }else{
            data = acm.callWeatherAPI(current);
            ApiCallsMethods.cache.put(current.getCity(),new ApiRequest(data));
            locHash.add(current);
        }
        data.location = q;
        if (!ApiCallsMethods.cities.contains(current.getCity())){
            ApiCallsMethods.locations.add(current);
            ApiCallsMethods.cities.add(current.getCity());
        }

        return new Gson().toJson(data);

    }

    @GetMapping("/api/v1/weather/cache")
    public String getCachedData() throws IOException {
        acm.addLog("Using REST API To Get Cached Weather Data");
        HashMap<String, ApiRequest> data=  new HashMap<>();
        for (Location l : ApiCallsMethods.locations){
            data.put(l.getCity(), ApiCallsMethods.cache.get(l.getCity()));
        }

        return new Gson().toJson(data);

    }



    @GetMapping("/api/v1/weather/statistics")
    public String getStatistics() throws IOException {
        acm.addLog("Using REST API To Get Statistics");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("WeatherApiCalls", ApiCallsMethods.weatherApiCalled);
        map.put("GeolocationApiCalls", ApiCallsMethods.geoApiCalled);
        map.put("hits", ApiCallsMethods.apiHits);
        map.put("misses", ApiCallsMethods.apiMisses);
        map.put("weatherCacheUsage", ApiCallsMethods.usedCache);
        map.put("geolocatorCacheUsage", ApiCallsMethods.geoCache);
        return new Gson().toJson(map);
    }




 

}



