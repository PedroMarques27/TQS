package hw.weatherbit.homework;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationTest {
    Location noData;
    @BeforeEach
    void setUp(){
        noData = new Location();
    }

    @Test
    void getLatLng() {
        Assertions.assertThrows(NullPointerException.class,
                ()->{
                    noData.getLatLng();
                }
        );
    }

    @Test
    void getCity() {
        Assertions.assertThrows(NullPointerException.class,
                ()->{
                    noData.getCity();
                }
        );
    }
}