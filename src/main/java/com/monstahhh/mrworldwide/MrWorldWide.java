package com.monstahhh.mrworldwide;

import com.monstahhh.config.Config;
import com.monstahhh.mrworldwide.commands.HelpCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class MrWorldWide {

    private static JDA jda;
    public static long OwnerId = 257247527630274561L;

    public static void main(String[] args) throws IOException, LoginException {
        Config config = new Config().read();

        JDABuilder builder = JDABuilder.createDefault(config.getToken());
        builder.setCompression(Compression.ZLIB);
        builder.setActivity(Activity.watching("the world"));
        builder.setContextEnabled(false);
        builder.setAutoReconnect(true);
        /*builder.addEventListeners();*/
        /*configureMemoryUsage(builder);*/

        jda = builder.build();

        new HelpCommand(jda);
    }

    private static void configureMemoryUsage(JDABuilder builder) {
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS);

        builder.setLargeThreshold(50);
    }
}
