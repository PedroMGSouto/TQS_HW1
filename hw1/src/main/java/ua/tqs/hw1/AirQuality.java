package ua.tqs.hw1;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class AirQuality {
    String cityName;
    String date;
    String aqi;

     Float[] LatLon;
     JSONArray o3;
     JSONArray pm10;
     JSONArray pm25;
     JSONArray uvi;

    public AirQuality() {
        LatLon = new Float[2];
        o3 = new JSONArray();
        pm10 = new JSONArray();
        pm25 = new JSONArray();
        uvi = new JSONArray();
    }

    public Float[] getLatLon() {
        return LatLon;
    }

    public void setLatLon(String Lat, String Lon) {
        try{
            this.LatLon[0]=Float.parseFloat(Lat);
            this.LatLon[1]=Float.parseFloat(Lon);
        }
        catch(Exception e){
            System.out.println("Something went wrong with the lat and lon");
            this.LatLon[0]=0.0f;
            this.LatLon[1]=0.0f;
        }

    }

    public JSONArray getO3() {
        return o3;
    }

    public void setO3(JSONArray o3) {
        this.o3 = o3;
    }

    public JSONArray getPm10() {
        return pm10;
    }

    public void setPm10(JSONArray pm10) {
        this.pm10 = pm10;
    }

    public JSONArray getPm25() {
        return pm25;
    }

    public void setPm25(JSONArray pm25) {
        this.pm25 = pm25;
    }

    public JSONArray getUvi() {
        return uvi;
    }

    public void setUvi(JSONArray uvi) {
        this.uvi = uvi;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Calendar getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            return c;
        } catch (ParseException e) {
            return c;
        }
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMin(){
        return Math.min(Math.min(o3.length(),pm10.length()),Math.min(pm25.length(),uvi.length()));
    }

    @Override
    public String toString() {
        return "AirQuality{" +
                "cityName='" + cityName + '\'' +
                ", date='" + date + '\'' +
                ", aqi='" + aqi + '\'' +
                ", LatLon=" + Arrays.toString(LatLon) +
                ", o3=" + o3 +
                ", pm10=" + pm10 +
                ", pm25=" + pm25 +
                ", uvi=" + uvi +
                '}';
    }
}
