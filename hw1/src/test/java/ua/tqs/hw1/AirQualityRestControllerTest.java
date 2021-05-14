package ua.tqs.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(AirQualityRestController.class)
class AirQualityRestControllerTest {
    AirQuality aq;

    @Autowired
    private MockMvc mvc;

    @MockBean
    AirQualityService aqs;

    @BeforeEach
    void setup(){
        aq = new AirQuality();
        aq.setCityName("oPorto");
        aq.setDate("2021-03-12");
    }


    @DisplayName("City not in cache")
    @Test
    void getCityData() throws Exception {
        when(aqs.getCityData(Mockito.anyString())).thenReturn(true);
        when(aqs.getAq()).thenReturn(aq);

        mvc.perform(get("/api/search/oporto"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("cityName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("date")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("aqi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("LatLon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("o3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("pm10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("pm25")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("uvi")));

    }

    @DisplayName("City in cache")
    @Test
    void getCityDataCache() throws Exception {
        when(aqs.getCityData(Mockito.anyString())).thenReturn(true);
        when(aqs.getAq()).thenReturn(aq);
        mvc.perform(get("/api/search/oPorto")); //1 request to save in cache


        mvc.perform(get("/api/search/oPorto")) //2 will get the data from cache
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("cityName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("date")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("aqi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("LatLon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("o3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("pm10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("pm25")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("uvi")));
    }

    @DisplayName("City does not exist")
    @Test
    void noCity() throws Exception {

        mvc.perform(get("/api/search/Aveiro"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"response\":\"invalidCity\"}"));
    }

    @DisplayName("Get /api/stats")
    @Test
    void getStatistics() throws Exception {
        mvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("Requests")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("Misses")))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasKey("Hits")));

    }
}