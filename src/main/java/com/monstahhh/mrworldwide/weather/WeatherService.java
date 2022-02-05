package com.monstahhh.mrworldwide.weather;

import com.monstahhh.config.Config;
import com.monstahhh.http.HttpClient;
import com.monstahhh.http.HttpMethod;
import com.monstahhh.http.HttpResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.io.IOException;

public class WeatherService {

    private final EmbedBuilder errorEmbed = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED);

    public City getLocation(String cityName) {
        return null;
    }

    public City getLocation(String cityName, String countryName) {

        if (cityName == null) {

        } else {

        }

        return null;
    }

    private String callLocation() throws IOException {
        HttpClient client = new HttpClient();
        Config conf = new Config().read();

        String formattedSend = String.format("?q=%s&appid=%s&units=metric", ""/*TODO*/, conf.getWeatherToken());
        HttpResponse result = client.request(HttpMethod.GET, ("http://api.openweathermap.org/data/2.5/weather" + formattedSend));

        String resultStr = result.asString();

        return resultStr;
    }

    public MessageEmbed getEmbedFor(City city) {
        try {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(city.embedColor);
            eb.setThumbnail(city.iconUrl);

            if (city.temperature >= 40) {
                eb.setTitle("Weather for " + city.cityName + ", " /*+getCountryName(city.countryCode) + TODO  " <:40DEGREESFUCK:617781121236860963>"*/);
            } else {
                eb.setTitle("Weather for " + city.cityName + ", "  /*+getCountryName(city.countryCode) TODO */);
            }
            eb.addField("Temperature", city.temperature + "°C", false);

            eb.addField("Horizon Events", "Sunrise: " + city.sunRiseTime + " | Sunset: " + city.sunSetTime, false);

            if (city.currentTime.charAt(0) == '0' && (city.currentTime.contains("AM") || city.currentTime.contains("PM"))) {
                eb.addField("Current Time", city.currentTime.substring(1), false);
            } else {
                eb.addField("Current Time", city.currentTime, false);
            }

            eb.addField(city.currentWeatherTitle, city.currentWeatherDescription + "at " + city.windSpeed + "km/h with " + city.humidity + "% humidity", false);

            String responsePrefix = "Made by Pitbull, ";
            String footer;
            if (city.timeOfCalculation[0].equals("0") && city.timeOfCalculation[1].equals("0")) {
                footer = responsePrefix + "Recorded just now";
            } else if (city.timeOfCalculation[0].equals("0")) {
                footer = responsePrefix + "Recorded " + city.timeOfCalculation[1] + " seconds ago";
            } else {
                if (city.timeOfCalculation[1].equalsIgnoreCase("0")) {
                    footer = responsePrefix + "Recorded " + city.timeOfCalculation[0] + " minute ago";
                } else if (city.timeOfCalculation[1].equalsIgnoreCase("1")) {
                    footer = responsePrefix + "Recorded " + city.timeOfCalculation[0] + " minutes and " + city.timeOfCalculation[1] + " second ago";
                } else {
                    footer = responsePrefix + "Recorded " + city.timeOfCalculation[0] + " minutes and " + city.timeOfCalculation[1] + " seconds ago";
                }
            }
            eb.setFooter(footer + ". FT: " + city.feelTemperature + "°C", null);

            return eb.build();
        } catch (Exception e) {
            if (e instanceof NullPointerException || e.getMessage() == null) {
                return null;
            }

            EmbedBuilder failEmbed = errorEmbed;
            failEmbed.addField("City Error", "Embed Creation failed: " + e.getCause().toString(), false);

            return failEmbed.build();
        }
    }

}
