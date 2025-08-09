package app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CarServiceTest {

    @Autowired
    CarService carService;

    @Test
    void cenario01(){

        boolean response = this.carService.checkCarName("Jeep", 2006);
        assertEquals(true, response);
    }

    @Test
    void cenario02(){
        assertThrows(Exception.class, () -> {
            boolean response = this.carService.checkCarName("Jeep Compass", 2005);
        });
    }
}
