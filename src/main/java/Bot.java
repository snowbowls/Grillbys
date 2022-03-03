import events.*;
import net.dv8tion.jda.api.JDABuilder;
import io.github.cdimascio.dotenv.Dotenv;



public class Bot{

    static Dotenv dotenv = Dotenv.load();


    public static void main(String[] args) throws Exception{

        JDABuilder builder = JDABuilder.createDefault(dotenv.get("TOKEN")
        );

        // Credit Score Events
        builder.addEventListeners(new AddReactEvent());
        builder.addEventListeners(new RemoveReactEvent());
        builder.addEventListeners(new ShowCommandEvent());

        // Other Events
        builder.addEventListeners(new HelpEvent());
        builder.addEventListeners(new TagUserEvent());
        builder.addEventListeners(new JonEvent());

        builder.build();
    }
}
