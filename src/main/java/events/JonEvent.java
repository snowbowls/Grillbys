package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JonEvent extends ListenerAdapter {
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
                System.out.println(msg);
            }
        }

        // Sus triggers
        triggers = (JSONArray) jsonObject.get("sus");
        for (Object key : triggers) {
            if (msg.contains(key.toString())) {
                event.getMessage().addReaction("amongass:854818205624827935").queue();
                return;
            }
        }

        if (msg.contains("grill")) {
            event.getMessage().addReaction("justagriller:816352491386044426").queue();
            System.out.println(msg);
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
            System.out.println(msg);
            return;
        }

        if (msg.contains("cum")) {
            event.getMessage().addReaction("gokek:801618290577113109").queue();
            System.out.println(msg);

        }

        if (msg.contains("foot") || msg.contains("feet")) {
            event.getMessage().addReaction("Hyper_Engineer:666502984846409758").queue();
            System.out.println(msg);
        }

        if (msg.contains("@everyone")) {
            event.getChannel().sendMessage("https://media.discordapp.net/attachments/944254315630035005/951189791691669534/unknown.png").queue();
            System.out.println(msg);
        }

        if (msg.contains("busta")) {
            event.getMessage().addReaction("pet_busta:950885255202615316").queue();
            System.out.println(msg);
        }

        if (msg.contains("crab")) {
            event.getMessage().addReaction("Crab_Rave:666502984976564224> ").queue();
            System.out.println(msg);
        }

        // Delete me
        //event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/858416918586851368/874480650332307516/SHUTTHEFUCKUP-1.mp4").queue();
    }
}
