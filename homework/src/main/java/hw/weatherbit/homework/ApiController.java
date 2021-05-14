package hw.weatherbit.homework;


import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static hw.weatherbit.homework.ApiCallsMethods.weatherApiCalled;


@Controller
@RequestMapping("/")
public class ApiController {

    ApiCallsMethods acm = new ApiCallsMethods();

    @GetMapping("/")
    public String getWelcome(Model model) throws IOException {
        model.addAttribute("selected", false);
        model.addAttribute("apicalls", weatherApiCalled);
        acm.addLog("Loading Web Page");
        return "weather.html";
    }
    @GetMapping("/cache")
    public String getCache(Model model) throws IOException {
        HashMap< WeatherData,String> data = new HashMap<>();


        for (String c: ApiCallsMethods.cities){
            ApiRequest d = ApiCallsMethods.cache.get(c);
            data.put(d.getData(),String.valueOf(d.isValid()));
        }
        acm.addLog("Loading Cache Web Page");
        model.addAttribute("data", data);
        return "cache.html";
    }

    @PostMapping("/")
    public String postWelcome(@RequestParam(name = "city") String redSoc, Model model) throws IOException, ParseException {
        acm.addLog("Getting Location Info For Location: "+redSoc);
        Location c;
        model.addAttribute("selected", true);
        if (ApiCallsMethods.locationCache.containsKey(redSoc)){
            c = new Location(redSoc, ApiCallsMethods.locationCache.get(redSoc));
            ApiCallsMethods.geoCache++;
        }

        else
            c = acm.callGeolocationAPIByAddress(redSoc);

        WeatherData data;
        if (ApiCallsMethods.cache.containsKey(redSoc) && ApiCallsMethods.cache.get(redSoc).isValid() ){
            ApiCallsMethods.usedCache++;
            data = ApiCallsMethods.cache.get(c.getCity()).getData();
            acm.addLog("Using Cached Data on Web Page For Location "+redSoc);
        }else{
            data = acm.callWeatherAPI(c);
            ApiCallsMethods.cache.put(c.getCity(),new ApiRequest(data));
            if (!ApiCallsMethods.cities.contains(redSoc))
                ApiCallsMethods.cities.add(redSoc);
            acm.addLog("Weather API Called on Web Controller");
        }
        acm.addLog("Loading Web Page With data For Location "+redSoc);
        model.addAttribute("weather", data);
        model.addAttribute("WeatherApiCalls", ApiCallsMethods.weatherApiCalled);
        model.addAttribute("GeolocationApiCalls", ApiCallsMethods.geoApiCalled);
        model.addAttribute("hits", ApiCallsMethods.apiHits);
        model.addAttribute("misses", ApiCallsMethods.apiMisses);
        model.addAttribute("weatherCacheUsage", ApiCallsMethods.usedCache);
        model.addAttribute("geolocatorCacheUsage", ApiCallsMethods.geoCache);
        return "weather.html";


    }





}



