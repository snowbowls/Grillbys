package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ZabaEvent extends ListenerAdapter {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert jsonObject != null;

        if(msg.equals("echo")){

            JSONObject moods = (JSONObject) jsonObject.get("mood");
            int max = moods.size();
            int min = 1;
            int range = max - min + 1;
            int rand = (int)(Math.random() * range) + min;
            String rng = String.valueOf(rand);
            JSONObject mood =(JSONObject) moods.get(rng);

            String str = mood.keySet().toString();
            String key = str.substring(1, str.length() - 1);




            event.getChannel().sendMessage(key).addFile(new File("videos/" + mood.get(key).toString())).queue();
        }
    }
    public void moodPosting(){}
    public void birthdayPosting(){}
    public void fridayPosting(){}
}
