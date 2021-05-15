package ua.tqs.hw1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AirQualityService {
    AirQuality aq;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean getCityData(String cityChoice){

        try{
            aq = new AirQuality();
            var info = restTemplate.getForObject("https://api.waqi.info/feed/"+cityChoice+"/?token=944dee06c3183ed59d45d84acd7532a04c41ec53", Map.class);
            JSONObject info_json = new JSONObject(info);
            JSONObject data = info_json.getJSONObject("data");
            JSONObject time = data.getJSONObject("time");
            JSONObject city = data.getJSONObject("city");
            JSONArray geo = city.getJSONArray("geo");
            JSONObject forecast = data.getJSONObject("forecast");
            JSONObject daily = forecast.getJSONObject("daily");
            JSONArray o3 = daily.getJSONArray("o3");
            JSONArray pm10 = daily.getJSONArray("pm10");
            JSONArray pm25 = daily.getJSONArray("pm25");
            JSONArray uvi = daily.getJSONArray("uvi");


            String[] latlon = geo.toString().split(",");
            aq.setLatLon(latlon[0].substring(1),latlon[1].substring(0,latlon[1].length()-1));
            aq.setO3(o3);
            aq.setPm10(pm10);
            aq.setPm25(pm25);
            aq.setUvi(uvi);
            aq.setCityName(cityChoice);
            aq.setDate(time.get("s").toString());
            aq.setAqi(data.get("aqi").toString());
            return true;
        }
        catch (Exception e){
            System.out.println("https://api.waqi.info/feed/"+cityChoice+"/?token=944dee06c3183ed59d45d84acd7532a04c41ec53");
            System.out.println(e);
            return false;
        }
    }

    public AirQuality getAq() {
        return aq;
    }
}
