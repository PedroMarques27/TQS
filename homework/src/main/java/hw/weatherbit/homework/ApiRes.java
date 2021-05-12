package hw.weatherbit.homework;

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
public class ApiRes {
    public static final String API_KEY = "87d54ca4f1f15bfd6e6592f1d7456571";
    HashMap<Integer, ApiRequest> cache = new HashMap<>();
    private int apiCalled = 0;
    private int apiHits = 0;
    private int apiMisses = 0;
    private int usedCache = 0;

    @GetMapping("/api/v1/weather")
    public String postWather(@RequestParam Double lat,@RequestParam Double lng) throws IOException, ParseException {

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

    @GetMapping("/api/v1/statistics")
    public String getStatistics() throws IOException, ParseException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("calls", apiCalled);
        map.put("hits", apiHits);
        map.put("misses", apiMisses);
        map.put("cacheUsage", usedCache);
        return new Gson().toJson(map);
    }



    @Async
    public WeatherData callAPI(Double lat, Double lon) throws IOException, ParseException {
        apiCalled++;
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

            //Write all the JSON data into a string
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }
            scanner.close();


            // Creating a Gson Object
            Gson gson = new Gson();

            // Converting json to object
            // first parameter should be prpreocessed json
            // and second should be mapping class
            WeatherData wd = gson.fromJson(inline, WeatherData.class);

            return wd;
        }
    }

}



