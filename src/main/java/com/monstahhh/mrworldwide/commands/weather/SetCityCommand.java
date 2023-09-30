package com.monstahhh.mrworldwide.commands.weather;

import com.monstahhh.mrworldwide.database.Profile;
import com.monstahhh.mrworldwide.weather.WeatherService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetCityCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null || !Objects.equals(event.getSubcommandName(), "city"))
            return;

        event.deferReply(true).queue();
        String cityInput = event.getOption("city").getAsString();
        if (!cityInput.isEmpty()) {
            WeatherService service = new WeatherService();
            if (service.callLocation(cityInput) != null) {
                Profile profile = new Profile(event.getUser().getIdLong());
                profile.setCity(cityInput.trim().toLowerCase());

                event.getHook().sendMessage("Personal city has been set to `" + cityInput + "`").queue();
            } else {
                event.getHook().sendMessage("City could not be found.").queue();
            }
        } else {
            event.getHook().sendMessage("Please enter a city to use as your preferred location").queue();
        }
    }

}
