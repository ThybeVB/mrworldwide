package com.monstahhh.mrworldwide.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TranslateCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("translate"))
            return;

        String sentenceToBeTranslated = event.getOption("sentence").getAsString();
        String language = event.getOption("originallanguage").getAsString();
        String destinationLanguage = event.getOption("destinationlanguage").getAsString();
    }
}
