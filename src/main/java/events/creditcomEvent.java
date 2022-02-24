package events;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.InsertOneResult;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class creditcomEvent extends ListenerAdapter {
    public static final String uri = System.getenv("URI");

    public void onMessageReceived(MessageReceivedEvent event){

        String username = event.getAuthor().getName();

        if(event.getMessage().getContentDisplay().equals("showmine")){
            try (MongoClient mongoClient = MongoClients.create(uri)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("socialcredit");
                try {
                    Bson projectionFields = Projections.fields(
                            Projections.include("user", "score"),
                            Projections.excludeId());
                    Document doc = collection.find(eq("user", username))
                            .projection(projectionFields)
                            .first();
                    if (doc == null) {
                        System.err.println("DNE");
                    }
                    else{
                        event.getChannel().sendMessage("User: " + username + "\nCredit Score: " + doc.getInteger("score")).complete();
                    }
                } catch (MongoException me) {
                    System.err.println("ERROR");
                }
            }
        }
    }
}
