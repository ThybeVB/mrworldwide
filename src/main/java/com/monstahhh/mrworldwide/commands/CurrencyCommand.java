package com.monstahhh.mrworldwide.commands;

import com.monstahhh.config.Config;
import com.monstahhh.http.HttpClient;
import com.monstahhh.http.HttpMethod;
import com.monstahhh.http.HttpResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;

public class CurrencyCommand extends ListenerAdapter {

    private final EmbedBuilder defaultError = new EmbedBuilder()
            .setTitle("Mr. Error")
            .setColor(Color.RED);

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("convert"))
            return;

        event.deferReply(false).queue();
        InteractionHook hook = event.getHook();

        try {
            double amount = event.getOption("amount").getAsDouble();
            String currentCurrency = event.getOption("originalcurrency").getAsString();
            String destinationCurrency = event.getOption("newcurrency").getAsString();

            if (currentCurrency.equalsIgnoreCase(destinationCurrency)) {
                EmbedBuilder eb = defaultError;
                eb.addField("Converter Error", "You can't enter the same currency twice!", false);
                eb.addField("Example", "convert 1.25 eur usd", false);
                hook.sendMessageEmbeds(eb.build()).setEphemeral(false).queue();
                return;
            }

            Config config = new Config().read();
            float[] prices = getValueFor(currentCurrency, destinationCurrency, amount, config.getCurrencyToken());
            if (prices != null) {
                if (prices[0] == 503 && prices[1] == 0) {
                    EmbedBuilder eb = defaultError;
                    eb.addField("Server Error", "503: Currency Server was unable to be reached.", false);

                    hook.sendMessageEmbeds(eb.build()).setEphemeral(false).queue();
                } else {
                    MessageEmbed embed = getCurrencyEmbed(prices[0], prices[1], currentCurrency, destinationCurrency);
                    hook.sendMessageEmbeds(embed).setEphemeral(false).queue();
                }
            } else {
                EmbedBuilder eb = defaultError;
                eb.addField("Currency Error", "It seems that one or both of the currencies are wrong or don't exist.", false);
                eb.addField("Example", "convert 1.25 eur usd", false);
                hook.sendMessageEmbeds(eb.build()).setEphemeral(false).queue();
            }


        } catch (Exception e) {
            EmbedBuilder eb = defaultError;
            eb.addField("Currency Error", "It seems that one or both of the currencies are wrong or don't exist.", false);
            eb.addField("Example", "convert 1.25 eur usd", false);
            hook.sendMessageEmbeds(eb.build()).setEphemeral(false).queue();
        }
    }

    private float[] getValueFor(String base, String destination, double amount, String token) {
        try {
            String params = "convert?q=%s_%s&compact=ultra&apiKey=%s";
            String formattedSend = String.format(params, base.toUpperCase(), destination.toUpperCase(), token);
            HttpResponse result = new HttpClient().request(HttpMethod.GET, ("https://free.currconv.com/api/v7/" + formattedSend));
            String res = result.asString();

            JSONObject obj = new JSONObject(res);
            String formedLanguageCode = base.toUpperCase() + "_" + destination.toUpperCase();

            float baseValue = Float.parseFloat(obj.get(formedLanguageCode).toString());
            float newPrice = baseValue * (float) amount;

            return new float[]{(float) amount, newPrice};

        } catch (Exception e) {
            if (e.getMessage().contains("503")) {
                return new float[]{503, 0};
            }
            System.out.println("Currency#getValueFor Error:" + e.getMessage());
            return null;
        }
    }

    private MessageEmbed getCurrencyEmbed(float old, float newPrice, String origin, String destination) {
        String title = origin.toUpperCase() + " -> " + destination.toUpperCase();
        String priceStr = String.format("%.2f", newPrice);
        String finalStr = old + " " + origin.toUpperCase() + " -> " + priceStr + " " + destination.toUpperCase();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setTitle("Currency Converter");
        eb.addField(title, finalStr, true);

        return eb.build();
    }
}
