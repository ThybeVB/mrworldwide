package com.monstahhh.mrworldwide.commands.weather;

import com.monstahhh.mrworldwide.weather.City;
import com.monstahhh.mrworldwide.weather.WeatherService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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
            .setFooter("Example: /config city", null);

    private final EmbedBuilder noPersonalLocError = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED)
            .addField("Location Error", "We could not find a location linked to your profile.", false)
            .addField("Notice", "If you want to set your own city, use the /setcity command.", false)
            .setFooter("Example: /config city", null);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null || !event.getName().equals("weather"))
            return;

        event.deferReply(false).queue();
        OptionMapping cityInput = event.getOption("city");
        OptionMapping countryInput = event.getOption("country");

        if (cityInput == null && countryInput == null) {
            this.personal(event);
            return;
        }

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

    private void cityOnly(String cityName, SlashCommandInteractionEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocationCity(cityName, e);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }

    private void countryOnly(String countryName, SlashCommandInteractionEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocationCountry(countryName, e);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }

    private void cityAndCountry(String cityName, String countryName, SlashCommandInteractionEvent e) {
        WeatherService service = new WeatherService();

        City city = service.getLocation(cityName, countryName, e);
        MessageEmbed embed = service.getEmbedFor(city);

        e.getHook().sendMessageEmbeds(embed).queue();
    }

    private void personal(SlashCommandInteractionEvent e) {
        WeatherService service = new WeatherService();
        City city = service.getPersonalLocation(e);

        if (city != null) {
            MessageEmbed embed = service.getEmbedFor(city);
            e.getHook().sendMessageEmbeds(embed).queue();
        } else {
            e.getHook().sendMessageEmbeds(noPersonalLocError.build()).queue();
        }
    }
}