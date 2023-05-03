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
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserStatEvent extends ListenerAdapter {
    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReceived(MessageReceivedEvent event) {
        // Get author name, parse message for processing
        String userid = event.getMessage().getAuthor().getId();
        String fullmsg = event.getMessage().getContentRaw();
        String[] msg = fullmsg.split(" ");


        // Database connection
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("badwords.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] words = reader.lines().toArray(String[]::new);

        boolean containsWord = false;

        // Check if the input string contains any of the words from the array
        for (String word : words) {
            String regex = "\\b" + word + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fullmsg);
            if (matcher.find()) {
                containsWord = true;
                break;
            }
        }
        if (containsWord) {
            System.out.println("The input string contains a word from the text file");
        } else {
            System.out.println("The input string does not contain any words from the text file");
        }

        if(fullmsg.equalsIgnoreCase("tryme")) {
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
                MongoCollection<Document> collection = database.getCollection("UserStats");
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
                        for (String s : currUsers)
                            sbUser.append(s);
                        for (String s : currCredit)
                            sbCredit.append(s);

                        eb.addField("Username  统一社会信用代码 ", sbUser.toString(), true);
                        eb.addField("Social Credit 社会信用评分", sbCredit.toString(), true);
                        MessageCreateData data = new MessageCreateBuilder()
                                .addEmbeds(eb.build())
                                .build();
                        event.getChannel().sendMessage(data).queue();
                    }
                } catch (MongoException me) {
                    System.err.println("ERROR");
                }

            }
        }
    }
}
