package events;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class JonEvent extends ListenerAdapter {
    // JonEvents are various actions with the intent of annoying someone. Nothing about this class is
    // organized well, it usually serves as a half-measure attempt at handling silly responses
    public void onMessageReceived(MessageReceivedEvent event) {

        String msg = event.getMessage().getContentRaw().toLowerCase();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        JSONArray triggers;

        // Load JSON
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert jsonObject != null;

        // Dragonball triggers
        triggers = (JSONArray) jsonObject.get("dragonball");
        for (Object key : triggers) {
            if (msg.contains(key.toString())) {
                event.getChannel().sendMessage("**SHUTUPSHUTUPSHUTUPSHUTUPSHUTUP**").queue();

                if(event.getAuthor().getId().equals("222163619125788682")) {
                    event.getMessage().addReaction("15_neg:934919187787288597").queue();
                }
                System.out.println("dragonball stuff" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                return;
            }
        }

        // Sus triggers
        triggers = (JSONArray) jsonObject.get("sus");
        for (Object key : triggers) {
            if (msg.contains(key.toString())) {
                event.getMessage().addReaction("amongass:854818205624827935").queue();
                System.out.println("sus stuff" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                return;
            }
        }

        if (msg.contains("grill")) {
            event.getMessage().addReaction("justagriller:816352491386044426").queue();
            System.out.println("grill" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }
        if (msg.equals("based")){
            if(Math.random() > 0.85){
                event.getChannel().sendMessage("not based").queue();
                System.out.println("not based" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
                }
        }

        if (msg.equals("n")) {
            event.getMessage().addReaction("disintegrate:829491387824865321").queue();
            event.getMessage().addReaction("touch_grass:936036425789493269").queue();
            event.getMessage().addReaction("soyjak:921475501023977573").queue();
            event.getMessage().addReaction("bravo:250845632141590528").queue();
            event.getMessage().addReaction("3dHyperThink:676990578793381937").queue();
            event.getMessage().addReaction("grabs_you:666497981742186506").queue();
            event.getMessage().addReaction("shiba_not_amused:866064465359011880").queue();
            if(!event.getAuthor().getId().equals("222163619125788682")) {
                event.getMessage().addReaction("15_neg:934919187787288597").queue();
            }
            System.out.println("n" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
            return;
        }

        if (msg.contains("cum")) {
            event.getMessage().addReaction("gokek:801618290577113109").queue();
            System.out.println("cum" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());

        }

        if (msg.contains("foot") || msg.contains("feet")) {
            event.getMessage().addReaction("Hyper_Engineer:666502984846409758").queue();
            System.out.println("foot / feet" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }

        if (msg.contains("@everyone")) {
            event.getChannel().sendMessage("https://media.discordapp.net/attachments/944254315630035005/951189791691669534/unknown.png").queue();
            System.out.println("@everyone" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }

        if (msg.contains("zaba")) {
            event.getMessage().addReaction("rdj:860593603033432064").queue();
            System.out.println("zaba" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }

        if (msg.contains("busta")) {
            event.getMessage().addReaction("pet_busta:950885255202615316").queue();
            System.out.println("busta" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }

        if (msg.contains("crab")) {
            event.getMessage().addReaction("Crab_Rave:666502984976564224> ").queue();
            System.out.println("crab" + " #" + event.getChannel().getName() + " @" + event.getMessage().getAuthor().getName());
        }

        if (msg.contains("rrrr mom")){
            JSONObject response = null;
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                response = (JSONObject) jsonObject.get("socialCredit");

            } catch (Exception e) {
                e.printStackTrace();
            }
                System.out.println("************************************************************");
                System.out.println(event.getAuthor().getName() + " had their family kidnapped!" );

                String userid = event.getAuthor().getName();
                JSONObject finalResponse = response;
                if (Math.random() < .90)
                    event.getJDA().getUserById(event.getMessage().getAuthor().getId()).openPrivateChannel()
                            .flatMap(channel -> channel.sendMessage(finalResponse.get("1").toString()))
                            .queue();
                else {
                    event.getJDA().getUserById(event.getMessage().getAuthor().getId()).openPrivateChannel()
                            .flatMap(channel -> channel.sendMessage(finalResponse.get("2").toString()))
                            .queue();
                    System.out.println(" U W U " );
                }


                System.out.println("************************************************************");
        }

        // Delete me
        //event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/858416918586851368/874480650332307516/SHUTTHEFUCKUP-1.mp4").queue();
    }
}
