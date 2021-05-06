package hw.weatherbit.homework;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class ApiController {
    public static final String API_KEY = "d5657cdd44274ec7967c25deef3a9f2f";
    ArrayList<Location> locations = new ArrayList<>();

    @Async
    @Scheduled(fixedRate = 10000)
    public void callAPI(String city) throws IOException, ParseException {
        Location currentLocation = new Location();
        for (Location locs: locations) {
            if (locs.getCity().equalsIgnoreCase(city)) {
                currentLocation = locs;
                break;
            }
        }
        String url_str = String.format("https://api.weatherbit.io/v2.0/current/airquality?lat=%f&lon=%f&key=%s",
                currentLocation.getLatitude(), currentLocation.getLongitude(), API_KEY);
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

            //Parse the string into a json object
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(inline);

            //Get the required object from the above created object
            JSONObject obj = (JSONObject) data_obj;
            System.out.println(obj);
        }
    }


}
