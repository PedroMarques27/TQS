package hw.weatherbit.homework;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

class ApiRestTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ApiRestController restController;

    @Mock
    private ApiCallsMethods acm = Mockito.mock(ApiCallsMethods.class);

    String weatherExample = "{'coord':{'lon':-8.6491,'lat':40.6446},'weather':[{'id':804,'main':'Clouds','description':'overcast clouds','icon':'04d'}],'base':'stations','main':{'temp':291.36,'feels_like':291.27,'temp_min':289.85,'temp_max':292.51,'pressure':1018,'humidity':78,'sea_level':1018,'grnd_level':1017},'visibility':10000,'wind':{'speed':6.67,'deg':294,'gust':8.71},'clouds':{'all':94},'dt':1620916522,'sys':{'type':1,'id':6898,'country':'PT','sunrise':1620883178,'sunset':1620934942},'timezone':3600,'id':8010750,'name':'Glória','cod':200}";
    String weatherExample2 = "{\"coord\":{\"lon\":-8.9491,\"lat\":42.6446},\"weather\":[{\"main\":\"Clouds\"}],\"base\":\"stations\",\"main\":{\"temp\":288.03,\"feels_like\":287.56,\"temp_min\":283.97,\"temp_max\":291.51,\"pressure\":1016,\"humidity\":76},\"visibility\":10000,\"wind\":{\"speed\":4.43},\"clouds\":{\"all\":83},\"dt\":1620920392,\"sys\":{\"type\":2,\"message\":0.0,\"country\":\"ES\",\"sunrise\":1620882939,\"sunset\":1620935326},\"timezone\":7200,\"location\":\"15948 Petón do Currumil, Espanha\"}";

    LatLng point1 = new LatLng(40.6446276,-8.6490691);
    LatLng point2 = new LatLng(42.6446276,-8.9490691);

    @BeforeEach
    void setUp() throws IOException, ParseException {
        ApiRequest.TIME_OUT = 120000;
        WeatherData dt = new Gson().fromJson(weatherExample,WeatherData.class);
        restController = new ApiRestController();
        when(acm.callWeatherAPI(new Location(40.6446276,-8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"))).thenReturn(
                dt
        );

        when(acm.callGeolocationAPIByLatLng(point1)).thenReturn(
                new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")
        );

        when(acm.callGeolocationAPIByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")).thenReturn(
                new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")
        );

        WeatherData dt2 = new Gson().fromJson(weatherExample2,WeatherData.class);

        when(acm.callWeatherAPI(new Location(42.6446276,-8.9490691, "15948 Petón do Currumil, Espanha"))).thenReturn(
                dt
        );

        when(acm.callGeolocationAPIByLatLng(point2)).thenReturn(
                new Location(42.6446276, -8.9490691, "15948 Petón do Currumil, Espanha")
        );

        when(acm.callGeolocationAPIByAddress("15948 Petón do Currumil, Espanha")).thenReturn(
                new Location(42.6446276, -8.9490691, "15948 Petón do Currumil, Espanha")
        );


        ApiCallsMethods.geoApiCalled = 0;
        ApiCallsMethods.weatherApiCalled = 0;
        ApiCallsMethods.apiHits = 0;
        ApiCallsMethods.apiMisses = 0;
        ApiCallsMethods.usedCache = 0;
        ApiCallsMethods.geoCache=0;
    }

    @Test
    @Order(2)
    void getWeatherByLocation() throws Exception {
            String result = restController.getWeatherByLocation(40.6446276, -8.6490691);
            assertThat(result, containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
    }

    @Test
    @Order(3)
    void getWeatherByAddress() throws IOException, ParseException {
        String result = restController.getWeatherByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal");
        assertThat(result,containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
    }

    @Test
    @Order(4)
    void getCachedData() throws IOException, ParseException {
        restController.getWeatherByLocation(40.6446276, -8.6490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);

        String data = restController.getCachedData();
        assertThat(data, containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
        assertThat(data, containsString("15948 Petón do Currumil, Espanha"));

    }
    @Test
    @Order(5)
    void cacheDoesntAddIfExisting() throws IOException, ParseException, InterruptedException {

        restController.getWeatherByLocation(40.6446276, -8.6490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);


       assertThat(acm.cache.keySet().size(), equalTo(2));

    }


    @Test
    @Order(7)
    void addedToLocations() throws IOException, ParseException {
        restController.getWeatherByLocation(point2.getLatitude(), point2.getLongitude());
        restController.getWeatherByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal");

        ArrayList<String> cities = new ArrayList<>();
        for (Location l: ApiCallsMethods.locations) {
            System.out.println(l.getCity());
            cities.add(l.getCity());
        }
        assertThat(cities, hasItems("15948 Petón do Currumil, Espanha","Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));

    }

    @Test
    @Order(6)
    void cacheNotValidUpdate() throws IOException, ParseException, InterruptedException {
        acm.cache.clear();
        ApiRequest.TIME_OUT=5000;
        restController.getWeatherByLocation(42.6446276, -8.9490691);
        assertThat(acm.cache.keySet().size(),equalTo(1));
        String city = (String) acm.cache.keySet().toArray()[0];
        assertThat(acm.cache.get(city).isValid(), equalTo(true));


        Thread.sleep(6000);
        assertThat(acm.cache.get(city).isValid(), equalTo(false));
        restController.getWeatherByLocation(42.6446276, -8.9490691);
        assertThat(acm.cache.keySet().size(),equalTo(1));

        assertThat(acm.cache.get(city).isValid(), equalTo(true));



    }

    @Test
    @Order(1)
    void getStatistics() throws IOException, ParseException {
        ApiCallsMethods.cache.clear();
        ApiCallsMethods.locationCache.clear();


        restController.getWeatherByLocation(40.6446276, -8.6490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);
        restController.getWeatherByLocation(40.6446276, -8.6490691);

        String dataJson = restController.getStatistics();
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        Map<String, Integer> statistics = new Gson().fromJson(dataJson, type);

        assertThat(statistics.get("hits"), equalTo(2));
        assertThat(statistics.get("GeolocationApiCalls"), equalTo(2));
        assertThat(statistics.get("WeatherApiCalls"), equalTo(2));
        assertThat(statistics.get("weatherCacheUsage"), equalTo(1));
        assertThat(statistics.get("geolocatorCacheUsage"), equalTo(1));
        assertThat(statistics.get("misses"), equalTo(0));

    }
}