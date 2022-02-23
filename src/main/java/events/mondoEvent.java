package events;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

public class mondoEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event){
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://snowbowls:13241324@cluster0.r4aqv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"));
        MongoDatabase database = mongoClient.getDatabase("sample_restaurants");
        MongoCollection<Document> collection = database.getCollection("sample_restaurants.restaurants\n");
        Document student1 = collection.find(new Document("cuisine", "American")).first();
       // System.out.println("Student 1: " + student1.toJson()); // IT BREAKS HERE




    }
}
