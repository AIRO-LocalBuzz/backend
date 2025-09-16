package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import lombok.Builder;

@Builder
public record ClutrFatvInfoResponse(
        Long contentId,
        String title,
        String firstImage1,
        String firstImage2,
        String homepage,
        String addr1,
        String addr2,
        String cat1,
        String usetimefestival,
        String playTime,
        String start,
        String end,
        String phoneNumber,
        String agelimit,
        String clutrFatvlIntro,
        String mainProgramInfo,
        String guestProgramInfo
) {

    public static ClutrFatvInfoResponse create(ClutrFatvl clutrFatvl, ClutrFatvlInfo clutrFatvlInfo) {
        return ClutrFatvInfoResponse.builder()
                .contentId(clutrFatvl.getContentId())
                .title(clutrFatvl.getTitle())
                .firstImage1(clutrFatvl.getFirstImage())
                .firstImage2(clutrFatvl.getFirstImage2())
                .homepage(clutrFatvlInfo.getEventHomePage())
                .addr1(clutrFatvl.getAddress().addr1())
                .addr2(clutrFatvl.getAddress().addr2())
                .cat1(clutrFatvl.getCat1())
                .usetimefestival(clutrFatvlInfo.getUsetimefestival())
                .playTime(clutrFatvlInfo.getPlayTime())
                .start(clutrFatvlInfo.getStart())
                .end(clutrFatvlInfo.getEnd())
                .phoneNumber(clutrFatvlInfo.getPhoneNumber())
                .agelimit(clutrFatvlInfo.getAgelimit())
                .clutrFatvlIntro(clutrFatvlInfo.getClutrFatvlIntro())
                .mainProgramInfo(clutrFatvlInfo.getMainProgramInfo())
                .guestProgramInfo(clutrFatvlInfo.getGuestProgramInfo())
                .build();
    }

}
