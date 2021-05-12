package hw.weatherbit.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiRequestTest {
    private ApiRequest apiRequest;

    @Test
    void isValidBefore() throws InterruptedException {

        apiRequest = new ApiRequest(new WeatherData());
        apiRequest.setTimeOut(500);

        Thread.sleep(200);
        assertTrue(apiRequest.isValid());
    }

    @Test
    void isValidAfter() throws InterruptedException {


        apiRequest = new ApiRequest(new WeatherData());
        apiRequest.setTimeOut(500);

        Thread.sleep(1000);
        assertFalse(apiRequest.isValid());
    }
}