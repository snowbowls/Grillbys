package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class JonEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event){
        String msg = event.getMessage().getContentRaw().toLowerCase();

        final Boolean[] trig = {false};
        ArrayList<String> triggers = new ArrayList<>();
        triggers.add("dbs");
        triggers.add("dragonball");
        triggers.add("goku");
        triggers.add("bejita");

        triggers.forEach((x) -> {
            if(msg.contains(x)){
                trig[0] = true;
            }
        });

        if(trig[0]){
            event.getChannel().sendMessage("**SHUTUPSHUTUPSHUTUPSHUTUPSHUTUP**").queue();
            if(event.getGuild().getId().equals("816125354875944960")) {
                event.getMessage().addReaction("15_neg:934919187787288597").queue();
            }
            else{
                event.getMessage().addReaction("amongass:854818205624827935").queue();
            }
        }

        if(msg.equals("jiren") && !trig[0]){
            event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/858416918586851368/874480650332307516/SHUTTHEFUCKUP-1.mp4").queue();
        }

        if(msg.equals("moe") && !trig[0]){
            event.getChannel().sendMessage("moe").queue();
        }

    }
}
