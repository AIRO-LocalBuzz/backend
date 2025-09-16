package backend.airo.persistence.clutrfatvl.entity;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.vo.Address;
import backend.airo.domain.clure_fatvl.vo.GeoPoint;
import backend.airo.persistence.abstracts.ImmutableEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(
        name = "clutr_fatvl",
        uniqueConstraints = @UniqueConstraint(name = "uk_clutr_bizkey", columnNames = "biz_key")
)

public class ClutrFatvlEntity extends ImmutableEntity {

    @Id
    private Long contentId;

    private Integer contenttypeId;

    private String title;

    @Embedded
    private GeoPoint location;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "clutr_fatvl_phone", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "phone_number", length = 32, nullable = false)
    @OrderColumn(name = "seq")
    private List<String> phoneNumber;

    private String cat1;

    private String firstImage;

    private String firstImage2;

    @JsonFormat(pattern = "yyyyMMddHH")
    private LocalDate modifiedDate;

    @Builder
    public ClutrFatvlEntity(Long contentId, Integer contenttypeId, String title, GeoPoint location, Address address, List<String> phoneNumber, String cat1, String firstImage, String firstImage2, LocalDate modifiedDate) {
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


    public static ClutrFatvlEntity toEntity(ClutrFatvl dto) {
        return ClutrFatvlEntity.builder()
                .contentId(dto.getContentId())
                .contenttypeId(dto.getContenttypeId())
                .title(dto.getTitle())
                .location(dto.getLocation())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .cat1(dto.getCat1())
                .firstImage(dto.getFirstImage())
                .firstImage2(dto.getFirstImage2())
                .modifiedDate(dto.getModifiedDate())
                .build();
    }
//
//    public static ClutrFatvl toDomain(ClutrFatvlInfo dto, String megaCodeId, String ctprvnCodeId) {
//        return ClutrFatvl.builder()
//                .contentId()
//                .build();
//    }

    public static ClutrFatvl toDomain(ClutrFatvlEntity clutrFatvlEntity) {
        return ClutrFatvl.builder()
                .contentId(clutrFatvlEntity.contentId)
                .contenttypeId(clutrFatvlEntity.contenttypeId)
                .title(clutrFatvlEntity.title)
                .location(clutrFatvlEntity.location)
                .address(clutrFatvlEntity.address)
                .phoneNumber(clutrFatvlEntity.phoneNumber)
                .cat1(clutrFatvlEntity.cat1)
                .firstImage(clutrFatvlEntity.firstImage)
                .firstImage2(clutrFatvlEntity.firstImage2)
                .modifiedDate(clutrFatvlEntity.modifiedDate)
                .build();
    }


}

