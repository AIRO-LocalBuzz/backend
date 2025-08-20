package backend.airo.support.cache.local;

import backend.airo.api.post.dto.PostSummaryResponse;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.post.Post;
import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.shop.Shop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public enum CacheName {

    // 이미지 다운로드 및 썸네일 생성 캐시
    DOWNLOADED_IMAGES("DOWNLOADED_IMAGES", TimeUnit.HOURS, 6, 200, BufferedImage.class),
    PROMOTION_THUMBNAILS("PROMOTION_THUMBNAILS", TimeUnit.HOURS, 24, 100, byte[].class),
    PROMOTION_DETAIL("PROMOTION_DETAIL", TimeUnit.HOURS, 24, 100, Post.class),


    //지역, 지역구
    MEGA_AREA_ALL("MEGA_AREA_ALL", TimeUnit.DAYS, 180, 1000, List.class),
    CITY_AREA_ALL("CITY_AREA_ALL", TimeUnit.DAYS, 180, 1000, List.class),
    MEGA_AREA_ID("MEGA_AREA_ID", TimeUnit.DAYS, 180, 1000, Long.class),
    MEGA_AREA_NAME("MEGA_AREA_NAME", TimeUnit.DAYS, 180, 1000, String.class),
    CITY_AREA_ID("CITY_AREA_ID", TimeUnit.DAYS, 180, 1000, String.class),
    CITY_AREA_NAME("CITY_AREA_NAME", TimeUnit.DAYS, 180, 1000, Long.class),

    //콘텐츠 ( 문화/축제, 소상공인, 농업 체험 )
    CLUTR_FATVL_LIST("CLUTR_FATVL_LIST", TimeUnit.DAYS, 1, 100, List.class),
    CLUTR_FATVL_INFO("CLUTR_FATVL_INFO", TimeUnit.MINUTES, 5, 1000, ClutrFatvl.class),

    SHOP_LIST("SHOP_LIST", TimeUnit.DAYS, 180, 100, List.class),
    SHOP_INFO("SHOP_INFO", TimeUnit.MINUTES, 5, 1000, Shop.class),

    RURAL_EX_LIST("RURAL_EX_LIST", TimeUnit.DAYS, 180, 100, List.class),
    RURAL_EX_INFO("RURAL_EX_INFO", TimeUnit.MINUTES, 5, 1000, RuralEx.class),

    POST_DETAIL("POST_DETAIL", TimeUnit.MINUTES, 10, 1000, Post.class),
    POST_LIST("POST_LIST", TimeUnit.MINUTES, 5, 500, Page.class),
    POST_SLICE("POST_SLICE", TimeUnit.MINUTES, 5, 500, Slice.class),
    POST_SUMMARY("POST_SUMMARY",TimeUnit.MINUTES, 5, 500, PostSummaryResponse .class);



    private final String name;
    private final TimeUnit timeUnit;
    private final Integer expireAfterWrite; // 만료되는 시간
    private final Integer maximumSize; // 최대 저장갯수
    private final Class<?> valueType;


    public Duration getDuration() {
        return Duration.ofNanos(timeUnit.toNanos(expireAfterWrite));
    }

    public static final String DOWNLOADED_IMAGES_CACHE = "DOWNLOADED_IMAGES";
    public static final String PROMOTION_THUMBNAILS_CACHE = "PROMOTION_THUMBNAILS";
    public static final String PROMOTION_DETAIL_CACHE = "PROMOTION_DETAIL";

    public static final String MEGA_AREA_ALL_CACHE = "MEGA_AREA_ALL";
    public static final String CITY_AREA_ALL_CACHE = "CITY_AREA_ALL";
    public static final String MEGA_AREA_ID_CACHE = "MEGA_AREA_ID";
    public static final String MEGA_AREA_NAME_CACHE = "MEGA_AREA_NAME";
    public static final String CITY_AREA_ID_CACHE = "CITY_AREA_ID";
    public static final String CITY_AREA_NAME_CACHE = "CITY_AREA_NAME";

    public static final String CLUTR_FATVL_LIST_CACHE = "CLUTR_FATVL_LIST";
    public static final String CLUTR_FATVL_INFO_CACHE = "CLUTR_FATVL_INFO";

    public static final String SHOP_LIST_CACHE = "SHOP_LIST";
    public static final String SHOP_INFO_CACHE = "SHOP_INFO";

    public static final String RURAL_EX_LIST_CACHE = "RURAL_EX_LIST";
    public static final String RURAL_EX_INFO_CACHE = "RURAL_EX_INFO";

    public static final String POST_DETAIL_CACHE = "POST_DETAIL";
    public static final String POST_LIST_CACHE = "POST_LIST";
    public static final String POST_SLICE_CACHE = "POST_SLICE";
    public static final String POST_SUMMARY_CACHE = "POST_SUMMARY";


}
