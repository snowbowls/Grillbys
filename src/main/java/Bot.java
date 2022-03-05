import events.*;
import net.dv8tion.jda.api.JDABuilder;
import io.github.cdimascio.dotenv.Dotenv;



public class Bot{

   // public static final String TOKEN = System.getenv("TOKEN");
    public static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) throws Exception{

        String TOKEN = dotenv.get("TOKEN");

        JDABuilder builder = JDABuilder.createDefault(TOKEN);

        // Credit Score Events
        builder.addEventListeners(new AddReactEvent());
        builder.addEventListeners(new RemoveReactEvent());
        builder.addEventListeners(new ShowCommandEvent());

        // Other Events
        builder.addEventListeners(new HelpEvent());
        //builder.addEventListeners(new TagUserEvent());
        //builder.addEventListeners(new JonEvent());

        builder.build();
    }
}