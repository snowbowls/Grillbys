package events;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

public class ShowCommandEvent extends ListenerAdapter {
    public static final String uri = System.getenv("URI");

    public void onMessageReceived(MessageReceivedEvent event){
        String username = event.getMessage().getAuthor().getName();
        String userid = event.getMessage().getAuthor().getId();

        String[] msg = event.getMessage().getContentRaw().split(" ");
        if(msg[0].equalsIgnoreCase("!show")){
            if(msg[1].equalsIgnoreCase("mine")){
                try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "score"),
                                Projections.excludeId());
                        Document doc = collection.find(eq("userid", userid))
                                .projection(projectionFields)
                                .first();
                        if (doc == null) {
                            System.err.println("DNE");
                        } else {
                            event.getChannel().sendMessage("User: " + username + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }
            }
            else if (msg[1].equalsIgnoreCase("all")){
                try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "score"),
                                Projections.excludeId());
                        try (MongoCursor<Document> cursor = collection.find()
                                .projection(projectionFields)
                                .sort(Sorts.descending("username")).iterator()) {
                            while (cursor.hasNext()) {
                                Document doc = cursor.next();
                                event.getChannel().sendMessage("User: " + doc.getString("username") + "\nSocial Credit: " + doc.getInteger("score") + "\n-----------\n").complete();
                            }
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }

                }
            }
            else if (msg[1].substring(0,1).equalsIgnoreCase("<")){
                userid = event.getMessage().getContentRaw().substring(9,event.getMessage().getContentRaw().length()-1);
                try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "score"),
                                Projections.excludeId());
                        Document doc = collection.find(eq("userid", userid))
                                .projection(projectionFields)
                                .first();
                        if (doc == null) {
                            System.err.println("DNE");
                        } else {
                            event.getChannel().sendMessage("User: " + username + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }
            }
            else{
                username = event.getMessage().getContentRaw().substring(6);
                try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "score"),
                                Projections.excludeId());
                        Document doc = collection.find(eq("userid", userid))
                                .projection(projectionFields)
                                .first();
                        if (doc == null) {
                            System.err.println("DNE");
                        } else {
                            event.getChannel().sendMessage("User: " + username + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }

            }
        }
    }
}
