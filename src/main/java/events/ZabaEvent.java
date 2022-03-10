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
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZabaEvent extends ListenerAdapter {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();
    }
    // Scheduler
    public void onReady(@NotNull ReadyEvent event) {

        // get the current ZonedDateTime of your TimeZone
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("US/Eastern"));

        // set the ZonedDateTime of the first lesson at x:xx
        ZonedDateTime nextFirstLesson = now.withHour(10).withMinute(23).withSecond(30);

        // if it's already past the time (in this case x:xx) the first lesson will be scheduled for the next day
        if (now.compareTo(nextFirstLesson) > 0) {
            nextFirstLesson = nextFirstLesson.plusDays(1);
        }

        // duration between now and the beginning of the next first lesson
        Duration durationUntilFirstLesson = Duration.between(now, nextFirstLesson);
        // in seconds
        long initialDelayFirstLesson = durationUntilFirstLesson.getSeconds();

        // schedules the reminder at a fixed rate of one day
        ScheduledExecutorService schedulerFirstLesson = Executors.newScheduledThreadPool(1);
        schedulerFirstLesson.scheduleAtFixedRate(() -> {
                    // execute
                    JDA jda = event.getJDA();
                    moodPosting(jda);
                },
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
        String rng = String.valueOf(rand);
        JSONObject mood =(JSONObject) moods.get(rng);

        String str = mood.keySet().toString();
        String key = str.substring(1, str.length() - 1);

        Guild guild = jda.getGuildById("944254135476305980");

        assert guild != null;
        Objects.requireNonNull(guild.getDefaultChannel()).sendMessage(key).addFile(new File("videos/" + mood.get(key).toString())).queue();
    }
    public void birthdayPosting(){}
    public void fridayPosting(){}
}
