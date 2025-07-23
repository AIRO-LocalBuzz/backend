package backend.airo.infra.clure_fatvl_open_api.vo;

import java.time.LocalDate;

public record ClutrFatvlInfo(
        String fstvlNm,        // 축제명
        String opar,           // 개최 장소
        LocalDate fstvlStartDate, // 축제 시작일자 (yyyy‑MM‑dd)
        LocalDate fstvlEndDate,   // 축제 종료일자
        String fstvlCo,        // 축제 내용
        String mnnstNm,        // 주관 기관명
        String auspcInsttNm,   // 주최 기관명
        String suprtInsttNm,   // 후원 기관명
        String phoneNumber,    // 전화번호
        String homepageUrl,    // 홈페이지 주소
        String relateInfo,     // 관련 정보
        String rdnmadr,        // 소재지 도로명 주소
        String lnmadr,         // 소재지 지번 주소
        Double latitude,       // 위도
        Double longitude,      // 경도
        LocalDate referenceDate, // 데이터 기준 일자
        String instt_code,     // 제공 기관 코드
        String instt_nm        // 제공 기관명
) {
}
