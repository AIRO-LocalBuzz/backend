package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 게시물 목록 응답 DTO
 */
@Schema(description = "게시물 목록 응답")
public record PostListResponse(
        @Schema(description = "게시물 목록")
        List<PostResponse> posts,

        @Schema(description = "페이징 정보")
        PageInfo pageInfo
) {
    public static PostListResponse fromDomain(Page<Post> postPage) {
        List<PostResponse> posts = postPage.getContent().stream()
                .map(PostResponse::fromDomain)
                .toList();

        PageInfo pageInfo = new PageInfo(
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isFirst(),
                postPage.isLast()
        );

        return new PostListResponse(posts, pageInfo);
    }

    @Schema(description = "페이징 정보")
    public record PageInfo(
            @Schema(description = "현재 페이지 번호", example = "0")
            int currentPage,

            @Schema(description = "페이지 크기", example = "20")
            int pageSize,

            @Schema(description = "전체 요소 수", example = "150")
            long totalElements,

            @Schema(description = "전체 페이지 수", example = "8")
            int totalPages,

            @Schema(description = "첫 번째 페이지 여부", example = "true")
            boolean first,

            @Schema(description = "마지막 페이지 여부", example = "false")
            boolean last
    ) {}
}