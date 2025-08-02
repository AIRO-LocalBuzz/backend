package backend.airo.infra.open_api.shop.vo;

public record ShopInfo(
        // 식별·기본 정보
        String bizesId,
        String bizesNm,
        String brchNm,

        // 상권 업종 분류
        String indsLclsCd,
        String indsLclsNm,
        String indsMclsCd,
        String indsMclsNm,
        String indsSclsCd,
        String indsSclsNm,
        String ksicCd,
        String ksicNm,

        // 행정 구역
        String ctprvnCd,
        String ctprvnNm,
        String signguCd,
        String signguNm,
        String adongCd,
        String adongNm,
        String ldongCd,
        String ldongNm,

        // 지번 주소
        String lnoCd,
        String plotSctCd,
        String plotSctNm,
        String lnoMnno,
        String lnoSlno,
        String lnoAdr,

        // 도로명 주소
        String rdnmCd,
        String rdnm,
        String bldMnno,
        String bldSlno,
        String bldMngNo,
        String bldNm,
        String rdnmAdr,

        // 우편·건물 상세
        String oldZipcd,
        String newZipcd,
        String dongNo,
        String flrNo,
        String hoNo,

        // 좌표
        Double lon,
        Double lat
) {
}
