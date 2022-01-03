package com.monstahhh.mrworldwide.commands.weather;

import com.monstahhh.mrworldwide.database.Profile;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChangeClock extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("changeclock"))
            return;

        //long userId = event.getAuthor().getIdLong();

        //Time currentTime = getUserTimeSetting(userId);
        //Time newTime = getNewTime(currentTime);

        //setUserTimeSetting(userId, newTime);
        //event.getChannel().sendMessage("Time has been changed from `" + currentTime + "` to `" + newTime + "` for " + event.getAuthor().getAsMention()).queue();
        //TODO

    }

    private void setUserTimeSetting(long userId, Time time) {
        //TODO
    }

    private Time getUserTimeSetting(long userId) {
        Profile profile = new Profile(userId);
        return profile.getTimeSetting();
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
