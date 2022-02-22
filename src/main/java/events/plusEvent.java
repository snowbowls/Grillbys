package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class plusEvent extends ListenerAdapter {
    public void onMessageReactionAdd(MessageReactionAddEvent event) {


        if(event.getReactionEmote().getId().equals("900119408859578451")){
           // event.getChannel().sendMessage("YEEET").queue();
            System.out.printf("%s\n", event.getChannel());


        }
        //event.getChannel().sendMessage("U+1F44D").complete();

        //event.getChannel().addReactionby(":boatyvv:854825460494761994");
        //event.getChannel().sendMessage(embedBuilder.build()).complete().addReaction("✔").queue();


    }
}
