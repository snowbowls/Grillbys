package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class JonEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event){
        String msg = event.getMessage().getContentRaw().toLowerCase();

        final Boolean[] trig = {false};
        ArrayList<String> triggersDBS = new ArrayList<>();
        triggersDBS.add("dbs");
        triggersDBS.add("dragonball");
        triggersDBS.add("goku");
        triggersDBS.add("bejita");

        triggersDBS.forEach((x) -> {
            if(msg.contains(x)){
                trig[0] = true;
            }
        });

        if(trig[0]){
            event.getChannel().sendMessage("**SHUTUPSHUTUPSHUTUPSHUTUPSHUTUP**").queue();

            event.getMessage().addReaction("15_neg:934919187787288597").queue();
            System.out.println(msg);
        }

        if(msg.contains("jiren") && !trig[0]){
            event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/858416918586851368/874480650332307516/SHUTTHEFUCKUP-1.mp4").queue();
            System.out.println(msg);
        }

        if(msg.contains("moe") && !trig[0]){
            event.getChannel().sendMessage("moe").queue();
            System.out.println(msg);
        }

        final Boolean[] trig2 = {false};
        ArrayList<String> triggersRDM = new ArrayList<>();
        triggersRDM.add("sus");
        triggersRDM.add("among");
        triggersRDM.add("amungus");
        triggersRDM.add("amogus");
        triggersRDM.add("imposter");

        triggersRDM.forEach((x) -> {
            if(msg.contains(x)){
                trig2[0] = true;
            }
        });

        if(trig2[0]) {
            event.getMessage().addReaction("amongass:854818205624827935").queue();
            System.out.println(msg);
        }

        if(msg.contains("grill")){
            event.getMessage().addReaction("justagriller:816352491386044426").queue();
            System.out.println(msg);
        }

        if(msg.equals("n")){
            event.getMessage().addReaction("disintegrate:829491387824865321").queue();
            event.getMessage().addReaction("touch_grass:936036425789493269").queue();
            event.getMessage().addReaction("soyjak:921475501023977573").queue();
            event.getMessage().addReaction("bravo:250845632141590528").queue();
            event.getMessage().addReaction("3dHyperThink:676990578793381937").queue();
            event.getMessage().addReaction("grabs_you:666497981742186506").queue();
            event.getMessage().addReaction("shiba_not_amused:866064465359011880").queue();
            event.getMessage().addReaction("15_neg:934919187787288597").queue();
            System.out.println(msg);
        }

        if(msg.contains("cum")){
            event.getMessage().addReaction("gokek:801618290577113109").queue();
            System.out.println(msg);

        }

        if(msg.contains("foot") || msg.contains("feet")){
            event.getMessage().addReaction("Hyper_Engineer:666502984846409758").queue();
            System.out.println(msg);
        }

        if(msg.contains("@everyone")){
            event.getChannel().sendMessage("wow ur so cool and brave").queue();
            System.out.println(msg);
        }

        if(msg.contains("alexa") && (msg.contains("search") || msg.contains("play"))){
            event.getChannel().sendMessage("do it yourself").queue();
            System.out.println(msg);
        }

        if(msg.contains("sex")){
            event.getMessage().addReaction("ned_leeds:936792444895363072").queue();
            System.out.println(msg);
        }


    }
}
