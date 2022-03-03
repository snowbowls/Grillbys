package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TagUserEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();

        if(event.getMessage().getAuthor().getId().equals("222163619125788682")){
            event.getChannel().sendMessage("<@222163619125788682>").complete();
            //event.getChannel().getHistory().
        }
    }
}
