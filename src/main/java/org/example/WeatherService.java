package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    HttpClient client;

    public WeatherService(HttpClient client) {
        this.client = client;
    }

    public String getForecast(double lat, double lon, int limit) throws Exception {
        String uri = "https://api.weather.yandex.ru/v2/forecast?lat=" + lat + "&lon=" + lon + "&limit=" + limit;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("X-Yandex-Weather-Key", Environment.getVariable("YANDEX_WEATHER_KEY"))
                .GET()
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode >= 200 & statusCode < 300) {
            JsonObject forecast = new Gson().fromJson(responseBody, JsonObject.class);
            JsonObject weatherInfo = forecast.get("fact").getAsJsonObject();
            JsonArray forecasts = forecast.get("forecasts").getAsJsonArray();
            int temperature = weatherInfo.get("temp").getAsInt();
            int forecastTemperaturesSum = 0;

            for (JsonElement item : forecasts) {
                int forecastAverageDayTemperature = ((JsonObject) item)
                        .get("parts").getAsJsonObject()
                        .get("day").getAsJsonObject()
                        .get("temp_avg").getAsInt();

                forecastTemperaturesSum += forecastAverageDayTemperature;
            }

            int forecastAverageDayTemperature = forecastTemperaturesSum / limit;

            System.out.println("Ответ сервера: " + responseBody);
            System.out.println("Температура в градусах цельсия: " + temperature);
            System.out.println("Средняя температура за " + limit + " дней в градусах цельсия: " + forecastAverageDayTemperature);

            return responseBody;
        }

        throw new RuntimeException("HTTP " + statusCode + " — " + (responseBody != null ? responseBody : "<empty>"));
    }
}
