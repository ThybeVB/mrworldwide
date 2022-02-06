package com.monstahhh.mrworldwide.commands.weather;

import com.monstahhh.mrworldwide.weather.City;
import com.monstahhh.mrworldwide.weather.WeatherService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class WeatherCommand extends ListenerAdapter {

    private final EmbedBuilder errorEmbed = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED);

    private final EmbedBuilder invalidLocError = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED)
            .addField("Argument Error", "It seems that you did not enter a valid location", false)
            .addField("Notice", "If you want to set your own city, use the setcity command.", false)
            .setFooter("Example: /weather city:Brussels country:Belgium", null);

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("weather"))
            return;

        event.deferReply(false).queue();
        OptionMapping cityInput = event.getOption("city");
        OptionMapping countryInput = event.getOption("country");

        if (cityInput != null && countryInput != null) {
            this.cityAndCountry(cityInput.getAsString(), countryInput.getAsString(), event);
        } else if (cityInput == null && countryInput != null) {
            this.countryOnly(countryInput.getAsString(), event);
        } else if (cityInput != null) {
            cityOnly(cityInput.getAsString(), event);
        } else {
            event.getHook().sendMessageEmbeds(invalidLocError.build()).queue();
        }
    }

    private void cityOnly(String cityName, SlashCommandEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocationCity(cityName);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }

    private void countryOnly(String countryName, SlashCommandEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocationCountry(countryName);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }

    private void cityAndCountry(String cityName, String countryName, SlashCommandEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocation(cityName, countryName);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }
}