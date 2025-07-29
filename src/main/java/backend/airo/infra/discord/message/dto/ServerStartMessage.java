package backend.airo.infra.discord.message.dto;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ServerStartMessage implements DiscordEmbeddable {
    private final String host;
    private final String profile;
    private final String javaVersion;
    private final String port;

    @Override
    public String getTitle() {
        return "서버 관리";
    }

    @Override
    public String getDescription() {
        return "서버가 정상적으로 배포 되었습니다.";
    }

    @Override
    public Map<String, String> getFields() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("호스트", "**호스트:** `" + host + "`");
        fields.put("환경", "**Profile:** `" + profile + "`");
        fields.put("JVM", "**Java Version:** `" + javaVersion + "`");
        fields.put("포트", "**Port:** `" + port + "`");
        return fields;
    }
}
