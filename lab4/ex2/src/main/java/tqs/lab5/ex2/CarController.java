package tqs.lab5.ex2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class CarController {
    @Autowired
    private CarManagerService carManagerService;


    @PostMapping("/createCar")
    public ResponseEntity<Car> createCar(@RequestBody Car car){
        HttpStatus status = HttpStatus.CREATED;
        Car saved = carManagerService.save(car);

        return new ResponseEntity<>(saved, status);
    }

    @GetMapping("/carId")
    public Car getCarById(@RequestParam(name = "id") Long id){
        return carManagerService.getCarDetails(id);
    }

    @GetMapping("/cars")
    public List<Car> getAllCars(){
        return carManagerService.getAllCars();
    }
}
