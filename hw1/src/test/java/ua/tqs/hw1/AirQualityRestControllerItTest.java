package ua.tqs.hw1;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirQualityRestControllerItTest {

    @LocalServerPort
    int port;

    @DisplayName("City exists then return aq forecast")
    @Test
    void whenCityExists(){
        RestAssured.given().port(port).get("/api/search/oPorto")
                .then()
                .statusCode(200)
                .and().body("cityName", equalTo("oPorto"));
    }

    @DisplayName("City doesnt exist")
    @Test
    void whenCityDoesntExist(){
        RestAssured.given().port(port).get("/api/search/Aveiro")
                .then()
                .statusCode(404)
                .and().body("response",equalTo("invalidCity"));
    }

    @DisplayName("Statistics")
    @Test
    void getStatistics(){
        RestAssured.given().port(port).get("/api/stats")
                .then()
                .statusCode(200)
                .and().body("Misses", equalTo(0))
                .and().body("Hits", equalTo(0))
                .and().body("Requests", equalTo(0));
    }

}