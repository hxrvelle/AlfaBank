package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

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

            if (todayRub > yesterdayRub) {
                System.out.println("Больше");
            } else {
                System.out.println("Меньше");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    static double getRub(String date) throws IOException, ParseException {
        String appKey = "01e31f992c6745f4ab6120ca2764945a";
        String endpoint = "latest.json";
        if (date != null) {
            endpoint = "historical/" + date + ".json";
        }
        String spec = "https://openexchangerates.org/api/" + endpoint + "?app_id=" + appKey + "&symbols=RUB";
        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
        String rates = jsonObject.get("rates").toString();
        jsonObject = (JSONObject) jsonParser.parse(rates);
        double rub = (double) jsonObject.get("RUB");
        return rub;
    }
}