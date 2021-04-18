package tqs.lab5.ex2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarManagerService carService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCar() throws Exception {
        Car car = new Car("Toyota","Yaris");
        car.setCarId(12345L);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/createCar")
                .content(asJsonString(car))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.when(carService.getCarDetails(car.getCarId())).thenReturn(car);


        assertEquals(carService.getCarDetails(car.getCarId()),car);
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCarById() throws Exception {
        Car car = new Car("Toyota","Yaris");
        car.setCarId(12345L);

        given(carService.getCarDetails(car.getCarId())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/carId").contentType(MediaType.APPLICATION_JSON).param("id",car.getCarId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maker", is("Toyota")))
                .andExpect(jsonPath("$.model ", is("Yaris")));
    }



    @Test
    void getAllCars() {
    }
}