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

@Component
@RequiredArgsConstructor
public class ServerStartupNotifier {

    private final WebServerApplicationContext context;
    private final DiscordAdapter discordAdapter;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void serverStartUp() throws UnknownHostException {
//        String[] activeProfiles = environment.getActiveProfiles();
//        boolean isLocal = Arrays.asList(activeProfiles).contains("local");
//        if (isLocal) return;

        String hostname = InetAddress.getLocalHost().getHostName();
        discordAdapter.sendMessageToChannelServerStart(hostname, "production", System.getProperty("java.version"), String.valueOf(context.getWebServer().getPort()));
    }

}
