package backend.airo.domain.clure_fatvl;

import backend.airo.domain.clure_fatvl.vo.FestivalPeriod;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ClutrFatvlInfo {

    private final String contentid;
    @Embedded
    private final FestivalPeriod period;

    private final String usetimefestival;
    private final String playTime;
    private final String start;
    private final String end;
    private final String phoneNumber;
    private final String agelimit;
    private final String clutrFatvlIntro;
    private final String mainProgramInfo;
    private final String guestProgramInfo;
    private final String eventHomePage;
    private final String eventPlace;


    @Builder
    public ClutrFatvlInfo(String contentid, FestivalPeriod period, String usetimefestival, String playTime, String start, String end, String phoneNumber, String agelimit, String clutrFatvlIntro, String mainProgramInfo, String guestProgramInfo, String eventHomePage, String eventPlace) {
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

    @Override
    public String toString() {
        return "ClutrFatvlInfo{" +
                "contentid='" + contentid + '\'' +
                ", period=" + period +
                ", usetimefestival='" + usetimefestival + '\'' +
                ", playTime='" + playTime + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", agelimit='" + agelimit + '\'' +
                ", clutrFatvlIntro='" + clutrFatvlIntro + '\'' +
                ", mainProgramInfo='" + mainProgramInfo + '\'' +
                ", guestProgramInfo='" + guestProgramInfo + '\'' +
                ", eventHomePage='" + eventHomePage + '\'' +
                ", eventPlace='" + eventPlace + '\'' +
                '}';
    }
}

