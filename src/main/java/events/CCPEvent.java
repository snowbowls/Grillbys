package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;
import java.util.Set;

public class CCPEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if(!event.getMessage().getAuthor().isBot()) {
            String msg = event.getMessage().getContentRaw();

            JSONParser parser = new JSONParser();
            JSONObject ccp = null;
            JSONObject jsonObject = null;

            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                ccp = (JSONObject) jsonObject.get("ccp");


            } catch (Exception e) {
                e.printStackTrace();
            }
            assert ccp != null;
            for (int i = 1; i <= ccp.size(); i++) {
                JSONObject ccpScan = (JSONObject) ccp.get(String.valueOf(i));
                String str = ccpScan.keySet().toString();
                String key = str.substring(1, str.length() - 1);
                if (msg.contains(key)) {
                    String keyValue = ccpScan.get(key).toString();
                    String resKey = (String) jsonObject.get(keyValue);
                    event.getChannel().sendMessage(resKey).queue();
                }
            }
        }
    }
}
