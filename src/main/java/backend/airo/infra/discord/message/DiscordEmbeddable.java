package backend.airo.infra.discord.message;

import java.util.Map;

public interface DiscordEmbeddable {

    String getTitle();
    String getDescription();
    Map<String, String> getFields();

}
