import events.*;
import net.dv8tion.jda.api.JDABuilder;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;


public class Bot{

    public static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) throws Exception {

        String TOKEN = dotenv.get("TOKEN");

        JDABuilder.createDefault(TOKEN)
                // JDA Builder Properties
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)

                // Credit Score Events
                .addEventListeners(new AddReactEvent())
                .addEventListeners(new RemoveReactEvent())
                .addEventListeners(new ShowCommandEvent())

                // Other Events
                .addEventListeners(new GenResponseEvent())
                .addEventListeners(new JonEvent())
                .addEventListeners(new ZabaEvent())

                .build();
    }
}