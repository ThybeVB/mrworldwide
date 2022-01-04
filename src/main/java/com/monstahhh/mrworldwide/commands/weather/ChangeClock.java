package com.monstahhh.mrworldwide.commands.weather;

import com.monstahhh.mrworldwide.database.Profile;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChangeClock extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !Objects.equals(event.getSubcommandName(), "changeclock"))
            return;

        event.deferReply(true).queue();
        long userId = event.getUser().getIdLong();

        Time currentTime = this.getUserTimeSetting(userId);
        Time newTime = this.getNewTime(currentTime);

        this.setUserTimeSetting(userId, newTime);

        String response = String.format("Time has been changed from `%s` to `%s` for %s", currentTime, newTime, event.getUser().getAsMention());
        event.getHook().sendMessage(response).queue();
    }

    private void setUserTimeSetting(long userId, Time time) {
        Profile profile = new Profile(userId);
        profile.setTimeSetting(time);
    }

    private Time getUserTimeSetting(long userId) {
        Profile profile = new Profile(userId);
        Time time = profile.getTimeSetting();
        if (time != null)
            return time;

        return Time.TWENTYFOURHOUR;
    }

    private Time getNewTime(Time currentTime) {

        return switch (currentTime) {
            case TWELVEHOUR -> Time.TWENTYFOURHOUR;
            case TWENTYFOURHOUR -> Time.TWELVEHOUR;
        };
    }

    public enum Time {
        TWENTYFOURHOUR,
        TWELVEHOUR
    }

}
