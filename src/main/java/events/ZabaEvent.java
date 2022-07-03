package events;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public class ZabaEvent extends ListenerAdapter {
    // ZabaEvents are for practical functions or utilities that the bot can execute in the guild

    public static Dotenv dotenv = Dotenv.load();
    String uri = dotenv.get("URI");

    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();

        if(msg.equals("echo")){
//            JSONParser parser = new JSONParser();
//            JSONObject test = null;
//            JSONObject jsonObject;
//            try {
//                Object obj = parser.parse(new FileReader("keywords.json"));
//                jsonObject = (JSONObject) obj;
//                test = (JSONObject) jsonObject.get("test");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            assert test != null;
//            Set<String> keys = test.keySet();
//            for (String f : keys) {
//                System.out.println(f);
//            }
        }
    }
    // Scheduler
    public void onReady(@NotNull ReadyEvent event) {
        fridayScheduling(event.getJDA());
        moodScheduler(event.getJDA());
        birthScheduler(event.getJDA());
        statusSet(event.getJDA());
        //creditCheckScheduler(event.getJDA());
    }
    public void statusSet(JDA jda){
        jda.getPresence().setActivity(Activity.watching("you"));
    }
    public void creditCheckScheduler(JDA jda){
        // get the current ZonedDateTime of your TimeZone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("US/Eastern"));
        // set the ZonedDateTime of the first lesson at 8:05
        ZonedDateTime nextFirstLesson = now.withHour(8).withMinute(0).withSecond(0);
        // if it's already past the time (in this case 8:05) the first lesson will be scheduled for the next day
        if (now.compareTo(nextFirstLesson) > 0) {
            nextFirstLesson = nextFirstLesson.plusDays(1);
        }
        // duration between now and the beginning of the next first lesson
        Duration durationUntilFirstLesson = Duration.between(now, nextFirstLesson);
        // in seconds
        long initialDelayFirstLesson = durationUntilFirstLesson.getSeconds();
        // schedules the reminder at a fixed rate of one day
        ScheduledExecutorService schedulerFirstLesson = Executors.newScheduledThreadPool(1);
        schedulerFirstLesson.scheduleAtFixedRate(() -> creditCheck(jda),
                initialDelayFirstLesson,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }
    public void creditCheck(JDA jda) {
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        System.out.println("* * * * * CREDIT CHECK * * * *");
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            JSONObject jsonObject = (JSONObject) obj;
            response = (JSONObject) jsonObject.get("socialCredit");

        } catch (Exception e) {
            e.printStackTrace();
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
                        int credit = doc.getInteger("score");
                        if (credit == 150) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("250").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 150!");
                            System.out.println("************************************************************");
                        }
                        if (credit == 300) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("250").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 300!");
                            System.out.println("************************************************************");
                        }
                        if (credit == 600) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("500").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 600!");
                            System.out.println("************************************************************");
                        }
                        if (credit == 750) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("750").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 750!");
                            System.out.println("************************************************************");
                        }
                        if (credit == 840) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("850").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 840!");
                            System.out.println("************************************************************");
                        }
                        if (credit == 990) {
                            String userid = doc.getString("userid");
                            JSONObject finalResponse = response;
                            Objects.requireNonNull(jda.getUserById(userid)).openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage(finalResponse.get("1000").toString()))
                                    .queue();
                            System.out.println("************************************************************");
                            System.out.println(doc.getString("username") + "reached 990!");
                            System.out.println("************************************************************");
                        }
                    }
                }
            } catch (MongoException me) {
                System.err.println("ERROR");
            }
        }
    }
    public void fridayScheduling(JDA jda){
        int scheduleHour = 8;

        Date aDate = new Date();
        Calendar with = Calendar.getInstance();
        with.setTime(aDate);
        Map<Integer, Integer> dayToDelay = new HashMap<>();
        dayToDelay.put(Calendar.FRIDAY, 6);
        dayToDelay.put(Calendar.SATURDAY, 5);
        dayToDelay.put(Calendar.SUNDAY, 4);
        dayToDelay.put(Calendar.MONDAY, 3);
        dayToDelay.put(Calendar.TUESDAY, 2);
        dayToDelay.put(Calendar.WEDNESDAY, 1);
        dayToDelay.put(Calendar.THURSDAY, 0);
        int dayOfWeek = with.get(Calendar.DAY_OF_WEEK);
        int hour = with.get(Calendar.HOUR_OF_DAY);
        int minute = with.get(Calendar.MINUTE);
        int second = with.get(Calendar.SECOND);
        int delayInDays = dayToDelay.get(dayOfWeek);
        int delayInHours;
        int delayInMinutes;
        int delayInSeconds;
        if(delayInDays == 6 && hour<scheduleHour){
            delayInHours = (scheduleHour - hour) - 1;
            delayInMinutes = ((60 - minute) + (delayInHours * 60));
            delayInSeconds = (delayInMinutes*60 - second);;
        }else{
            delayInHours = (delayInDays*24+((24-hour)+scheduleHour)) - 1;
            delayInMinutes = (60 - minute) + (delayInHours * 60);
            delayInSeconds = (delayInMinutes*60 - second);
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                fridayPosting(jda);
            } catch (Exception ex) {
                ex.printStackTrace(); // or loggger would be better
            }
        }, delayInSeconds, 604800, TimeUnit.SECONDS);
    }
    public void fridayPosting(JDA jda){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert jsonObject != null;
        JSONObject friday = (JSONObject) jsonObject.get("friday");
        int max = friday.size();
        int min = 1;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        String rng = String.valueOf(rand);
        String meme= friday.get(rng).toString();

        if(Math.random() < .20) {
            Objects.requireNonNull(jda.getTextChannelById("944254315630035005")).sendMessage("Behold, everyone! \nIt's **Friday**").addFile(new File("videos/friday/" + meme)).queue();
            Objects.requireNonNull(jda.getTextChannelById("816125354875944964")).sendMessage("Behold, everyone! \nIt's **Friday**").addFile(new File("videos/friday/" + meme)).queue();
            Objects.requireNonNull(jda.getTextChannelById("165246172892495872")).sendMessage("Behold, everyone! \nIt's **Friday**").addFile(new File("videos/friday/" + meme)).queue();
            System.out.println("------------------- Friday: " + meme);
        }
        else{
            System.out.println("------------------- No Friday for today");
        }
    }
    public void moodScheduler(JDA jda){
        // get the current ZonedDateTime of your TimeZone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("US/Eastern"));
        // set the ZonedDateTime of the first lesson at 8:05
        ZonedDateTime nextFirstLesson = now.withHour(11).withMinute(0).withSecond(0);
        // if it's already past the time (in this case 8:05) the first lesson will be scheduled for the next day
        if (now.compareTo(nextFirstLesson) > 0) {
            nextFirstLesson = nextFirstLesson.plusDays(1);
        }
        // duration between now and the beginning of the next first lesson
        Duration durationUntilFirstLesson = Duration.between(now, nextFirstLesson);
        // in seconds
        long initialDelayFirstLesson = durationUntilFirstLesson.getSeconds();
        // schedules the reminder at a fixed rate of one day
        ScheduledExecutorService schedulerFirstLesson = Executors.newScheduledThreadPool(1);
        schedulerFirstLesson.scheduleAtFixedRate(() -> moodPosting(jda),
                initialDelayFirstLesson,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }
    public void moodPosting(JDA jda){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert jsonObject != null;

        JSONObject moods = (JSONObject) jsonObject.get("moods");
        int max = moods.size();
        int min = 1;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        String rng = String.valueOf(rand);
        JSONObject mood =(JSONObject) moods.get(rng);

        String str = mood.keySet().toString();
        String key = str.substring(1, str.length() - 1);

        Guild guild = jda.getGuildById("816125354875944960");
        assert guild != null;
        if(Math.random() < .15) {
            Objects.requireNonNull(jda.getTextChannelById("816125354875944964")).sendMessage("Behold, everyone! \nToday's mood is: **" + key + "**").addFile(new File("videos/moods/" + mood.get(key).toString())).queue();
            System.out.println("------------------- Mood: " + key);
        }
        else{
            System.out.println("------------------- No mood for today");
        }
    }
    public void birthScheduler(JDA jda){
        // get the current ZonedDateTime of your TimeZone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("US/Eastern"));
        // set the ZonedDateTime of the first lesson at 8:05
        ZonedDateTime nextFirstLesson = now.withHour(8).withMinute(0).withSecond(0);
        // if it's already past the time (in this case 8:05) the first lesson will be scheduled for the next day
        if (now.compareTo(nextFirstLesson) > 0) {
            nextFirstLesson = nextFirstLesson.plusDays(1);
        }
        // duration between now and the beginning of the next first lesson
        Duration durationUntilFirstLesson = Duration.between(now, nextFirstLesson);
        // in seconds
        long initialDelayFirstLesson = durationUntilFirstLesson.getSeconds();
        // schedules the reminder at a fixed rate of one day
        ScheduledExecutorService schedulerFirstLesson = Executors.newScheduledThreadPool(1);
        schedulerFirstLesson.scheduleAtFixedRate(() -> birthdayPosting(jda),
                initialDelayFirstLesson,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }
    public void birthdayPosting(JDA jda) {
        String chatID = "816125354875944964";
        String today = LocalDate.now().toString().substring(5);
        //System.out.println("Today's Date: " + today);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        // Load JSON
        JSONObject dates = null;
        JSONObject poi = null;
        try {
            Object obj = parser.parse(new FileReader("keywords.json"));
            jsonObject = (JSONObject) obj;
            dates = (JSONObject) jsonObject.get("birthdays");
            poi = (JSONObject) jsonObject.get("poi");

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert dates != null;
        assert poi != null;
        Set<String> birthDates = dates.keySet();
        if(birthDates.contains(today)) {
            Objects.requireNonNull(jda.getTextChannelById(chatID)).sendMessage("Behold, everyone!").queue();
            String id = poi.get(dates.get(today)).toString();
            StringBuilder mention = new StringBuilder();
            mention.append("<@");
            mention.append(id);
            mention.append(">");

            if(dates.get(today).toString().equals("Jon")){

                Objects.requireNonNull(jda.getTextChannelById(chatID)).sendMessage(":green_circle: Today is " + mention + "'s birthday! :purple_circle:").queue();
                Objects.requireNonNull(jda.getTextChannelById(chatID)).sendMessage("\n⣿⣿⠿⠿⠿⠿⣿⣷⣂⠄⠄⠄⠄⠄⠄⠈⢷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⣷⡾⠯⠉⠉⠉⠉⠚⠑⠄⡀⠄⠄⠄⠄⠄⠈⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡀⠄⠄⠄⠄⠄⠄⠄⠄⠉⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⠎⠄⠄⣀⡀⠄⠄⠄⠄⠄⠄⠄⠘⠋⠉⠉⠉⠉⠭⠿⣿\n" +
                        "⡀⠄⠄⠄⠄⠄⠄⠄⠄⡇⠄⣠⣾⣳⠁⠄⠄⢺⡆⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄\n" +
                        "⣿⣷⡦⠄⠄⠄⠄⠄⢠⠃⢰⣿⣯⣿⡁⢔⡒⣶⣯⡄⢀⢄⡄⠄⠄⠄⠄⠄⣀⣤⣶\n" +
                        "⠓⠄⠄⠄⠄⠄⠸⠄⢀⣤⢘⣿⣿⣷⣷⣿⠛⣾⣿⣿⣆⠾⣷⠄⠄⠄⠄⢀⣀⣼⣿\n" +
                        "⠄⠄⠄⠄⠄⠄⠄⠑⢘⣿⢰⡟⣿⣿⣷⣫⣭⣿⣾⣿⣿⣴⠏⠄⠄⢀⣺⣿⣿⣿⣿\n" +
                        "⣿⣿⣿⣿⣷⠶⠄⠄⠄⠹⣮⣹⡘⠛⠿⣫⣾⣿⣿⣿⡇⠑⢤⣶⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⣿⣿⣿⣯⣤⣤⣤⣤⣀⣀⡹⣿⣿⣷⣯⣽⣿⣿⡿⣋⣴⡀⠈⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⣝⡻⢿⣿⡿⠋⡒⣾⣿⣧⢰⢿⣿⣿⣿⣿⣿⣿⣿\n" +
                        "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⣏⣟⣼⢋⡾⣿⣿⣿⣘⣔⠙⠿⠿⠿⣿⣿⣿\n" +
                        "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⣛⡵⣻⠿⠟⠁⠛⠰⠿⢿⠿⡛⠉⠄⠄⢀⠄⠉⠉⢉\n" +
                        "⣿⣿⣿⣿⡿⢟⠩⠉⣠⣴⣶⢆⣴⡶⠿⠟⠛⠋⠉⠩⠄⠉⢀⠠⠂⠈⠄⠐⠄⠄⠄").queue();
            }
            
            else{
                Objects.requireNonNull(jda.getTextChannelById(chatID)).sendMessage("Today is " + mention + "'s birthday!").queue();
            }
        }
    }
}
