package backend.airo.infra.discord.adapter;

import backend.airo.infra.discord.message.convertor.DiscordMessageConverter;
import backend.airo.infra.discord.message.dto.ServerStartMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordAdapter{

    private static final Logger logger = LoggerFactory.getLogger(DiscordAdapter.class);

    private final JDA jda;
    @Value("${discord.bot.channel_id}")
    private String channelId;

    public void sendMessageToChannelServerStart(String host, String profile, String javaVersion, String port) {
        logger.info("Discord Adapter 호출");
        TextChannel textChannel = jda.getTextChannelById(channelId);
        MessageEmbed buildEmbedReportMessage = DiscordMessageConverter.buildReportMessage(
                new ServerStartMessage(host, profile, javaVersion, port)
        );
        textChannel.sendMessageEmbeds(buildEmbedReportMessage).queue();
    }

    public void sendMessageToChannelServerShutdown(String host, String profile, String javaVersion, String port, String message) {
        logger.info("Discord Adapter 호출");
        TextChannel textChannel = jda.getTextChannelById(channelId);
        MessageEmbed buildEmbedReportMessage = DiscordMessageConverter.buildReportMessage(
                new ServerStartMessage(host, profile, javaVersion, port)
        );
        textChannel.sendMessageEmbeds(buildEmbedReportMessage).queue();
    }

}
