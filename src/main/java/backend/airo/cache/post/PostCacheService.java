package backend.airo.cache.post;

import backend.airo.api.post.dto.PostListRequest;
import backend.airo.api.post.dto.PostSliceRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.query.GetPostListQueryService;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCacheService {

    private final GetPostQueryService getPostQueryService;
    private final GetPostListQueryService getPostListQueryService;

    @Cacheable(
            cacheNames = CacheName.POST_DETAIL_CACHE,
            key = "#postId",
            sync = true
    )
    public Post getPost(Long postId) {
        log.debug("캐시 미스 - DB에서 게시물 조회: postId={}", postId);
        return getPostQueryService.handle(postId);
    }

    @Cacheable(
            cacheNames = CacheName.POST_LIST_CACHE,
            key = "#request.hashCode()",
            sync = true
    )
    public Page<Post> getPostList(PostListRequest request) {
        log.debug("캐시 미스 - DB에서 게시물 목록 조회: {}", request);
        return getPostListQueryService.handle(request);
    }

    @Cacheable(
            cacheNames = CacheName.POST_SLICE_CACHE,
            key = "#request.hashCode()",
            sync = true
    )
    public Slice<Post> getPostSlice(PostSliceRequest request) {
        log.debug("캐시 미스 - DB에서 게시물 슬라이스 조회: {}", request);
        return getPostListQueryService.handleSlice(request);
    }

    @CacheEvict(cacheNames = CacheName.POST_DETAIL_CACHE, key = "#postId")
    public void evictPost(Long postId) {
        log.debug("게시물 캐시 삭제: postId={}", postId);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.POST_DETAIL_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.POST_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.POST_SLICE_CACHE, allEntries = true)
    })
    public void evictAllPostCaches() {
        log.debug("모든 게시물 캐시 삭제");
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.POST_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.POST_SLICE_CACHE, allEntries = true)
    })
    public void evictPostListCaches() {
        log.debug("게시물 목록 캐시 삭제");
    }
}