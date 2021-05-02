package tqs.lab5.ex2;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CarRepository  extends JpaRepository<Car, Long> {
    public Car findByCarId(Long id);

    public List<Car> findAll();
}