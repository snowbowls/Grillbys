package events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZabaEvent extends ListenerAdapter {
    // ZabaEvents are for practical functions or utilities that the bot can execute in the guild
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
        int second = with.get(Calendar.SECOND);
        int delayInDays = dayToDelay.get(dayOfWeek);
        int delayInHours ;
        int delayInSeconds ;
        if(delayInDays == 6 && hour<scheduleHour){
            delayInHours = scheduleHour - hour;
            delayInSeconds = delayInHours*60 - second;
        }else{
            delayInHours = delayInDays*24+((24-hour)+scheduleHour);
            delayInSeconds = delayInHours*60 - second;
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

        if(Math.random() < .99) {
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

        JSONObject moods = (JSONObject) jsonObject.get("mood");
        int max = moods.size();
        int min = 1;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        String rng = String.valueOf(15);
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
    public void birthdayPosting(){}

}
