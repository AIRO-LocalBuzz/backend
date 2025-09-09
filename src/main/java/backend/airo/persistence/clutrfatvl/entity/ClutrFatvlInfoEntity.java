package backend.airo.persistence.clutrfatvl.entity;

import backend.airo.domain.clure_fatvl.ClutrFatvlInfo;
import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ClutrFatvlInfoEntity {

    @Id
    private String contentid;

    @Embedded
    private FestivalPeriod period;

    private String usetimefestival;
    private String playTime;
    private String start;
    private String end;
    private String phoneNumber;
    private String agelimit;
    private String clutrFatvlIntro;
    private String mainProgramInfo;
    private String guestProgramInfo;
    private String eventHomePage;
    private String eventPlace;


    @Builder
    public ClutrFatvlInfoEntity(String contentid, FestivalPeriod period, String usetimefestival, String playTime, String start, String end, String phoneNumber, String agelimit, String clutrFatvlIntro, String mainProgramInfo, String guestProgramInfo, String eventHomePage, String eventPlace) {
        this.contentid = contentid;
        this.period = period;
        this.usetimefestival = usetimefestival;
        this.playTime = playTime;
        this.start = start;
        this.end = end;
        this.phoneNumber = phoneNumber;
        this.agelimit = agelimit;
        this.clutrFatvlIntro = clutrFatvlIntro;
        this.mainProgramInfo = mainProgramInfo;
        this.guestProgramInfo = guestProgramInfo;
        this.eventHomePage = eventHomePage;
        this.eventPlace = eventPlace;
    }

    public static ClutrFatvlInfo toDomain(ClutrFatvlInfoEntity clutrFatvlInfoEntity) {
        return ClutrFatvlInfo.builder()
                .contentid(clutrFatvlInfoEntity.contentid)
                .playTime(clutrFatvlInfoEntity.playTime)
                .start(clutrFatvlInfoEntity.start)
                .end(clutrFatvlInfoEntity.end)
                .phoneNumber(clutrFatvlInfoEntity.phoneNumber)
                .agelimit(clutrFatvlInfoEntity.agelimit)
                .clutrFatvlIntro(clutrFatvlInfoEntity.clutrFatvlIntro)
                .mainProgramInfo(clutrFatvlInfoEntity.mainProgramInfo)
                .guestProgramInfo(clutrFatvlInfoEntity.guestProgramInfo)
                .eventHomePage(clutrFatvlInfoEntity.eventHomePage)
                .eventPlace(clutrFatvlInfoEntity.eventPlace)
                .build();
    }

    public static ClutrFatvlInfoEntity toEntity(ClutrFatvlInfo clutrFatvlInfo) {
        return ClutrFatvlInfoEntity.builder()
                .contentid(clutrFatvlInfo.getContentid())
                .playTime(clutrFatvlInfo.getPlayTime())
                .start(clutrFatvlInfo.getStart())
                .end(clutrFatvlInfo.getEnd())
                .phoneNumber(clutrFatvlInfo.getPhoneNumber())
                .agelimit(clutrFatvlInfo.getAgelimit())
                .clutrFatvlIntro(clutrFatvlInfo.getClutrFatvlIntro())
                .mainProgramInfo(clutrFatvlInfo.getMainProgramInfo())
                .guestProgramInfo(clutrFatvlInfo.getGuestProgramInfo())
                .eventHomePage(clutrFatvlInfo.getEventHomePage())
                .eventPlace(clutrFatvlInfo.getEventPlace())
                .build();
    }



}

