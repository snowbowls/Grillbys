package events;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class HelpEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equalsIgnoreCase(("!help"))){
            event.getChannel().sendMessage(""
                    + "\n   "
                    + "\n   "
                    + "\n   ").queue();

        }
        if(event.getMessage().getContentRaw().equalsIgnoreCase("json")){
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("test.json"));
                JSONObject jsonObject = (JSONObject)obj;
                String name = (String)jsonObject.get("Name");
                String course = (String)jsonObject.get("Course");
                JSONArray subjects = (JSONArray)jsonObject.get("test");
                System.out.println("Name: " + name);
                System.out.println("Course: " + course);
                System.out.println("Subjects:");
                for (Object subject : subjects) {
                    if(subjects.contains("a")) {
                        System.out.println(subject);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else if(event.getMessage().getContentRaw().equals("!explain") || event.getMessage().getContentRaw().equals("tell 'em zaba")){
                event.getChannel().sendMessage("多黨制\t\t**Social Credit - How to preform your 中国共产党 Duty**"
                    + "\n*李洪志*\t   ● React to a comment with <:15_plus:900119408859578451> or <:15_minus:934919187787288597> to contribute to the author's social credit"
                    + "\n*六四天*\t   ● If the author and the reactor are the same user, their social credit is left unchanged"
                    + "\n*劉曉波*\t   ● If the react is removed, the author's social credit will update accordingly").queue();
        }
    }
    private static void parseEmployeeObject(JSONObject employee)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee.get("test");

        //Get employee first name
        String firstName = (String) employeeObject.get("1");
        System.out.println(firstName);

        //Get employee last name
        String lastName = (String) employeeObject.get("2");
        System.out.println(lastName);

        //Get employee website name
        String website = (String) employeeObject.get("3");
        System.out.println(website);
    }
}
