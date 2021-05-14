package ua.tqs.hw1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class AirQualityRestController {
    private HashMap<String,String> bad;
    private HashMap<String,Integer> stats;
    private HashMap<String, Object> results;

    @Autowired
    AirQualityService aqs;

    @GetMapping("/search/{city}")
    public ResponseEntity<Object> getCityInfo(@PathVariable("city") String city) {
        AirQuality aq;

        if(AirQualityController.cache.get(city.toString().toLowerCase())!=null){
            aq = AirQualityController.cache.get(city.toString().toLowerCase());
        }
        else{
            if(!aqs.getCityData(city)){
                bad = new HashMap<>();
                bad.put("response","invalidCity");
                return new ResponseEntity<>(bad, HttpStatus.NOT_FOUND);
            }
            aq = aqs.getAq();
            AirQualityController.cache.put(city.toString().toLowerCase(),aq);
        }

        results = new HashMap<>();
        results.put("cityName", aq.getCityName());
        results.put("date", aq.getDate());
        results.put("aqi", aq.getAqi());
        results.put("LatLon", aq.getLatLon());
        results.put("o3", aq.getO3().toString());
        results.put("pm10", aq.getPm10().toString());
        results.put("pm25", aq.getPm25().toString());
        results.put("uvi", aq.getUvi().toString());

        return new ResponseEntity<>(results,HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistics(){
        stats = new HashMap<>();
        stats.put("Requests",AirQualityController.cache.getRequests());
        stats.put("Hits",AirQualityController.cache.getHits());
        stats.put("Misses",AirQualityController.cache.getMisses());

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
