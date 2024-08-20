import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {
        JSONObject locationData = getLocationData(locationName);
        if (locationData == null) {
            System.out.println("Error: Could not get location data.");
            return null;
        }

        double latitude = Double.parseDouble((String) locationData.get("lat"));
        double longitude = Double.parseDouble((String) locationData.get("lon"));

        String urlString = "https://api.open-meteo.com/v1/forecast?"
                + "latitude=" + latitude
                + "&longitude=" + longitude
                + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Europe%2FLondon";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            if (hourly == null) {
                System.out.println("Error: 'hourly' data is missing from the API response.");
                return null;
            }

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weathercode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = weathercode != null ? convertWeatherCode((long) weathercode.get(index)) : "Unknown";

            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (relativeHumidity != null) ? (long) relativeHumidity.get(index) : -1;

            JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (windspeedData != null) ? (double) windspeedData.get(index) : -1;

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity != -1 ? humidity : "Unknown");
            weatherData.put("windspeed", windspeed != -1 ? windspeed : "Unknown");

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    public static JSONObject getLocationData(String locationName) {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + locationName + "&format=json&limit=1";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to geocoding API");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONArray resultsJsonArray = (JSONArray) parser.parse(resultJson.toString());
                if (!resultsJsonArray.isEmpty()) {
                    return (JSONObject) resultsJsonArray.get(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject weatherData = getWeatherData("Tashkent");
        if (weatherData != null) {
            System.out.println(weatherData.toJSONString());
        }
    }

    private static int findIndexOfCurrentTime(JSONArray timelist){
        String currentTime = getCurrentTime();

        for(int i = 0; i<timelist.size(); i++){
            String time = (String) timelist.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;

    }

    private static String convertWeatherCode(long weathercode){
        String weatherCondition = "";
        if(weathercode == 0L){
            weatherCondition = "Clear";
        }else if(weathercode <= 3L && weathercode > 0L){
            weatherCondition = "Cloudy";
        }else if((weathercode >= 51L && weathercode <= 67L)
                    || (weathercode >= 80L && weathercode <= 99L)){
            weatherCondition = "Rain";
        }else if (weathercode >= 71L && weathercode <= 77L){
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
