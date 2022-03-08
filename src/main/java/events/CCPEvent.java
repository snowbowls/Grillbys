package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class CCPEvent extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();

        if(event.getMessage().getContentRaw().equals("echo")) {

            JSONParser parser = new JSONParser();
            JSONObject ccpKeys = null;
            JSONObject ccpValues = null;
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                JSONObject jsonObject = (JSONObject) obj;
                ccpKeys = (JSONObject) jsonObject.get("ccpKeys");
                ccpValues = (JSONObject) jsonObject.get("ccpValues");

            } catch (Exception e) {
                e.printStackTrace();
            }
            assert ccpKeys != null;
            assert ccpValues != null;

            int x = ccpKeys.size();
            System.out.println(x);
            for(int i = 1; i <= x; i++){
                if(msg.contains((String) ccpKeys.get(i))){
                    System.out.println(ccpValues.get(i));
                }
            }

        }
    }
}
