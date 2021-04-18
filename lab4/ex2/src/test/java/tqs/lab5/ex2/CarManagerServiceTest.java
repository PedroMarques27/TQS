package tqs.lab5.ex2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarManagerServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carService;

    private Car car1, car2;

    @BeforeEach
    void setUp() {
        car1 = new Car("Yaris", "Toyota");
        car1.setCarId(1L);
        car2 = new Car("model1", "Maker1");
        car2.setCarId(2L);

        Mockito.when(carRepository.findByCarId(car1.getCarId())).thenReturn(car1);
        Mockito.when(carRepository.findByCarId(car1.getCarId())).thenReturn(car1);
        Mockito.when(carRepository.findByCarId(10000L)).thenReturn(null);
    }




    @Test
    void getCarDetails() {

        Car c = carService.getCarDetails(1L);
        assertEquals(c,car1);

        c = carService.getCarDetails(10000L);
        assertEquals(c,null);
    }
}


