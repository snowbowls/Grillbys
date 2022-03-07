package events;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class RemoveReactEvent extends ListenerAdapter {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    //public static final String uri = System.getenv("URI");
    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

        Message msg = event.retrieveMessage().complete();
        String username = msg.getAuthor().getName();
        String userid = msg.getAuthor().getId();
        String reactor = Objects.requireNonNull(event.getUser()).getName();

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();


        // Trigger when message rem react +15
        if(event.getReactionEmote().getId().equals("900119408859578451") && !username.equals(reactor)) {
            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("socialcredit");
                Bson projectionFields = Projections.fields(
                        Projections.include("username", "score", "userid"),
                        Projections.excludeId());
                Document doc = collection.find(eq("userid", userid))
                        .projection(projectionFields)
                        .first();
                if (doc == null) { // Does this ever trigger?
                    System.out.print("NULL DOC at rem+15");
                } else {
                    int currVal = doc.getInteger("score");
                    doc.append("score", currVal - 15);
                    System.out.println("-------------------------------------------------");
                    System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " @" + dtf.format(now));
                    System.out.println("Reactor: " + reactor);
                    try {
                        Bson query = eq("userid", userid);
                        ReplaceOptions opts = new ReplaceOptions().upsert(true);

                        UpdateResult result = collection.replaceOne(query, doc, opts);
                    } catch (MongoException me) {
                        System.err.println("\nUnable to update due to an error: " + me);
                    }
                }
            }
        }

        // Trigger when message rem react -15
        if(event.getReactionEmote().getId().equals("934919187787288597") && !username.equals(reactor)) {
            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("socialcredit");
                Bson projectionFields = Projections.fields(
                        Projections.include("username", "score", "userid"),
                        Projections.excludeId());
                Document doc = collection.find(eq("userid", userid))
                        .projection(projectionFields)
                        .first();
                if (doc == null) { // Does this ever trigger?
                    System.out.print("NULL DOC at rem-15");
                } else {
                    int currVal = doc.getInteger("score");
                    doc.append("score", currVal + 15);
                    System.out.println("-------------------------------------------------");
                    System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " @" + dtf.format(now));
                    System.out.println("Reactor: " + reactor);
                    try {
                        Bson query = eq("userid", userid);
                        ReplaceOptions opts = new ReplaceOptions().upsert(true);

                        UpdateResult result = collection.replaceOne(query, doc, opts);
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                }
            }
        }
    }
}
