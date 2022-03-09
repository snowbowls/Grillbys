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
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class AddReactEvent extends ListenerAdapter {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        Message msg = event.retrieveMessage().complete();
        String username = msg.getAuthor().getName();
        String userid = msg.getAuthor().getId();
        String reactor = Objects.requireNonNull(event.getUser()).getName();
        boolean isCustom;

        String jiApprove = "zhao_xina:900118296471425124";
        String jiCondemn = "mao_zedong:934920068729536512";

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        try{
            String s = event.getReactionEmote().getId();
            isCustom = true;

        }
        catch(Exception e){
            isCustom = false;
        }

        if(isCustom) {
            // ----- CREDIT SCORE -----
            String readId = event.getReactionEmote().getId();
            // Trigger when message add react +15
            if (readId.equals("900119408859578451") && !username.equals(reactor)) {
                try (MongoClient mongoClient = MongoClients.create(settings)) {

                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    Bson projectionFields = Projections.fields(
                            Projections.include("username", "score", "userid"),
                            Projections.excludeId());
                    Document doc = collection.find(eq("userid", userid))
                            .projection(projectionFields)
                            .first();
                    if (doc == null) {

                        try {
                            InsertOneResult result = collection.insertOne(new Document()
                                    .append("_id", new ObjectId())
                                    .append("username", username)
                                    .append("userid", userid)
                                    .append("score", 15));

                            System.out.println("Success! Inserted document id: " + result.getInsertedId() + "add15");
                        } catch (MongoException me) {
                            System.err.println("Unable to insert due to an error: " + me);
                        }
                    } else {
                        int currVal = doc.getInteger("score");
                        doc.append("score", currVal + 15);
                        System.out.println("-------------------------------------------------");
                        System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " @" + dtf.format(now));
                        System.out.println("Reactor: " + reactor + " @" + event.getChannel().getName());
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

            // Trigger when message add react -15
            if (readId.equals("934919187787288597") && !username.equals(reactor)) {

                try (MongoClient mongoClient = MongoClients.create(settings)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    Bson projectionFields = Projections.fields(
                            Projections.include("username", "score", "userid"),
                            Projections.excludeId());
                    Document doc = collection.find(eq("userid", userid))
                            .projection(projectionFields)
                            .first();

                    if (doc == null) {
                        try {
                            InsertOneResult result = collection.insertOne(new Document()
                                    .append("_id", new ObjectId())
                                    .append("username", username)
                                    .append("userid", userid)
                                    .append("score", -15));

                            System.out.println("Success! Inserted document id: " + result.getInsertedId() + "add-15");
                        } catch (MongoException me) {
                            System.err.println("Unable to insert due to an error: " + me);
                        }
                    } else {
                        int currVal = doc.getInteger("score");
                        doc.append("score", currVal - 15);
                        System.out.println("-------------------------------------------------");
                        System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " @" + dtf.format(now));
                        System.out.println("Reactor: " + reactor + " @" + event.getChannel().getName());
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

            // Count num of reacts for +15
            if (readId.equals("900119408859578451")) {

                List<MessageReaction> reactionsList = msg.getReactions();
                List<User> users = null;
                int i; // Num of diff reacts

                for (i = 0; i < reactionsList.size(); i++) {
                    users = reactionsList.get(i).retrieveUsers().complete();
                }

                assert users != null;
                int cnt = users.size(); // Num of react

                if (cnt >= 2) {
                    msg.addReaction(jiApprove).queue();
                }
            }

            // Count num of reacts for -15
            if (readId.equals("934919187787288597")) {

                List<MessageReaction> reactionsList = msg.getReactions();
                List<User> users = null;
                int i; // Num of diff reacts

                for (i = 0; i < reactionsList.size(); i++) {
                    users = reactionsList.get(i).retrieveUsers().complete();
                }

                assert users != null;
                int cnt = users.size(); // Num of react

                if (cnt >= 2) {
                    msg.addReaction(jiCondemn).queue();
                }
            }

            // ----- OTHER -----

            if (readId.equals("887861940012085288")) {
                msg.addReaction("soy_point:887860865439789086").queue();
            }

            if (readId.equals("802264386026340403")) {
                msg.addReaction("sus:802264386026340403").queue();
            }
        }

    }
}
