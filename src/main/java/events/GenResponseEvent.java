package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class GenResponseEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();
        if(msg.equalsIgnoreCase(("!help"))){
            event.getChannel().sendMessage(""
                    + "\n   "
                    + "\n   "
                    + "\n   ").queue();
            return;

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
            JSONObject jsonObject;
            
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                gen = (JSONObject) jsonObject.get("general");


            } catch (Exception e) {
                e.printStackTrace();
            }
            assert gen != null;
            for (int i = 1; i <= gen.size(); i++) {
                JSONObject genScan = (JSONObject) gen.get(String.valueOf(i));
                String str = genScan.keySet().toString();
                String key = str.substring(1, str.length() - 1);
                if (msg.contains(key)) {
                    System.out.println(key + " @" + event.getChannel().getName());
                    event.getChannel().sendMessage(genScan.get(key).toString()).queue();
                }
            }
        }
    }
}
