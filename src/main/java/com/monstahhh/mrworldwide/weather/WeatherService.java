package com.monstahhh.mrworldwide.weather;

import com.monstahhh.config.Config;
import com.monstahhh.http.HttpClient;
import com.monstahhh.http.HttpMethod;
import com.monstahhh.http.HttpResponse;
import com.monstahhh.mrworldwide.commands.weather.ChangeClockCommand;
import com.monstahhh.mrworldwide.database.Profile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;

public class WeatherService {

    private final EmbedBuilder errorEmbed = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED);

    public City getLocation(String cityName, String countryName, SlashCommandEvent e) {
        Profile profile = new Profile(e.getUser().getIdLong());
        ChangeClockCommand.Time time = profile.getTimeSetting();

        String resultJson = this.callLocation(cityName, countryName);

        return new City().getCityObjectForJson(resultJson, time);
    }

    public City getLocationCity(String cityName, SlashCommandEvent e) {
        Profile profile = new Profile(e.getUser().getIdLong());
        ChangeClockCommand.Time time = profile.getTimeSetting();

        String resultJson = this.callLocation(cityName);

        return new City().getCityObjectForJson(resultJson, time);
    }

    public City getPersonalLocation(SlashCommandEvent e) {
        Profile profile = new Profile(e.getUser().getIdLong());
        ChangeClockCommand.Time time = profile.getTimeSetting();

        String cityName = profile.getCity();
        if (cityName != null) {
            String resultJson = this.callLocation(cityName);
            return new City().getCityObjectForJson(resultJson, time);
        }

       return null;
    }

    public City getLocationCountry(String countryName, SlashCommandEvent e) {
        JSONObject country = this.getCountryInformation(countryName);
        if (country != null) {
            String capital = country.getJSONArray("capital").getString(0);
            String countryCode = country.getString("cca2");

            Profile profile = new Profile(e.getUser().getIdLong());
            ChangeClockCommand.Time time = profile.getTimeSetting();

            String loc = this.callLocation(capital, countryCode);

            return new City().getCityObjectForJson(loc, time);
        } else {
            return null;
        }
    }


    private String callLocation(String cityName, String countryCode) {
        try {
            HttpClient client = new HttpClient();
            Config conf = new Config().read();

            String formattedSend = String.format("?q=%s,%s&appid=%s&units=metric", cityName, countryCode, conf.getWeatherToken());
            HttpResponse result = client.request(HttpMethod.GET, ("http://api.openweathermap.org/data/2.5/weather" + formattedSend));

            return result.asString();
        } catch (IOException e) {
            return null;
        }
    }

    public String callLocation(String cityName) {
        try {
            HttpClient client = new HttpClient();
            Config conf = new Config().read();

            String formattedSend = String.format("?q=%s&appid=%s&units=metric", cityName, conf.getWeatherToken());
            HttpResponse result = client.request(HttpMethod.GET, ("http://api.openweathermap.org/data/2.5/weather" + formattedSend));

            return result.asString();
        } catch (IOException e) {
            return null;
        }
    }

    public MessageEmbed getEmbedFor(City city) {
        try {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(city.embedColor);
            eb.setThumbnail(city.iconUrl);

            if (city.temperature >= 40) {
                eb.setTitle("Weather for " + city.cityName + ", " +getCountryName(city.countryCode) +  " <:40DEGREESFUCK:617781121236860963>");
            } else {
                eb.setTitle("Weather for " + city.cityName + ", "  +getCountryName(city.countryCode));
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

    private String getCountryName(String countryCode) {
        try {
            Locale l = new Locale("", countryCode.toUpperCase());
            return l.getDisplayCountry(Locale.ENGLISH);
        } catch (Exception exception) {
            return null;
        }
    }

    private JSONObject getCountryInformation(String countryName) {
        try {
            String formattedSend = String.format("https://restcountries.com/v3.1/name/%s", countryName);
            HttpResponse result = new HttpClient().request(HttpMethod.GET, formattedSend);

            String resultStr = result.asString();
            JSONArray jsonArr = new JSONArray(resultStr);

            return jsonArr.getJSONObject(0);
        } catch (IOException e) {
            return null;
        }
    }
}
