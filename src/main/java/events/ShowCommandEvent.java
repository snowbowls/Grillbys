package events;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ShowCommandEvent extends ListenerAdapter {

    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReceived(MessageReceivedEvent event){
        //String username = event.getAuthor().getName();
        String userid = event.getMessage().getAuthor().getId();
        String[] msg = event.getMessage().getContentRaw().split(" ");

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        if(msg[0].equalsIgnoreCase("!show")){
            // !show mine
            if(msg[1].equalsIgnoreCase("mine")){
                System.out.println("Command: !show mine -" + event.getMessage().getAuthor().getName() + " @" + event.getChannel().getName());
                try (MongoClient mongoClient = MongoClients.create(settings)) {
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
                            event.getChannel().sendMessage("idk who you are, go get some credit then come back").queue();
                            System.err.println("DNE - " + event.getMessage().getAuthor().getName() + " @" + event.getChannel().getName());
                        } else {
                            event.getChannel().sendMessage("User: " + doc.getString("username") + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }
            }
            // !show all
            else if (msg[1].equalsIgnoreCase("all")){
                System.out.println("Command !show all -" + event.getMessage().getAuthor().getName() + " @" + event.getChannel().getName());

                event.getGuild().loadMembers();
                List<Member> users = event.getGuild().getMembers();
                List<String> usersId = new ArrayList<>();

                List<String> currUsers = new ArrayList<>();
                List<String> currCredit = new ArrayList<>();

                StringBuilder sbUser = new StringBuilder();
                StringBuilder sbCredit = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setThumbnail("https://raw.githubusercontent.com/snowbowls/Zaba/master/images/icon.PNG");
                eb.setTitle("社会信用体系", null);
                eb.setColor(new Color(114, 41, 54));




                for (Member u : users) {
                    usersId.add(u.getId());
                }

                try (MongoClient mongoClient = MongoClients.create(settings)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "userid", "score"),
                                Projections.excludeId());
                        try (MongoCursor<Document> cursor = collection.find()
                                .projection(projectionFields)
                                .sort(Sorts.descending("username")).iterator()) {
                            while (cursor.hasNext()) {
                                Document doc = cursor.next();
                                if (usersId.contains(doc.getString("userid"))) {

                                    currUsers.add(doc.getString("username") + "\n");
                                    currCredit.add(".\u3000\u3000\u3000" + doc.getInteger("score").toString() + "\n");
                                }
                            }
                            for(String s : currUsers)
                                sbUser.append(s);
                            for(String s: currCredit)
                                sbCredit.append(s);

                            eb.addField("Username  统一社会信用代码 ", sbUser.toString(), true);
                            eb.addField("Social Credit 社会信用评分", sbCredit.toString(), true);
                            event.getChannel().sendMessage(eb.build()).queue();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }

                }
            }
            // !show <user tag>
            else if (msg[1].substring(0,1).equalsIgnoreCase("<")){
                userid = event.getMessage().getContentRaw().substring(9,event.getMessage().getContentRaw().length()-1);
                try (MongoClient mongoClient = MongoClients.create(settings)) {
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
                            event.getChannel().sendMessage("Does not exist").queue();
                            System.err.println("DNE");
                        } else {
                            event.getChannel().sendMessage("User: " + doc.getString("username") + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }
            }
            // !show name
            else{
                String username = event.getMessage().getContentRaw().substring(6);
                System.out.println(username);
                try (MongoClient mongoClient = MongoClients.create(settings)) {
                    MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                    MongoCollection<Document> collection = database.getCollection("socialcredit");
                    try {
                        Bson projectionFields = Projections.fields(
                                Projections.include("username", "score"),
                                Projections.excludeId());
                        Document doc = collection.find(eq("username", username))
                                .projection(projectionFields)
                                .first();
                        if (doc == null) {
                            System.err.println("DNE");
                        } else {
                            event.getChannel().sendMessage("User: " + doc.getString("username") + "\nSocial Credit: " + doc.getInteger("score")).complete();
                        }
                    } catch (MongoException me) {
                        System.err.println("ERROR");
                    }
                }
            }
        }
    }
}
