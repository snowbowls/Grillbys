package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class plusEvent extends ListenerAdapter {
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getGuild().getId().equals(("944254135476305980"))) {
            String id = event.getMessageId();

            if (event.getReactionEmote().getId().equals("900119408859578451")) {
                event.getChannel().retrieveMessageById(id).queue((message) -> { // async callback
                    System.out.printf("[%s]: %s\n", message.getAuthor().getName(), message.getContentDisplay());
                });
            }
        }
    }
}
