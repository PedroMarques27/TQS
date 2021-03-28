import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AddressResolverIT {
    AddressResolver addressResolver;

    @BeforeEach
    void setUp(){
        addressResolver = new AddressResolver();
    }

    @Test
    void findAddressForLocation() throws IOException, URISyntaxException, ParseException {
        Address address = addressResolver.findAddressForLocation(40.6318,-8.658);
        assertEquals(address.city,"Glória e Vera Cruz");
        assertEquals(address.road,"Parque Estacionamento da Reitoria - Univerisdade de Aveiro");
        assertEquals(address.houseNumber,"");
        assertEquals(address.zip,"3810-193");
        assertEquals(address.state,"Centro");

    }


    @DisplayName("VALID COORDINATES W/O Mockito")
    @Test
    public void validInput() {
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                addressResolver.findAddressForLocation(500, 10);
            }
        });
    }
}
