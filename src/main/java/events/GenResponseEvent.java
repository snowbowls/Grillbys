package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenResponseEvent extends ListenerAdapter {
    // GenResponseEvents are actions that can occur in casual conversation.
    // You can think of this class as where most of the 'personality' of the bot is handled.
    // Naming convention can be done better but the gist is this class is used for handling the JSON file
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();

        String[] help = msg.split("\\s");
        if(help[0].equalsIgnoreCase(("!help"))){
            if(help.length == 1){
                List<String> command = new ArrayList<>();
                List<String> use = new ArrayList<>();

                StringBuilder commands = new StringBuilder();
                StringBuilder uses = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setThumbnail("https://images-ext-1.discordapp.net/external/HwnSW1Qv1B0_ZkomUohZ7P-TYmFsX775K0H4CdQRbAw/https/e0.pxfuel.com/wallpapers/940/704/desktop-wallpaper-glass-animals-zaba-artwork-by-micah-lidberg-glass-animals-pool-thumbnail.jpg");
                eb.setTitle("Zaba", null);
                eb.setColor(new Color(114, 41, 54));

                command.add("!help player\n");
                use.add("Lists commands for the audio player\n");
                command.add("!help poll\n");
                use.add("Explains how the poll function works\n");
                command.add("!help socialcredit\n");
                use.add("Explains how social credit works\n");

                for(String s : command)
                    commands.append(s);
                for(String s: use)
                    uses.append(s);

                eb.addField("Command", commands.toString(), true);
                eb.addField("Function", uses.toString(), true);
                MessageCreateData data = new MessageCreateBuilder()
                        .addEmbeds(eb.build())
                        .build();
                event.getChannel().sendMessage(data).queue();
                return;
            }
            if(help[1].equalsIgnoreCase("player")){
                List<String> command = new ArrayList<>();
                List<String> use = new ArrayList<>();

                StringBuilder commands = new StringBuilder();
                StringBuilder uses = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setThumbnail("https://images-ext-1.discordapp.net/external/HwnSW1Qv1B0_ZkomUohZ7P-TYmFsX775K0H4CdQRbAw/https/e0.pxfuel.com/wallpapers/940/704/desktop-wallpaper-glass-animals-zaba-artwork-by-micah-lidberg-glass-animals-pool-thumbnail.jpg");
                eb.setTitle("Audio Player", null);
                eb.setColor(new Color(114, 41, 54));

                command.add("!play\n");
                use.add("Adds or plays songs from the current queue\n");
                command.add("!leave\n");
                use.add("Leaves the voice channel\n");
                command.add("!pplay\n");
                use.add("Adds a playlist to the queue\n");
                command.add("!pause\n");
                use.add("Pauses audio playback\n");
                command.add("!stop\n");
                use.add("Completely stops audio playback\n");
                command.add("!skip\n");
                use.add("Skips the current song\n");
                command.add("!nowplaying\n");
                use.add("Prints information about the current song\n");
                command.add("!np\n");
                use.add("Alias for nowplaying\n");
                command.add("!list\n");
                use.add("Lists the songs in the queue\n");
                command.add("!volume [val]\n");
                use.add("Sets the volume of the MusicPlayer [10 - 100]\n");
                command.add("!restart\n");
                use.add("Restarts the current song\n");
                command.add("!repeat\n");
                use.add("Makes the player repeat the currently playing song\n");
                command.add("!reset\n");
                use.add("Completely resets the player for a quick fix\n");

                for(String s : command)
                    commands.append(s);
                for(String s: use)
                    uses.append(s);

                eb.addField("Command", commands.toString(), true);
                eb.addField("Function", uses.toString(), true);
                MessageCreateData data = new MessageCreateBuilder()
                        .addEmbeds(eb.build())
                        .build();
                event.getChannel().sendMessage(data).queue();
                }
            if(help[1].equalsIgnoreCase("poll")){
                List<String> command = new ArrayList<>();
                List<String> call = new ArrayList<>();
                List<String> calldesc = new ArrayList<>();


                StringBuilder commands = new StringBuilder();
                StringBuilder calls = new StringBuilder();
                StringBuilder calldescs = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setThumbnail("https://images-ext-1.discordapp.net/external/HwnSW1Qv1B0_ZkomUohZ7P-TYmFsX775K0H4CdQRbAw/https/e0.pxfuel.com/wallpapers/940/704/desktop-wallpaper-glass-animals-zaba-artwork-by-micah-lidberg-glass-animals-pool-thumbnail.jpg");
                eb.setTitle("Poll Function", null);
                eb.setColor(new Color(114, 41, 54));

                command.add("Send '!poll' followed by up to 10 options separated by commas\n\n");
                command.add("Example: !poll TITLE HERE, Option 1, Option 2, Option 3, ...\n");
                call.add("!poll headcount\n");
                calldesc.add("Options for going, not going, and don't know yet");

                for(String s : command)
                    commands.append(s);
                for(String s: call)
                    calls.append(s);
                for(String s: calldesc)
                    calldescs.append(s);

                eb.addField("Command Explanation", commands.toString(), false);
                eb.addField("Premade Polls", calls.toString(), true);
                eb.addField("Description", calldescs.toString(), true);
                MessageCreateData data = new MessageCreateBuilder()
                        .addEmbeds(eb.build())
                        .build();
                event.getChannel().sendMessage(data).queue();
            }
            if(help[1].equalsIgnoreCase("socialcredit")){
                event.getChannel().sendMessage("多黨制\t\t**Social Credit - How to perform your 中国共产党 Duty**"
                        + "\n*李洪志*\t   ● React to a comment with <:15_plus:900119408859578451> or <:15_minus:934919187787288597> to contribute to the author's social credit"
                        + "\n*六四天*\t   ● If the author and the reactor are the same user, their social credit is left unchanged"
                        + "\n*劉曉波*\t   ● If the react is removed, the author's social credit will update accordingly"
                        + "\n*李洪志*\t   ● Use '!show all' or '!show mine' to view social credit scores").queue();
                return;
            }
        }

        List<String> unapprovedChannels = new ArrayList<>();
        unapprovedChannels.add("946443239630733322");
        unapprovedChannels.add("954377064247599154");

        if(!event.getMessage().getAuthor().isBot()) {
            JSONParser parser = new JSONParser();
            JSONObject gen = null;
            JSONObject genEx = null;
            JSONObject genCom = null;
            JSONObject jsonObject = null;
            JSONObject mto = null;
            JSONObject genEm = null;
            JSONObject emoteList = null;
            JSONObject responseList = null;
            JSONObject genProb = null;
            JSONObject genZaba = null;
            
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                gen = (JSONObject) jsonObject.get("general");
                genEx = (JSONObject) jsonObject.get("generalExact");
                genCom = (JSONObject) jsonObject.get("generalComplex");
                mto = (JSONObject) jsonObject.get("manyToOne");
                genEm = (JSONObject) jsonObject.get("generalEmote");
                genProb = (JSONObject) jsonObject.get("generalProb");
                emoteList = (JSONObject) jsonObject.get("emotes");
                responseList = (JSONObject) jsonObject.get("responseList");
                genZaba = (JSONObject) jsonObject.get("generalZaba");


            } catch (Exception e) {
                e.printStackTrace();
            }


            // GenEm or General Emote will react with an emote whenever a trigger word / phrase is detected in a message
            assert genEm != null;
            assert emoteList != null;
            Set<String> scanGenEm = genEm.keySet();
            for (String str : scanGenEm) {
                if(msg.contains(str)){
                    String emote = emoteList.get(genEm.get(str)).toString();
                    event.getMessage().addReaction(Emoji.fromUnicode(emote)).queue();
                }
            }

            if(unapprovedChannels.contains(event.getChannel().getId()))
                return;
            // Gen or General will respond whenever a trigger word / phrase is detected in a message
            assert gen != null;
            Set<String> scanGen = gen.keySet();
            for (String str : scanGen) {
                if(msg.contains(str)){
                    System.out.println( " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                    event.getChannel().sendMessage(gen.get(str).toString()).queue();
                }
            }

            // GenEx or General Exact will respond only when the message in question matches exactly with the trigger list
            assert genEx != null;
            Set<String> scanGenEx = genEx.keySet();
            String msgClean= msg.replaceAll("[^a-zA-Z0-9 ]", "");
            for (String str : scanGenEx) {
                if(msgClean.equals(str)){
                    System.out.println( " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                    event.getChannel().sendMessage(genEx.get(msgClean).toString()).queue();
                }
            }

            // GenCom or General Complex will trigger from a list of words and pull a random response from another list
            assert genCom != null;
            for (int i = 1; i <= genCom.size(); i++){
                JSONObject genScan = (JSONObject) genCom.get(String.valueOf(i));
                String str = genScan.keySet().toString();
                String key = str.substring(1, str.length() - 1);
                if (msg.contains(key)) {
                    JSONObject responses = (JSONObject) jsonObject.get(genScan.get(key));
                    int rand = (int)((Math.random() * (responses.size() - 1)) + 1);
                    String meme =  responses.get(String.valueOf(rand)).toString();

                    if(key.equals("uwu")) {
                        if (Math.random() > .65) {
                            System.out.println(key + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                            MessageCreateData data = new MessageCreateBuilder()
                                    .setFiles(FileUpload.fromData(new File("videos/" + genScan.get(key) + "/" + meme)))
                                    .build();
                            event.getChannel().sendMessage(data).queue();
                        }
                    }
                    else {
                        System.out.println(key + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                        MessageCreateData data = new MessageCreateBuilder()
                                .setFiles(FileUpload.fromData(new File("videos/" + genScan.get(key) + "/" + meme)))
                                .build();
                        event.getChannel().sendMessage(data).queue();
                    }
                }
            }

            // GenProb or General Probability when triggered will pull from a list of responses accompanied by a chance of the response being given
            assert genProb != null;
            Set<String> scanProb = genProb.keySet();
            for (String str : scanProb) {
                if(msg.contains(str)){
                    String resp = genProb.get(str).toString();
                    String[] split = resp.split("\\;");
                    if(Math.random() < Float.parseFloat(split[1])/100)
                        event.getChannel().sendMessage(split[0]).queue();
                }
            }

            // GenZaba or General Zaba is like General but specifically when talking to Zaba, keeps things organized
            assert genZaba != null;
            Set<String> scanZaba = genZaba.keySet();
            for (String str : scanZaba) {
                if(msg.contains(str)){
                    String resp = genZaba.get(str).toString();
                    event.getChannel().sendMessage(resp).queue();
                }
            }


            // mto or Many To One is...
            assert mto != null;
            assert responseList != null;
            Set<String> scanMto = mto.keySet();
            for (String str : scanMto) {
                if(msg.contains(str)){
                    String resp = responseList.get(mto.get(str)).toString();
                    event.getChannel().sendMessage(resp).queue();
                }
            }
        }
    }
}
