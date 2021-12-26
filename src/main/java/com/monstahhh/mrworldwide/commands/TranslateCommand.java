package com.monstahhh.mrworldwide.commands;

import com.monstahhh.http.HttpClient;
import com.monstahhh.http.HttpMethod;
import com.monstahhh.http.HttpResponse;
import com.neovisionaries.i18n.LanguageCode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslateCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null || !event.getName().equals("translate"))
            return;

        String sentenceToBeTranslated = event.getOption("sentence").getAsString();
        String language = this.getLanguageCode(event.getOption("originallanguage").getAsString());
        String destinationLanguage = this.getLanguageCode(event.getOption("destinationlanguage").getAsString());

        String result = getTranslation(language, destinationLanguage, sentenceToBeTranslated);
        EmbedBuilder eb = new EmbedBuilder();

        if (result == null) {
            event.reply("There was an error translating the sentence. Please try again later.").setEphemeral(false).queue();
        } else {
            String link = String.format("https://translate.google.com?sl=%s&tl=%s&op=translate&text=", language, destinationLanguage);
            String inputText = URLEncoder.encode(sentenceToBeTranslated, StandardCharsets.UTF_8);
            eb.setTitle("Google Translate", link + inputText);

            String originLocale = new Locale(language).getDisplayLanguage(Locale.ENGLISH);
            String wantedLocale = new Locale(destinationLanguage).getDisplayLanguage(Locale.ENGLISH);

            eb.addField(originLocale + " -> " + wantedLocale, sentenceToBeTranslated + " -> " + result, false);
            eb.setColor(Color.BLUE);

            event.replyEmbeds(eb.build()).setEphemeral(false).queue();
        }
    }

    private String getTranslation(String origin, String destination, String msg) {
        String result = null;

        try {
            result = requestTranslation(msg, origin, destination);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private String requestTranslation(String textToTranslate, String from, String to) throws IOException {

        if (textToTranslate.contains(". ")) {
            String[] sentences = textToTranslate.split("\\.");

            StringBuilder sb = new StringBuilder();
            for (String sentence : sentences) {
                String singleResult = castResult(doTranslateFor(sentence, from, to).asString());
                sb.append(singleResult);
                sb.append(". ");
            }

            return sb.toString();
        } else {
            return castResult(doTranslateFor(textToTranslate, from, to).asString());
        }
    }

    private HttpResponse doTranslateFor(String textToTranslate, String from, String to) throws IOException {
        String encodedText = java.net.URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8);
        String paramsLink = "single?client=gtx&sl=%s&tl=%s&ie=UTF-8&oe=UTF-8&dt=t&dt=rm&q=%s";
        String params = String.format(paramsLink, from, to, encodedText);

        return new HttpClient().request(HttpMethod.GET, ("https://translate.googleapis.com/translate_a/" + params));
    }

    private String castResult(String result) {
        Pattern pat = Pattern.compile("\"(.*?)\"");
        List<String> allMatches = new ArrayList<>();

        Matcher matcher = pat.matcher(result);
        while (matcher.find()) {
            allMatches.add(matcher.group().replace("\"", ""));
        }

        return allMatches.get(0);
    }

    private String getLanguageCode(String language) {
        String capLang = language.substring(0, 1).toUpperCase() + language.substring(1); //Language needs to have an uppercase letter to be found
        List<LanguageCode> languageCodes = LanguageCode.findByName(capLang);
        return languageCodes.get(0).toString();
    }
}
