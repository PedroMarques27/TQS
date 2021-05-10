package hw.weatherbit.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class ApiController {
    public static final String API_KEY = "87d54ca4f1f15bfd6e6592f1d7456571";
    ArrayList<Location> locations = Location.getLocations();
    HashMap<Location, ApiRequest> cache = new HashMap<>();
    private int apiCalled = 0;

    @GetMapping("/")
    public String getWelcome(Model model) {
        model.addAttribute("locations", locations);
        model.addAttribute("selected", false);
        model.addAttribute("apicalls", apiCalled);
        return "weather.html";
    }

    @PostMapping("/")
    public String postWelcome(@RequestParam(name = "redSoc") String redSoc, Model model) throws IOException, ParseException {
        for (Location c : locations){
            if (c.getCity().equalsIgnoreCase(redSoc)){
                model.addAttribute("locations", locations);
                model.addAttribute("selected", redSoc);

                WeatherData data = new WeatherData();
                if (cache.containsKey(c) && cache.get(c).isValid() ){

                        data = cache.get(c).getData();

                }else{
                    data = callAPI(c);
                    cache.put(c,new ApiRequest(data));
                }

                model.addAttribute("weather", data);
                model.addAttribute("apicalls", apiCalled);
                return "weather.html";
            }
        }
        return "";
    }

    @Async
    public WeatherData callAPI(Location city) throws IOException, ParseException {
        apiCalled++;
        String url_str = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s",
                city.getLatitude(), city.getLongitude(), API_KEY);
        System.out.println(url_str);
        URL url = new URL(url_str);

        //Make GET Request
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        //Get response code
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
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
            WeatherData wd = gson.fromJson(inline,WeatherData.class);


            System.out.println(inline);
            System.out.println(wd.dt);
            return wd;
        }
    }


}
