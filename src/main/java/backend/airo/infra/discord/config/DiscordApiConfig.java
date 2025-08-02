package backend.airo.infra.discord.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordApiConfig {

    @Bean
    JDA jda(@Value("${discord.bot.token}") String token) throws InterruptedException {
        return JDABuilder.createDefault(token)
                .disableCache(CacheFlag.VOICE_STATE)
                .disableCache(CacheFlag.EMOJI)
                .disableCache(CacheFlag.CLIENT_STATUS)
                .disableCache(CacheFlag.STICKER)
                .disableCache(CacheFlag.SCHEDULED_EVENTS)
                .setEnabledIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .build()
                .awaitReady();
    }
}
