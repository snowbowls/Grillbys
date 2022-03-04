package events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equalsIgnoreCase(("!help"))){
            event.getChannel().sendMessage("Commands:"
                    + "\nree"
                    + "\nree").queue();
        }
    }
}
