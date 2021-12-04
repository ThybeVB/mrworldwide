package com.monstahhh.mrworldwide.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends ListenerAdapter {

    public HelpCommand(JDA jda) {
        jda.upsertCommand("help", "Receive the various commands of the bot").queue();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.getName().equals("help")) return;

        event.reply("aaa").queue();
    }
}
