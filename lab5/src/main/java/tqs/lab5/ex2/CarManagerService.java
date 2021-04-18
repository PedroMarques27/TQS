package tqs.lab5.ex2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarManagerService {
    @Autowired
    private CarRepository carRepository;

    public Car save(Car car) {
        return carRepository.save(car);
    }
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }
    public Car getCarDetails(long id){
        return carRepository.findByCarId(id);
    }
}
