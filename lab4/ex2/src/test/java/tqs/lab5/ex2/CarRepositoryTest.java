package tqs.lab5.ex2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    private Car car1, car2;

    @BeforeEach
    void setUp() {
        car1 = new Car("Yaris", "Toyota");
        car1.setCarId(1L);
        car2 = new Car("model1", "Maker1");
        car2.setCarId(2L);

        carRepository.save(car1);
        carRepository.save(car2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByCarId() {
        Car available = carRepository.findByCarId(2L);
        assertEquals(available,car2);
    }

    @Test
    void findAll() {
        List<Car> available = carRepository.findAll();
        assertEquals(available.size(),2);
    }


}