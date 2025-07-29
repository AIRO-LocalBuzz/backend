package backend.airo.support;


import backend.airo.infra.discord.adapter.DiscordAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ServerStartupNotifier {

    private final WebServerApplicationContext context;
    private final DiscordAdapter discordAdapter;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void serverStartUp() throws UnknownHostException {
//        if (checkProfile(environment)) return;
        String hostname = InetAddress.getLocalHost().getHostName();
        discordAdapter.sendMessageToChannelServerStart(hostname, "production", System.getProperty("java.version"), String.valueOf(context.getWebServer().getPort()));
    }

    public void collectClutrFatvlDataSuccessWithNotification (int size, LocalDate start, LocalDate end){
//        if (checkProfile(environment)) return;

        discordAdapter.sendMessageToChannelCollectClutrFatvlDataSuccess(size, start, end);
    }

    public void collectClutrFatvlDataFailWithNotification(){
//        if (checkProfile(environment)) return;
        discordAdapter.sendMessageToChannelCollectClutrFatvlDataFail();
    }

    private static boolean checkProfile(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        return Arrays.asList(activeProfiles).contains("local");
    }

}
