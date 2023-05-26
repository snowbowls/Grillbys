package events;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.bson.Document;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContribroEvent extends ListenerAdapter {
    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String msg = event.getMessage().getContentRaw();
        if(msg.length() > 7)
            if(!msg.substring(0,7).equalsIgnoreCase("!contra"))
                return;

        if (event.getMessage().getContentRaw().equalsIgnoreCase("!contra")) {
            System.out.println("Contributor summoned by " + event.getAuthor().getName());
            int itemCnt = 0;
            List<String> options = new ArrayList<>();

            // Database connection
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            // Create a new EmbedBuilder and set the title and color
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Inventory");
            builder.setColor(Color.magenta);

            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("Contri");
                try {
                    FindIterable<Document> documents = collection.find();


                    for (Document document : documents) {
                        int cnt = document.getInteger("cnt");
                        itemCnt = cnt;
                        int i = 0;
                        // Iterate over each key in the document
                        for (String key : document.keySet()) {
                            // Retrieve the value associated with the key
                            Object value = document.get(key);
                            List<String> itemArray = new ArrayList<>();
                            String itemName;

                            // Check if the value is an array
                            if (value instanceof List) {
                                itemName = key;
                                options.add(key);

                                // Iterate over each element in the array
                                List<?> array = (List<?>) value;
                                for (Object element : array) {
                                    itemArray.add(element.toString());
                                }
                                String names = String.join(", ", itemArray);
                                builder.addField(itemName, names, false);
                                itemArray.clear();
                                i = i + 1;
                            }
                        }
                    }
                    MessageCreateData data = new MessageCreateBuilder()
                            .addEmbeds(builder.build())
                            .build();


                    int actionRowCnt = itemCnt % 5;
                    if (itemCnt > 5) {
                        Button[] buttons1 = new Button[itemCnt - (itemCnt % 5)];
                        for (int i = 0; i < itemCnt - (itemCnt % 5); i++) {
                            buttons1[i] = Button.primary(options.get(i), options.get(i));
                        }
                        Button[] buttons2 = new Button[actionRowCnt];
                        for (int i = 0; i < actionRowCnt; i++) {
                            buttons2[i] = Button.primary(options.get(i + 5), options.get(i + 5));
                        }
                        ActionRow actionRow2 = ActionRow.of(buttons2);

                        event.getChannel().sendMessageEmbeds(builder.build())
                                .addComponents(ActionRow.of(buttons1), actionRow2)
                                .complete();
                    }
                    else{
                        Button[] buttons1 = new Button[itemCnt];
                        for (int i = 0; i < itemCnt; i++) {
                            buttons1[i] = Button.primary(options.get(i), options.get(i));
                        }

                        event.getChannel().sendMessageEmbeds(builder.build())
                                .addComponents(ActionRow.of(buttons1))
                                .complete();
                    }

                } catch (MongoException me) {
                    System.err.println("ERROR");
                }

                return;

            }

        }

        String command = null;
        try{
            command = msg.substring(7);
        }catch (StringIndexOutOfBoundsException e){
            return;
        }
        if (command.substring(1,4).equalsIgnoreCase("add")) {
            System.out.println("Inventory add: " + command.substring(5));
            // Database connection
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();


            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("Contri");
                collection.updateMany(new Document(), Updates.inc("cnt", 1));

                // Iterate over the documents in the collection
                FindIterable<Document> documents = collection.find();
                for (Document document : documents) {
                    // Create a new list to hold the updated array
                    List<String> updatedArray = new ArrayList<>();

                    // Update the document with the updated array
                    document.put(command.substring(5), updatedArray);

                    // Save the updated document back to the collection
                    collection.replaceOne(new Document("_id", document.get("_id")), document);
                }
                event.getChannel().sendMessage("Entry added!").complete();
                return;
            }

        }

        if (command.substring(1,4).equalsIgnoreCase("del")) {
            System.out.println("Inventory del: " + command.substring(5));
            // Database connection
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            // Create a new EmbedBuilder and set the title and color
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Inventory");
            builder.setColor(Color.magenta);

            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("Contri");

                // Iterate over the documents in the collection
                String fieldName = command.substring(5);
                try{

                    Document document = collection.find().first();
                    if (document != null && document.containsKey(fieldName)) {
                        collection.updateMany(new Document(), Updates.unset(fieldName));
                        collection.updateMany(new Document(), Updates.inc("cnt", -1));

                        event.getChannel().sendMessage("Entry deleted!").complete();
                    }
                    else{
                        event.getChannel().sendMessage("Entry not found!").complete();
                    }

                }catch(Exception e){
                    System.out.println("FAILED");
                    System.out.println(e);
                }



            }

        }

        if (command.substring(1,6).equalsIgnoreCase("clear")) {
            // Database connection
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            try (MongoClient mongoClient = MongoClients.create(settings)) {
                MongoDatabase database = mongoClient.getDatabase("ChillGrill");
                MongoCollection<Document> collection = database.getCollection("Contri");

                // Delete all documents in the collection
                collection.deleteMany(new Document());

                // Create a new document with the "cnt" field set to 0
                Document newDocument = new Document("cnt", 0);

                // Insert the new document into the collection
                collection.insertOne(newDocument);
                event.getChannel().sendMessage("Contra has been reset!").complete();
                System.out.println("Contra reset!");
            }


        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        event.deferEdit().queue();

        int cnt = 0;
        List<String> options = new ArrayList<>();
        Boolean present = false;

        // Database connection
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        List<String> combinedValuesList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("ChillGrill");
            MongoCollection<Document> collection = database.getCollection("Contri");
            String arrayField = event.getComponentId();
            String toAdd = event.getUser().getName();


            FindIterable<Document> documents = collection.find();
            for (Document document : documents) {
                List<String> names = (List<String>) document.get(arrayField);

                if(names.contains(toAdd))
                    present = true;

                if (!present){
                    collection.updateMany(new Document(), Updates.push(arrayField, toAdd));
                    System.out.println("User " + toAdd + " added to entry: " + event.getComponentId());
                }
                else{
                    collection.updateMany(new Document(), new Document("$pull", new Document(arrayField, toAdd)));
                    System.out.println("User " + toAdd + " removed from entry: " + event.getComponentId());
                }


            }

            Document document = collection.find().first();
            cnt = document.getInteger("cnt");

            // Extract values from the string arrays and store them in a String list

            // Process each array field
            for (String key : document.keySet()) {

                Object value = document.get(key);
                if (value instanceof List) {
                    options.add(key);
                    List<String> stringArray = (List<String>) value;
                    String combinedValues = String.join(", ", stringArray);
                    combinedValuesList.add(combinedValues);
                }
            }
        }

        // Get the poll question and options from the message
        MessageEmbed originalEmbed = event.getMessage().getEmbeds().get(0);
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(originalEmbed.getTitle())
                .setDescription(originalEmbed.getDescription())
                .setColor(originalEmbed.getColor());

        int index = 0;
        for (MessageEmbed.Field field : originalEmbed.getFields()) {
            builder.addField(field.getName(), combinedValuesList.get(index), false);
            index = index + 1;
        }
        
        int itemCnt = cnt;
        int actionRowCnt = itemCnt % 5;
        if (itemCnt > 5) {
            Button[] buttons1 = new Button[itemCnt - (itemCnt % 5)];
            for (int i = 0; i < itemCnt - (itemCnt % 5); i++) {
                buttons1[i] = Button.primary(options.get(i), options.get(i));
            }
            Button[] buttons2 = new Button[actionRowCnt];
            for (int i = 0; i < actionRowCnt; i++) {
                buttons2[i] = Button.primary(options.get(i + 5), options.get(i + 5));
            }
            ActionRow actionRow2 = ActionRow.of(buttons2);

            MessageCreateData data = new MessageCreateBuilder()
                    .addEmbeds(builder.build())
                    .build();
            event.editMessage(MessageEditData.fromCreateData(data))
                    .setComponents(ActionRow.of(buttons1), actionRow2)
                    .complete();
        }
        else{
            Button[] buttons1 = new Button[itemCnt];
            for (int i = 0; i < itemCnt; i++) {
                buttons1[i] = Button.primary(options.get(i), options.get(i));
            }

            MessageCreateData data = new MessageCreateBuilder()
                    .addEmbeds(builder.build())
                    .build();
            event.getMessage().editMessage(MessageEditData.fromCreateData(data))
                    .setComponents(ActionRow.of(buttons1))
                    .queue();
        }
    }
}
