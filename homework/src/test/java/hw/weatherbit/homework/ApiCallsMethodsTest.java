package hw.weatherbit.homework;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ApiCallsMethodsTest {
    private MockMvc mockMvc;

    @Mock
    private ApiCallsMethods acm = Mockito.mock(ApiCallsMethods.class);

    String weatherExample = "{'coord':{'lon':-8.6491,'lat':40.6446},'weather':[{'id':804,'main':'Clouds','description':'overcast clouds','icon':'04d'}],'base':'stations','main':{'temp':291.36,'feels_like':291.27,'temp_min':289.85,'temp_max':292.51,'pressure':1018,'humidity':78,'sea_level':1018,'grnd_level':1017},'visibility':10000,'wind':{'speed':6.67,'deg':294,'gust':8.71},'clouds':{'all':94},'dt':1620916522,'sys':{'type':1,'id':6898,'country':'PT','sunrise':1620883178,'sunset':1620934942},'timezone':3600,'id':8010750,'name':'Glória','cod':200}";
    String weatherExample2 = "{\"coord\":{\"lon\":-8.9491,\"lat\":42.6446},\"weather\":[{\"main\":\"Clouds\"}],\"base\":\"stations\",\"main\":{\"temp\":288.03,\"feels_like\":287.56,\"temp_min\":283.97,\"temp_max\":291.51,\"pressure\":1016,\"humidity\":76},\"visibility\":10000,\"wind\":{\"speed\":4.43},\"clouds\":{\"all\":83},\"dt\":1620920392,\"sys\":{\"type\":2,\"message\":0.0,\"country\":\"ES\",\"sunrise\":1620882939,\"sunset\":1620935326},\"timezone\":7200,\"location\":\"15948 Petón do Currumil, Espanha\"}";

    LatLng point1 = new LatLng(40.6446276, -8.6490691);
    LatLng point2 = new LatLng(42.6446276, -8.9490691);
    Location _temp  = new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal");
    Location _temp2 = new Location(42.6446276, -8.9490691, "15948 Petón do Currumil, Espanha");

    @BeforeEach
    void setUp() throws IOException, ParseException {
        WeatherData dt = new Gson().fromJson(weatherExample,WeatherData.class);
        WeatherData dt2 = new Gson().fromJson(weatherExample2,WeatherData.class);
        dt.location = "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal";
        dt2.location = "15948 Petón do Currumil, Espanha";

        when(acm.callWeatherAPI(_temp)).thenReturn(dt);
        when(acm.callGeolocationAPIByLatLng(point1)).thenReturn(_temp);
        when(acm.callGeolocationAPIByAddress("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal")).thenReturn(_temp);

        when(acm.callWeatherAPI(_temp2)).thenReturn(dt2);
        when(acm.callGeolocationAPIByLatLng(point2)).thenReturn(_temp2);
        when(acm.callGeolocationAPIByAddress("15948 Petón do Currumil, Espanha")).thenReturn(_temp2);

    }

    @Test
    void callWeatherAPI() throws IOException, ParseException {
        WeatherData data = acm.callWeatherAPI(_temp);
        assertThat(data.location,equalTo("Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal") );


    }




    @Test
    void callGeolocationAPIByLatLng() throws IOException, ParseException {
        Location data = acm.callGeolocationAPIByLatLng(point2);
        System.out.println(data.getLatitude());
        assertThat(data.getCity(), equalTo("15948 Petón do Currumil, Espanha"));
    }

    @Test
    void callGeolocationAPIByAddress() throws IOException, ParseException {
        Location data = acm.callGeolocationAPIByAddress("15948 Petón do Currumil, Espanha");
        assertThat(data.getLatitude(), equalTo( 42.6446276));
        assertThat(data.getLongitude(), equalTo( -8.9490691));

    }


}