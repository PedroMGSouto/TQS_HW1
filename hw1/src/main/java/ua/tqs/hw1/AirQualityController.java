package ua.tqs.hw1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

@Controller
public class AirQualityController {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    static AirQualityCache<String, AirQuality> cache = new AirQualityCache<>(200,500);
    public final static Logger logger = LogManager.getLogger();


    @Autowired
    AirQualityService aqs;

    @GetMapping("/")
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("index");
        LogManager.writeToFile();
        logger.info("Get Mapping /");
        return setNA(mv);
    }

    @GetMapping("/search/{city}")
    public ModelAndView getCityInfo(@PathVariable("city") String city) {
        logger.info("Get Mapping /search/{city} with city="+city);
        ModelAndView mv = new ModelAndView("index");
        AirQuality aq;

        if(cache.get(city.toLowerCase())!=null){
            logger.info("AirQuality for city="+city+ " in cache");
            logger.info("Getting AirQuality data for city="+city+" from the cache");
            aq = cache.get(city.toLowerCase());
            logger.info(aq.toString());
        }
        else{
            logger.info("AirQuality for city="+city+ "not in cache");
            if(!aqs.getCityData(city)){
                logger.info("City="+city+ " does not exist or something went wrong");
                return setNA(mv);
            }
            logger.info("Getting AirQuality data for city="+city+" from the API (Service)");
            aq = aqs.getAq();
            logger.info(aq.toString());
            logger.info("Saving AirQuality data for city="+city+" in cache");
            cache.put(city.toLowerCase(),aq);
        }

        HashMap<Integer, String[]> forecast = new HashMap<>();
        int min = aq.getMin();
        logger.info("Getting min between forecasts, min="+min);
        String dt = sdf.format(aq.getDate().getTime());
        String week = getWeekDay(aq.getDate());

        logger.info("Adding attributes to the ModelAndView");
        mv.addObject("lat", aq.getLatLon()[0].toString());
        mv.addObject("lon", aq.getLatLon()[1].toString());
        mv.addObject("cityName", aq.getCityName());
        mv.addObject("date", dt);
        mv.addObject("week", doUpper(week));
        mv.addObject("aqi", aq.getAqi());

        for(int i = 1; i < min; i++){
            String[] a = new String[6];
            a[0] = aq.pm10.getJSONObject(i).get("avg").toString();
            a[1] = aq.o3.getJSONObject(i).get("avg").toString();
            a[2] = aq.pm25.getJSONObject(i).get("avg").toString();
            a[3] = aq.uvi.getJSONObject(i).get("avg").toString();

            Calendar c = aq.getDate();
            c.add(Calendar.DATE,i);
            dt = sdf.format(c.getTime());


            a[4] = dt;
            a[5] = getWeekDay(c);
            forecast.put(i,a);
        }

        mv.addObject("forecast", forecast);

        return mv;
    }



    private ModelAndView setNA(ModelAndView mv){
        mv.addObject("aqi", "Search a City");
        return mv;
    }

    private String doUpper(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String getWeekDay(Calendar c){
        int day = c.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case(1):
                return "Domingo";
            case(2):
                return "Segunda-feira";
            case(3):
                return "Terça-feira";
            case(4):
                return "Quarta-feira";
            case(5):
                return "Quinta-feira";
            case(6):
                return "Sexta-feira";
            case(7):
                return "Sábado";
            default:
                return "";
        }
    }

}