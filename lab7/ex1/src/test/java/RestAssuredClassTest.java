import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class RestAssuredClassTest {
    private RestAssuredClass rest;
    @BeforeEach
    void setUp() {
        rest = new RestAssuredClass();
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void testGetResponseBody() {
        assertTrue(RestAssuredClass.getResponseBody(4).contains("et porro tempora"));
    }

    @Test
    void getResponseStatus() {
        assertEquals(200, RestAssuredClass.getResponseStatus());
    }

    @Test
    void getSpecificPartOfResponseBody() {
        assertTrue(RestAssuredClass.getSpecificPartOfResponseBody().contains(198));
        assertTrue(RestAssuredClass.getSpecificPartOfResponseBody().contains(199));
    }
}