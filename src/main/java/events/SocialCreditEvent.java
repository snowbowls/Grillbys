package events;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class SocialCreditEvent extends ListenerAdapter {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

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
                                .sort(Sorts.descending("score")).iterator()) {
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
                        System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " --> Reactor: " + reactor + " @" + event.getChannel().getName());
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
                        System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " --> Reactor: " + reactor + " @" + event.getChannel().getName());
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
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

        Message msg = event.retrieveMessage().complete();
        String username = msg.getAuthor().getName();
        String userid = msg.getAuthor().getId();
        String reactor = Objects.requireNonNull(event.getUser()).getName();
        boolean isCustom;

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
            {
                // Trigger when message rem react +15
                if (event.getReactionEmote().getId().equals("900119408859578451") && !username.equals(reactor)) {
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
                            System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " --> Reactor: " + reactor + " @" + event.getChannel().getName());
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
                if (event.getReactionEmote().getId().equals("934919187787288597") && !username.equals(reactor)) {
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
                            System.out.println(doc.get("username") + "-> Old: " + currVal + " New " + doc.getInteger("score") + " --> Reactor: " + reactor + " @" + event.getChannel().getName());
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
    }
}
