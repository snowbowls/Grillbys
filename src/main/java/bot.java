import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class bot {

    public static void main(String[] args) throws Exception{

        JDABuilder builder = JDABuilder.createDefault("ree");
        builder.build();

    }
}
