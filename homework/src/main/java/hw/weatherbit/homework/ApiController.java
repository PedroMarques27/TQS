package hw.weatherbit.homework;


import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static hw.weatherbit.homework.ApiCallsMethods.weatherApiCalled;


@Controller
@RequestMapping("/")
public class ApiController {
    ArrayList<Location> locations = Location.getLocations();
    HashMap<Location, ApiRequest> cache = new HashMap<>();
    ApiCallsMethods acm = new ApiCallsMethods();

    private static final String GEO_API_KEY = "04d52af6c36e4f2bbf04224f96cfcbcc";
    @GetMapping("/")
    public String getWelcome(Model model) {
        model.addAttribute("locations", locations);
        model.addAttribute("selected", false);
        model.addAttribute("apicalls", weatherApiCalled);
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
                    ApiCallsMethods.usedCache++;
                    data = cache.get(c).getData();

                }else{

                    weatherApiCalled++;
                    data = acm.callWeatherAPI(c);
                    cache.put(c,new ApiRequest(data));
                }

                model.addAttribute("weather", data);
                model.addAttribute("apicalls", weatherApiCalled);
                return "weather.html";
            }
        }
        return "";
    }





}



