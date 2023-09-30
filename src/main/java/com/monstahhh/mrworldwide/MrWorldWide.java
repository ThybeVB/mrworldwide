package com.monstahhh.mrworldwide;

import com.monstahhh.config.Config;
import com.monstahhh.mrworldwide.commands.CurrencyCommand;
import com.monstahhh.mrworldwide.commands.HelpCommand;
import com.monstahhh.mrworldwide.commands.TranslateCommand;
import com.monstahhh.mrworldwide.commands.weather.ChangeClockCommand;
import com.monstahhh.mrworldwide.commands.weather.SetCityCommand;
import com.monstahhh.mrworldwide.commands.weather.WeatherCommand;
import com.monstahhh.mrworldwide.database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;

public class MrWorldWide {

    public static JDA jda;

    public static void main(String[] args) throws IOException {
        new Database();
        Config config = new Config().read();

        JDABuilder builder = JDABuilder.createDefault(config.getToken());
        builder.setCompression(Compression.ZLIB);
        builder.setActivity(Activity.watching("the world"));
        builder.setAutoReconnect(true);
        builder.addEventListeners(new HelpCommand(), new TranslateCommand(), new WeatherCommand(), new CurrencyCommand(), new ChangeClockCommand(), new SetCityCommand());
        configureIntents(builder);

        jda = builder.build();

        jda.updateCommands().addCommands(
                Commands.slash("help", "Receive the various commands of the bot"),
                Commands.slash("weather", "Receive weather for a specific city or country")
                        .addOptions(
                                new OptionData(OptionType.STRING, "country", "The country in which the city is located").setRequired(false),
                                new OptionData(OptionType.STRING, "city", "The city which should be checked if specified").setRequired(false)
                        ),
                Commands.slash("convert", "Converts an amount of money from one currency to the other")
                        .addOptions(
                                new OptionData(OptionType.INTEGER, "amount", "The amount of money in the original currency").setRequired(true),
                                new OptionData(OptionType.STRING, "originalcurrency", "The currency that the amount is originally in").setRequired(true),
                                new OptionData(OptionType.STRING, "newcurrency", "The currency that the amount should be in").setRequired(true)
                        ),
                Commands.slash("translate", "Translate a sentence from one language to the other")
                        .addOptions(
                                new OptionData(OptionType.STRING, "sentence", "The sentence that should be translated").setRequired(true),
                                new OptionData(OptionType.STRING, "originallanguage", "The language that the sentence originally is in").setRequired(true),
                                new OptionData(OptionType.STRING, "destinationlanguage", "The language that the sentence must be translated to").setRequired(true)
                        ),
                Commands.slash("config", "Customize settings for the weather module")
                        .addSubcommands(
                                new SubcommandData("changeclock", "Customize your time format (AM/PM - 00:00)"),
                                new SubcommandData("city", "Customize your default city")
                                        .addOptions(new OptionData(OptionType.STRING, "city", "The city you want to be bound to")),
                                new SubcommandData("country", "Customize your default country")
                                        .addOptions(new OptionData(OptionType.STRING, "city", "The country you want to be bound to"))
                        )
        ).queue();
    }

    private static void configureIntents(JDABuilder builder) {
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);

        builder.setLargeThreshold(50);
    }
}
