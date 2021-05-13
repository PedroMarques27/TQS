package hw.weatherbit.homework;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ApiCallsMethodsTest {
    private MockMvc mockMvc;

    @Mock
    private ApiCallsMethods acm = Mockito.mock(ApiCallsMethods.class);

    private String weatherExample = "{'coord':{'lon':-8.6491,'lat':40.6446},'weather':[{'id':804,'main':'Clouds','description':'overcast clouds','icon':'04d'}],'base':'stations','main':{'temp':291.36,'feels_like':291.27,'temp_min':289.85,'temp_max':292.51,'pressure':1018,'humidity':78,'sea_level':1018,'grnd_level':1017},'visibility':10000,'wind':{'speed':6.67,'deg':294,'gust':8.71},'clouds':{'all':94},'dt':1620916522,'sys':{'type':1,'id':6898,'country':'PT','sunrise':1620883178,'sunset':1620934942},'timezone':3600,'id':8010750,'name':'Glória','cod':200}";

    private Location _temp;

    @BeforeEach
    void setUp() throws IOException, ParseException {
        WeatherData dt = new Gson().fromJson(weatherExample,WeatherData.class);

        _temp = new Location(40.6446276, -8.6490691, "Churrasqueira Don Torradinho, Rua do Gravito, 3800-196 Aveiro, Portugal");
        when(acm.callWeatherAPI(_temp)).thenReturn(dt);

        when(acm.callGeolocationAPIByLatLng(new LatLng(40.6446276, -8.6490691))).thenReturn(_temp);

        when(acm.callGeolocationAPIByAddress("")).thenReturn(
                _temp);
    }

    @Test
    void callWeatherAPI() {

    }

    @Test
    void callGeolocationAPIByLatLng() {
    }

    @Test
    void callGeolocationAPIByAddress() {
    }


}