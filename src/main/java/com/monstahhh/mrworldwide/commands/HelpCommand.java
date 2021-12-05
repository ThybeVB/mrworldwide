package com.monstahhh.mrworldwide.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends ListenerAdapter {

    private final String helpMsg = "```----- Mr. Worldwide Commands -----" +
            "\n<> = Required Field" +
            "\n> weather <cityname,countrycode>" +
            "\n> weather <countryname>" +
            "\n> weather *(If 'setcity' has been used)*" +
            "\n> weather <@Guaka25#4852>" +
            "\n> setcity <cityname,countrycode>" +
            "\n> changeclock" +
            "\n> translate <originLanguage> <newLanguage> <message>" +
            "\n> trs <originLanguage> <newLanguage> <message>" +
            "\n> convert <amount> <originCurrency> <newCurrency>" +
            "\n> invite" +
            "\n----------------------------------```" +
            "\nSupport Server: https://discord.gg/CrZ7FZ7";

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("help"))
            return;

        event.reply(helpMsg).setEphemeral(false).queue();
    }
}
