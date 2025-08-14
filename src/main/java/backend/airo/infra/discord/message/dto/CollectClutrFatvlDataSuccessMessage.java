package backend.airo.infra.discord.message.dto;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
public class CollectClutrFatvlDataSuccessMessage  implements DiscordEmbeddable {
    private final Long totalRead;
    private final Long totalWrite;
    private final Long totalSkip;
    private final LocalDate start;
    private final LocalDate end;
    private final Double tookSec;

    @Override
    public String getTitle() {
        return "서버 관리";
    }

    @Override
    public String getDescription() {
        return "축제/행사 데이터 배치 처리 완료";
    }

    @Override
    public Map<String, String> getFields() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("수집 데이터", "**EA:** `" + totalRead + "`");
        fields.put("저장된 데이터", "**EA:** `" + totalWrite + "`");
        fields.put("스킵된 데이터", "**EA:** `" + totalSkip + "`");
        fields.put("데이터 수집 처리 시간 ", "**::** `" + tookSec + "`");
        fields.put("행사 수집 기간", "**START:** `" + start + "`\n**END:** `" + end + "`");
        return fields;
    }
}
