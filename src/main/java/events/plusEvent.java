package events;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class plusEvent extends ListenerAdapter {
    public static final String uri = System.getenv("URI");
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        String username = event.retrieveMessage().complete().getAuthor().getName();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("ChillGrill");
            MongoCollection<Document> collection = database.getCollection("socialcredit");
            Bson projectionFields = Projections.fields(
                    Projections.include("user", "score"),
                    Projections.excludeId());
            Document doc = collection.find(eq("user", username))
                    .projection(projectionFields)
                    .first();
            if (doc == null) {
                try {
                    InsertOneResult result = collection.insertOne(new Document()
                            .append("_id", new ObjectId())
                            .append("user", username)
                            .append("score", 0));
                    System.out.println("Success! Inserted document id: " + result.getInsertedId());
                } catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
            } else {
                System.out.println(doc.toJson());
            }
        }
    }
}
