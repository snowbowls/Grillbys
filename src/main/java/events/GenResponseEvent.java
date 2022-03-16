package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class GenResponseEvent extends ListenerAdapter {
    // GenResponseEvents are actions that can occur in casual conversation.
    // You can think of this class as where most of the 'personality' of the bot is handled.
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
            JSONObject genCom = null;
            JSONObject jsonObject = null;
            JSONObject mto = null;
            
            try {
                Object obj = parser.parse(new FileReader("keywords.json"));
                jsonObject = (JSONObject) obj;
                gen = (JSONObject) jsonObject.get("general");
                genCom = (JSONObject) jsonObject.get("generalComplex");
                mto = (JSONObject) jsonObject.get("manyToOne");

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
                            System.out.println(key + " @" + event.getChannel().getName());
                            event.getChannel().sendMessage(" ").addFile(new File("videos/" + genScan.get(key) + "/" + meme)).queue();
                        }
                    }
                    else {
                        System.out.println(key + " @" + event.getChannel().getName());
                        event.getChannel().sendMessage(" ").addFile(new File("videos/" + genScan.get(key) + "/" + meme)).queue();
                    }
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
                    event.getChannel().sendMessage(resKey).queue();
                }
            }
        }
    }
}
