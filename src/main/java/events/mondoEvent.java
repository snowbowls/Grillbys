package events;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

public class mondoEvent extends ListenerAdapter {

    static Dotenv dotenv = Dotenv.load();

    public void onMessageReceived(MessageReceivedEvent event){

        String username = event.getAuthor().getName();


        try (MongoClient mongoClient = MongoClients.create(dotenv.get("URI"))) {
            MongoDatabase database = mongoClient.getDatabase("ChillGrill");
            MongoCollection<Document> collection = database.getCollection("socialcredit");
            try {
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("user", username)
                        .append("score", 0));
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }



    }
}
