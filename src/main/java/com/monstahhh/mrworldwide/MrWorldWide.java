package com.monstahhh.mrworldwide;

import com.monstahhh.config.Config;
import com.monstahhh.mrworldwide.commands.CurrencyCommand;
import com.monstahhh.mrworldwide.commands.HelpCommand;
import com.monstahhh.mrworldwide.commands.TranslateCommand;
import com.monstahhh.mrworldwide.commands.weather.WeatherCommand;
import com.monstahhh.mrworldwide.database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class MrWorldWide {

    public static JDA jda;

    public static void main(String[] args) throws IOException, LoginException {
        new Database();
        Config config = new Config().read();

        JDABuilder builder = JDABuilder.createDefault(config.getToken());
        builder.setCompression(Compression.ZLIB);
        builder.setActivity(Activity.watching("the world"));
        builder.setAutoReconnect(true);
        builder.addEventListeners(new HelpCommand(), new TranslateCommand(), new WeatherCommand(), new CurrencyCommand());
        /*configureMemoryUsage(builder);*/

        jda = builder.build();


        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                new CommandData("help", "Receive the various commands of the bot"),
                new CommandData("weather", "Receive weather for a specific city or country")
                        .addOptions(
                                new OptionData(OptionType.STRING, "country", "The country in which the city is located").setRequired(false),
                                new OptionData(OptionType.STRING, "city", "The city which should be checked if specified").setRequired(false)
                        ),
                new CommandData("convert", "Converts an amount of money from one currency to the other")
                        .addOptions(
                                new OptionData(OptionType.INTEGER, "amount", "The amount of money in the original currency").setRequired(true),
                                new OptionData(OptionType.STRING, "originalcurrency", "The currency that the amount is originally in").setRequired(true),
                                new OptionData(OptionType.STRING, "newcurrency", "The currency that the amount should be in").setRequired(true)
                        ),
                new CommandData("translate", "Translate a sentence from one language to the other")
                        .addOptions(
                                new OptionData(OptionType.STRING, "sentence", "The sentence that should be translated").setRequired(true),
                                new OptionData(OptionType.STRING, "originallanguage", "The language that the sentence originally is in").setRequired(true),
                                new OptionData(OptionType.STRING, "destinationlanguage", "The language that the sentence must be translated to").setRequired(true)
                        )
        );

        commands.queue();
    }

    private static void configureMemoryUsage(JDABuilder builder) {
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);

        builder.setLargeThreshold(50);
    }
}
