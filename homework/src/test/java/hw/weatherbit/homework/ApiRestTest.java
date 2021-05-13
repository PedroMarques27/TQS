package hw.weatherbit.homework;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static hw.weatherbit.homework.ApiCallsMethods.*;
import static hw.weatherbit.homework.ApiCallsMethods.apiMisses;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class ApiRestTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ApiRestController restController;

    @Mock
    private ApiCallsMethods acm = Mockito.mock(ApiCallsMethods.class);

    String weatherExample = "{'coord':{'lon':-8.6491,'lat':40.6446},'weather':[{'id':804,'main':'Clouds','description':'overcast clouds','icon':'04d'}],'base':'stations','main':{'temp':291.36,'feels_like':291.27,'temp_min':289.85,'temp_max':292.51,'pressure':1018,'humidity':78,'sea_level':1018,'grnd_level':1017},'visibility':10000,'wind':{'speed':6.67,'deg':294,'gust':8.71},'clouds':{'all':94},'dt':1620916522,'sys':{'type':1,'id':6898,'country':'PT','sunrise':1620883178,'sunset':1620934942},'timezone':3600,'id':8010750,'name':'Glória','cod':200}";
    String weatherExample2 = "{\"coord\":{\"lon\":-8.9491,\"lat\":42.6446},\"weather\":[{\"main\":\"Clouds\"}],\"base\":\"stations\",\"main\":{\"temp\":288.03,\"feels_like\":287.56,\"temp_min\":283.97,\"temp_max\":291.51,\"pressure\":1016,\"humidity\":76},\"visibility\":10000,\"wind\":{\"speed\":4.43},\"clouds\":{\"all\":83},\"dt\":1620920392,\"sys\":{\"type\":2,\"message\":0.0,\"country\":\"ES\",\"sunrise\":1620882939,\"sunset\":1620935326},\"timezone\":7200,\"location\":\"15948 Petón do Currumil, Espanha\"}";

    @BeforeEach
    void setUp() throws IOException, ParseException {
        WeatherData dt = new Gson().fromJson(weatherExample,WeatherData.class);
        restController = new ApiRestController();
        when(acm.callWeatherAPI(new Location(40.6446276,-8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"))).thenReturn(
                dt
        );

        when(acm.callGeolocationAPIByLatLng(new LatLng(40.6446276,-8.6490691))).thenReturn(
                new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")
        );

        when(acm.callGeolocationAPIByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")).thenReturn(
                new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")
        );

        WeatherData dt2 = new Gson().fromJson(weatherExample2,WeatherData.class);

        when(acm.callWeatherAPI(new Location(42.6446276,-8.9490691, "15948 Petón do Currumil, Espanha"))).thenReturn(
                dt
        );

        when(acm.callGeolocationAPIByLatLng(new LatLng(42.6446276,-8.6490691))).thenReturn(
                new Location(42.6446276, -8.9490691, "15948 Petón do Currumil, Espanha")
        );

        when(acm.callGeolocationAPIByAddress("15948 Petón do Currumil, Espanha")).thenReturn(
                new Location(42.6446276, -8.9490691, "15948 Petón do Currumil, Espanha")
        );

    }

    @Test
    void getWeatherByLocation() throws Exception {
            String result = restController.getWeatherByLocation(40.6446276, -8.6490691);
            assertThat(result, containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
    }

    @Test
    void getWeatherByAddress() throws IOException, ParseException {
        String result = restController.getWeatherByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal");
        assertThat(result,containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
    }

    @Test
    void getCachedData() throws IOException, ParseException {
        restController.getWeatherByLocation(40.6446276, -8.6490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);

        String data = restController.getCachedData();
        assertThat(data, containsString("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal"));
        assertThat(data, containsString("15948 Petón do Currumil, Espanha"));

    }

    @Test
    void getStatistics() throws IOException, ParseException {
        restController.getWeatherByLocation(40.6446276, -8.6490691);
        restController.getWeatherByLocation(42.6446276, -8.9490691);
        restController.getWeatherByLocation(40.6446276, -8.6490691);

        String dataJson = restController.getStatistics();
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        HashMap<String, Integer> statistics = new Gson().fromJson(dataJson, type);

        assertThat(statistics.get("hits"), equalTo(2));
        assertThat(statistics.get("GeolocationApiCalls"), equalTo(2));
        assertThat(statistics.get("WeatherApiCalls"), equalTo(2));
        assertThat(statistics.get("hits"), equalTo(2));
        assertThat(statistics.get("misses"), equalTo(2));

    }
}