package org.example;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        WeatherService weatherService = new WeatherService(client);
        weatherService.getForecast(55.75204, 37.61781, 7);
    }
}