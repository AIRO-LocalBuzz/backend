package backend.airo.domain.post.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.dto.PostSearchCriteria;
import backend.airo.domain.post.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Post 도메인 Repository 인터페이스
 * Domain Layer에서 정의하는 비즈니스 저장소 계약
 */
public interface PostRepository extends AggregateSupport<Post, Long> {


    /**
     * 게시물 존재 여부 확인
     * @param id 게시물 ID
     * @return 존재 여부
     */
    boolean existsById(Long id);

    /**
     * 게시물 삭제
     * @param id 삭제할 게시물 ID
     */
    void deleteById(Long id);


    Page<Post> findByStatus(
            PostStatus status,
            Pageable pageable
    );

    int incrementLikeCount(Long postId);
    int decrementLikeCount(Long postId);

    // 좋아요 순으로 게시물 조회
    Page<Post> findAllOrderByLikeCountDesc(Pageable pageable);

    // 조회 순으로 게시물 조회
    Page<Post> findAllOrderByViewCountDesc(Pageable pageable);

}