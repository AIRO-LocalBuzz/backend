package backend.airo.domain.clure_fatvl;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvl;
import backend.airo.domain.clure_fatvl.utils.PhoneNormalizer;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.GeoPoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
public class ClutrFatvl {

    private final Long contentId;

    private final Integer contenttypeId;

    private final String title;

    @Embedded
    private final GeoPoint location;

    @Embedded
    private final Address address;

    private final List<String> phoneNumber;

    private final String cat1;

    private final String firstImage;

    private final String firstImage2;

    @JsonFormat(pattern = "yyyyMMdd")
    private final LocalDate modifiedDate;

    @Builder

    public ClutrFatvl(Long contentId, Integer contenttypeId, String title, GeoPoint location, Address address, List<String> phoneNumber, String cat1, String firstImage, String firstImage2, LocalDate modifiedDate) {
        this.contentId = contentId;
        this.contenttypeId = contenttypeId;
        this.title = title;
        this.location = location;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cat1 = cat1;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.modifiedDate = modifiedDate;
    }



    public static ClutrFatvl create(OpenApiClutrFatvl dto) {
        return ClutrFatvl.builder()
                .contentId(dto.contentId())
                .contenttypeId(dto.contenttypeId())
                .title(dto.title())
                .location(new GeoPoint(dto.lat(),dto.lon()))
                .address(new Address(dto.addr1(), dto.addr2(), dto.megaCodeId(), dto.ctprvnCodeId()))
                .phoneNumber(PhoneNormalizer.allPhones(dto.phoneNumber()))
                .cat1(dto.cat1())
                .firstImage(dto.firstImage())
                .firstImage2(dto.firstImage2())
                .modifiedDate(dto.modifiedDate())
                .build();
    }


}

