package hw.weatherbit.homework;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiCallsMethods {
    public static final String API_KEY = "87d54ca4f1f15bfd6e6592f1d7456571";

    private static final String GEO_API_KEY = "04d52af6c36e4f2bbf04224f96cfcbcc";


    public static int geoApiCalled = 0;
    public static int weatherApiCalled = 0;
    public static int apiHits = 0;
    public static int apiMisses = 0;
    public static int usedCache = 0;

    public WeatherData callWeatherAPI(Location city) throws IOException, ParseException {
        String url_str = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s",
                city.getLatitude(), city.getLongitude(), API_KEY);
        addLog(String.format("Called Weather API For Location %s (%.4f,%.4g)",city.getCity(),city.getLatitude(),city.getLongitude()));
        URL url = new URL(url_str);
        //Make GET Request
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        //Get response code
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            apiMisses++;
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
            Gson gson = new Gson();
            WeatherData wd = gson.fromJson(inline,WeatherData.class);

            return wd;
        }
    }


    public Location callGeolocationAPIByLatLng(LatLng latlng) throws IOException, ParseException {
        addLog("Calling jOpenCageGeocoder: Coordinates to Address");
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(GEO_API_KEY);

        JOpenCageReverseRequest request = new JOpenCageReverseRequest(latlng.getLatitude(), latlng.getLongitude());
        request.setLanguage("pt");
        request.setNoDedupe(true);
        request.setLimit(5);
        request.setNoAnnotations(true);
        request.setMinConfidence(3);
        JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
        String formattedAddress = response.getResults().get(0).getFormatted();

        Location finalLocation = new Location(latlng.getLatitude(), latlng.getLongitude(), formattedAddress);

        return finalLocation;
    }

    public Location callGeolocationAPIByAddress(String name) throws IOException, ParseException {
        addLog("Called jOpenCageGeocoder: Address to Coordinates");
        String url_str = String.format("https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s&pretty=1",
                name, GEO_API_KEY);

        URL url = new URL(url_str);
        Location finalLocation = new Location();
        finalLocation.setCity(name);
        //Make GET Request
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(GEO_API_KEY);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(name);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        finalLocation.setLatitude(firstResultLatLng.getLat());
        finalLocation.setLongitude(firstResultLatLng.getLng());

        return finalLocation;

    }

    public void addLog(String logData) throws IOException {
        File file = new File("log.txt");
        CharSink chs = Files.asCharSink(
                file, Charsets.UTF_8, FileWriteMode.APPEND);
        chs.write(System.currentTimeMillis()+": "+logData+"\n");
    }
}
