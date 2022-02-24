package events;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class plusEvent extends ListenerAdapter {
    public static final String uri = System.getenv("URI");
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        String username;
        //username = msg.getAuthor().getName();
        //System.out.printf("%s ", username);

        /*try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("ChillGrill");
            MongoCollection<Document> collection = database.getCollection("socialcredit");
            Bson projectionFields = Projections.fields(
                    Projections.include("user"),
                    Projections.excludeId());
            Document doc = collection.find(eq("title", username))
                    .projection(projectionFields)
                    .first();
            if (doc == null) {
                System.out.println("No results found.");
            } else {
                System.out.println(doc.toJson());
            }
        }*/
    }
}
