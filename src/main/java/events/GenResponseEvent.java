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
        if(msg.equalsIgnoreCase(("!help"))){

            List<String> command = new ArrayList<>();
            List<String> use = new ArrayList<>();

            StringBuilder commands = new StringBuilder();
            StringBuilder uses = new StringBuilder();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setThumbnail("https://images-ext-1.discordapp.net/external/HwnSW1Qv1B0_ZkomUohZ7P-TYmFsX775K0H4CdQRbAw/https/e0.pxfuel.com/wallpapers/940/704/desktop-wallpaper-glass-animals-zaba-artwork-by-micah-lidberg-glass-animals-pool-thumbnail.jpg");
            eb.setTitle("Zaba", null);
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
        else if(msg.equals("!explain") || msg.equals("tell 'em zaba")){
            event.getChannel().sendMessage("多黨制\t\t**Social Credit - How to perform your 中国共产党 Duty**"
                    + "\n*李洪志*\t   ● React to a comment with <:15_plus:900119408859578451> or <:15_minus:934919187787288597> to contribute to the author's social credit"
                    + "\n*六四天*\t   ● If the author and the reactor are the same user, their social credit is left unchanged"
                    + "\n*劉曉波*\t   ● If the react is removed, the author's social credit will update accordingly").queue();
            return;
        }

        if(!event.getMessage().getAuthor().isBot()) {
            JSONParser parser = new JSONParser();
            JSONObject gen = null;
            JSONObject genEx = null;
            JSONObject genCom = null;
            JSONObject jsonObject = null;
            JSONObject mto = null;
            JSONObject genEm = null;
            JSONObject emoteList = null;
            
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                gen = (JSONObject) jsonObject.get("general");
                genEx = (JSONObject) jsonObject.get("generalExact");
                genCom = (JSONObject) jsonObject.get("generalComplex");
                mto = (JSONObject) jsonObject.get("manyToOne");
                genEm = (JSONObject) jsonObject.get("generalEmote");
                emoteList = (JSONObject) jsonObject.get("emotes");


            } catch (Exception e) {
                e.printStackTrace();
            }

            assert gen != null;
            Set<String> scanGen = gen.keySet();
            for (String str : scanGen) {
                if(msg.contains(str)){
                    System.out.println( " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                    event.getChannel().sendMessage(gen.get(str).toString()).queue();
                }
            }

            assert genEx != null;
            Set<String> scanGenEx = genEx.keySet();
            for (String str : scanGenEx) {
                if(msg.equals(str)){
                    System.out.println( " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                    event.getChannel().sendMessage(genEx.get(msg).toString()).queue();
                }
            }


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

            assert genEm != null;
            assert emoteList != null;
            Set<String> scanGenEm = genEm.keySet();
            for (String str : scanGenEm) {
                if(msg.contains(str)){
                    String emote = emoteList.get(genEm.get(str)).toString();
                    event.getMessage().addReaction(Emoji.fromUnicode(emote)).queue();
                }
            }

            assert mto != null;
            for (int i = 1; i <= mto.size(); i++) {
                JSONObject keyScan = (JSONObject) mto.get(String.valueOf(i));
                String str = keyScan.keySet().toString();
                String key = str.substring(1, str.length() - 1);
                if (msg.contains(key)) {
                    String keyValue = keyScan.get(key).toString();
                    String resKey = (String) jsonObject.get(keyValue);
                    System.out.println(key + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                    event.getChannel().sendMessage(resKey).queue();
                }
            }
        }
    }
}
