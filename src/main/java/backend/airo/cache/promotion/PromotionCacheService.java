package backend.airo.cache.promotion;

import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.repository.PromotionRepository;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionCacheService {

    private final PromotionRepository promotionRepository;
    private final CacheManager cacheManager;

    /**
     * 홍보물 정보 조회 (캐시 적용)
     */
    @Cacheable(
            cacheNames = CacheName.PROMOTION_DETAIL_CACHE,
            key = "#postId",
            sync = true
    )
    public Promotion getPromotion(Long postId) {
        log.debug("캐시 미스 - DB에서 홍보물 조회: postId={}", postId);
        Optional<Promotion> promotion = promotionRepository.findByPostId(postId);
        return promotion.orElse(null);
    }

    public byte[] getPromotionImageData(Long postId) {
        // 홍보물 정보 조회 (캐시 적용)
        Promotion promotion = getPromotion(postId);
        if (promotion == null) {
            log.debug("홍보물이 존재하지 않음: postId={}", postId);
            return null;
        }

        // 🔧 postId 기반 캐시 키로 통일
        String cacheKey = "post_" + postId;  // 변경 필요

        log.debug("홍보물 이미지 캐시 조회: postId={}, cacheKey={}", postId, cacheKey);

        var cache = cacheManager.getCache(CacheName.PROMOTION_THUMBNAILS_CACHE);

        if (cache != null) {
            byte[] cachedResult = cache.get(cacheKey, byte[].class);
            if (cachedResult != null) {
                log.debug("캐시에서 홍보물 이미지 반환: postId={}, size={} bytes", postId, cachedResult.length);
                return cachedResult;
            }
        }

        log.debug("홍보물 이미지가 캐시에 없음: postId={}", postId);
        return null;
    }

    /**
     * 홍보물 존재 여부 확인
     */
    public boolean existsByPostId(Long postId) {
        Promotion promotion = getPromotion(postId);
        return promotion != null;
    }

    //----------------------------------------
    // 캐시 삭제 메서드들
    //----------------------------------------

    /**
     * 특정 홍보물 캐시 삭제
     */
    @CacheEvict(cacheNames = CacheName.PROMOTION_THUMBNAILS_CACHE, key = "'post_' + #postId")
    public void evictPromotionCaches(Long postId, Object promotionResult) {
        log.debug("홍보물 관련 캐시 삭제: postId={}", postId);
    }

    /**
     * 모든 홍보물 캐시 삭제
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.PROMOTION_DETAIL_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.PROMOTION_THUMBNAILS_CACHE, allEntries = true)
    })
    public void evictAllPromotionCaches() {
        log.debug("모든 홍보물 캐시 삭제");
    }

    /**
     * 홍보물 이미지 캐시만 삭제
     */
    @CacheEvict(cacheNames = CacheName.PROMOTION_THUMBNAILS_CACHE, allEntries = true)
    public void evictPromotionImageCaches() {
        log.debug("홍보물 이미지 캐시 삭제");
    }
}