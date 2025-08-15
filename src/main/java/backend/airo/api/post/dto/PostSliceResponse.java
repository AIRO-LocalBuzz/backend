package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import org.springframework.data.domain.Slice;

import java.util.List;

public record PostSliceResponse(
        List<PostSummaryResponse> posts,
        boolean hasNext,
        int size,
        Long lastPostId
) {
//    public static PostSliceResponse fromDomain(Slice<Post> slice) {
//        List<PostSummaryResponse> posts = slice.getContent().stream()
//                .map(PostSummaryResponse::fromDomain)
//                .toList();
//
//        Long lastPostId = posts.isEmpty() ? null :
//                posts.get(posts.size() - 1).id();
//
//        return new PostSliceResponse(
//                posts,
//                slice.hasNext(),
//                slice.getSize(),
//                lastPostId
//        );
//    }


    public static PostSliceResponse fromDomain(Slice<PostSummaryResponse> slice) {
        Long lastPostId = slice.getContent().isEmpty() ? null :
                slice.getContent().get(slice.getContent().size() - 1).id();

        return new PostSliceResponse(
                slice.getContent(),
                slice.hasNext(),
                slice.getSize(),
                lastPostId
        );
    }
}