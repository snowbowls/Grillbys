package events;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class remreactEvent extends ListenerAdapter {
    public static final String uri = System.getenv("URI");
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

        String username = event.retrieveMessage().complete().getAuthor().getName();
        String reactor = "ree"; event.retrieveUser().complete().getName();
        //System.out.printf("user: %s reactor: %s", username, reactor);
        if(event.getReactionEmote().getId().equals("900119408859578451") && !username.equals(reactor)) {
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
                                .append("score", 15));

                        System.out.println("Success! Inserted document id: " + result.getInsertedId());
                    } catch (MongoException me) {
                        System.err.println("Unable to insert due to an error: " + me);
                    }
                } else {
                    int currVal = doc.getInteger("score");
                    doc.append("score", currVal - 15);
                    System.out.println("old: " + currVal + " new " + doc.getInteger("score"));
                    try {
                        Bson query = eq("user", username);
                        ReplaceOptions opts = new ReplaceOptions().upsert(true);

                        UpdateResult result = collection.replaceOne(query, doc, opts);

                        System.out.println("Modified document count: " + result.getModifiedCount());
                        System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                }
            }
        }







        if(event.getReactionEmote().getId().equals("934919187787288597") && !username.equals(reactor)) {
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
                    int currVal = doc.getInteger("score");
                    doc.append("score", currVal + 15);
                    System.out.println("old: " + currVal + " new " + doc.getInteger("score"));
                    try {
                        Bson query = eq("user", username);
                        ReplaceOptions opts = new ReplaceOptions().upsert(true);

                        UpdateResult result = collection.replaceOne(query, doc, opts);

                        System.out.println("Modified document count: " + result.getModifiedCount());
                        System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                }
            }
        }
    }
}
