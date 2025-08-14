package backend.airo.infra.discord.adapter;

import backend.airo.infra.discord.message.convertor.DiscordMessageConverter;
import backend.airo.infra.discord.message.dto.CollectClutrFatvlDataFailMessage;
import backend.airo.infra.discord.message.dto.CollectClutrFatvlDataSuccessMessage;
import backend.airo.infra.discord.message.dto.ServerStartMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiscordAdapter{

    private final JDA jda;
    @Value("${discord.bot.channel_id}")
    private String channelId;

    public void sendMessageToChannelServerStart(String host, String profile, String javaVersion, String port) {
        TextChannel textChannel = jda.getTextChannelById(channelId);
        MessageEmbed buildEmbedReportMessage = DiscordMessageConverter.buildReportMessage(
                new ServerStartMessage(host, profile, javaVersion, port)
        );
        textChannel.sendMessageEmbeds(buildEmbedReportMessage).queue();
    }

    public void sendMessageToChannelCollectClutrFatvlDataSuccess(long totalRead, long totalWrite, long totalSkip, LocalDate start, LocalDate end, double tookSec) {
        TextChannel textChannel = jda.getTextChannelById(channelId);
        MessageEmbed buildEmbedReportMessage = DiscordMessageConverter.buildReportMessage(
                new CollectClutrFatvlDataSuccessMessage(totalRead, totalWrite, totalSkip, start, end, tookSec)
        );
        textChannel.sendMessageEmbeds(buildEmbedReportMessage).queue();
    }

    public void sendMessageToChannelCollectClutrFatvlDataFail(String message) {
        TextChannel textChannel = jda.getTextChannelById(channelId);
        MessageEmbed buildEmbedReportMessage = DiscordMessageConverter.buildReportMessage(
                new CollectClutrFatvlDataFailMessage(message)
        );
        textChannel.sendMessageEmbeds(buildEmbedReportMessage).queue();
    }

}
