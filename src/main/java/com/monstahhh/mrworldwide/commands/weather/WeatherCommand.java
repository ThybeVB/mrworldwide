package com.monstahhh.mrworldwide.commands.weather;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.LocaleUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class WeatherCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        //TODO
        System.out.println(toISO2("english"));
    }

    public String toISO2(String name) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (name.equals(locale.getDisplayLanguage())) {
                return locale.getLanguage();
            }
        }
        throw new IllegalArgumentException("No language found: " + name);
    }
}
