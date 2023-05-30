package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            String monthStr = String.valueOf(month);
            if (month < 10) {
                monthStr = "0" + monthStr;
            }
            int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            String yesterday = calendar.get(Calendar.YEAR) + "-" + monthStr + "-" + day;
            double todayRub = getRub(null);
            double yesterdayRub = getRub(yesterday);

            System.out.println("Курс на сегодня: " + todayRub);
            System.out.println("Курс на вчера: " + yesterdayRub);
            String compare;
            if (todayRub > yesterdayRub) {
                compare = "More";
                System.out.println(getImg(compare));
            } else {
                compare = "Less";
                System.out.println(getImg(compare));
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    static double getRub(String date) throws IOException, ParseException {
        String appKey = "01e31f992c6745f4ab6120ca2764945a";
        String endpoint = "latest.json";
        if (date != null) {
            endpoint = "historical/" + date + ".json";
        }
        String spec = "https://openexchangerates.org/api/" + endpoint + "?app_id=" + appKey + "&symbols=RUB";
        StringBuilder result = getResult(spec);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
        String rates = jsonObject.get("rates").toString();
        jsonObject = (JSONObject) jsonParser.parse(rates);
        double rub = (double) jsonObject.get("RUB");
        return rub;
    }

    static String getImg(String compare) throws IOException, ParseException {
        String apiKey = "prOAefWSeU2ktKrE8TL4W8QkUFGWKDZ5";
        String endpoint = "broke";
        if (compare.equals("More")) {
            endpoint = "rich";
        }
        String spec = "https://api.giphy.com/v1/gifs/search?api_key=" + apiKey + "&q=" + endpoint;
        StringBuilder result = getResult(spec);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
        JSONArray jsonArray = (JSONArray) jsonObject.get("data");
        Random random = new Random();
        int i = random.nextInt(0, jsonArray.size());
        JSONObject data = (JSONObject) jsonArray.get(i);
        String img = data.get("url").toString();
        return img;
    }

    static StringBuilder getResult (String spec) throws IOException {
        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }
}