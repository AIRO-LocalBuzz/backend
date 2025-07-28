package backend.airo.infra.discord.message.convertor;

import backend.airo.infra.discord.message.DiscordEmbeddable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class DiscordMessageConverter {

    private DiscordMessageConverter() {
        throw new UnsupportedOperationException("해당 클래스는 유틸성 클래스입니다. [ 생성 금지 ]");
    }

    private static final ZoneId KOREAN_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static MessageEmbed buildReportMessage(
            DiscordEmbeddable message
    ) {
        String reportTime = TIME_FORMATTER.format(Instant.now().atZone(KOREAN_ZONE));
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(message.getTitle())
                .setDescription(message.getDescription())
                .addField("\u200B", "\u200B", false);

        for (Map.Entry<String, String> field : message.getFields().entrySet()) {
            builder.addField(field.getKey(), field.getValue(), true);
        }

        builder
                .addField("실행 시각", "`" + reportTime + " (KST)`", true)
                .addField("\u200B", "\u200B", false)
                .setFooter("AIRO 관리 시스템")
                .setTimestamp(Instant.now())
                .setColor(0xFF6B6B);

        return builder.build();
    }
}
