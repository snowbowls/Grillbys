import events.*;
import net.dv8tion.jda.api.JDABuilder;


public class Bot {

    public static final String TOKEN = System.getenv("TOKEN");

    public static void main(String[] args) throws Exception{

        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        builder.addEventListeners(new AddReactEvent());
        builder.addEventListeners(new RemoveReactEvent());
        builder.addEventListeners(new CommandEvent());
        builder.build();


    }
}
