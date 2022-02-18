import events.HelloEvent;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class bot {
    public static final String TOKEN = System.getenv("TOKEN");

    public static void main(String[] args) throws Exception{

        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        builder.addEventListeners(new HelloEvent());
        builder.build();


    }
}
