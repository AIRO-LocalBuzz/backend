package backend.airo.infra.discord.message.dto;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
public class CollectClutrFatvlDataFailMessage implements DiscordEmbeddable {

    @Override
    public String getTitle() {
        return "서버 관리";
    }

    @Override
    public String getDescription() {
        return "축제/행사 데이터 배치 처리 실패";
    }

    @Override
    public Map<String, String> getFields() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("실패 사유", "**사유:** `" + "수집된 데이터가 없습니다." + "`");
        return fields;
    }
}
