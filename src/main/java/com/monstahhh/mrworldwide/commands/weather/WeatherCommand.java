package com.monstahhh.mrworldwide.commands.weather;

import net.dv8tion.jda.api.EmbedBuilder;
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
            this.cityAndCountry();
        } else if (cityInput == null && countryInput != null) {
            this.countryOnly();
        } else if (cityInput != null) {
            cityOnly();
        } else {
            event.getHook().sendMessageEmbeds(invalidLocError.build()).queue();
        }
    }

    private void cityOnly() {
        //TODO
    }

    private void countryOnly() {
        //TODO
    }

    private void cityAndCountry() {
        //TODO
    }
}