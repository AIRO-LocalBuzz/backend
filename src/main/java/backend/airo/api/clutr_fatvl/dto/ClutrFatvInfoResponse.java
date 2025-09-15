package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import lombok.Builder;

@Builder
public record ClutrFatvInfoResponse(
        String id,

        // 축제/행사 이름
        String fstvlNm,

        // 행사 장소
        String opar,

        // 행사 상세 내용
        String fstvlCo,

        // 행사 기간 [시작일, 종료일]
        FestivalPeriod period,

        // 행사 주소 [지번, 도로명]
        Address address,

        // 주관 기관명(Organizer / 실제 운영)
        String mnnstNm,

        // 주최 기관명(Host)
        String auspcInsttNm,

        // 후원 기관명(Sponsor)
        String suprtInsttNm,

        // 행사 담당 전화번호
        String phoneNumber,

        // 행사 홈페이지 URL
        String homepageUrl,

        // 관련 정보(비고/추가 안내/링크 등)
        String relateInfo

//        // 이 데이터(레코드)의 기준/갱신 일자
//        LocalDate referenceDate

//        // 공공데이터포털 제공 기관 코드
//        String insttCode,
//
//        // 공공데이터포털 제공 기관명
//        String insttNm
) {

    public static ClutrFatvInfoResponse create(ClutrFatvl clutrFatvlInfo) {
        return ClutrFatvInfoResponse.builder()
                .id(clutrFatvlInfo.id())
                .fstvlNm(clutrFatvlInfo.fstvlNm())
                .opar(clutrFatvlInfo.opar())
                .fstvlCo(clutrFatvlInfo.fstvlCo())
                .period(clutrFatvlInfo.period())
                .address(clutrFatvlInfo.address())
                .mnnstNm(clutrFatvlInfo.mnnstNm())
                .auspcInsttNm(clutrFatvlInfo.auspcInsttNm())
                .suprtInsttNm(clutrFatvlInfo.suprtInsttNm())
                .phoneNumber(clutrFatvlInfo.phoneNumber())
                .homepageUrl(clutrFatvlInfo.homepageUrl())
                .relateInfo(clutrFatvlInfo.relateInfo())
                .build();
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
