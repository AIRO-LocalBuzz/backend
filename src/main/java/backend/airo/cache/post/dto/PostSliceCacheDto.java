package backend.airo.cache.post.dto;

import backend.airo.api.post.dto.PostSummaryResponse;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PostSliceCacheDto {
    private List<PostSummaryResponse> content;
    private boolean hasNext;
    private int size;
    private int numberOfElements;

    public Slice<PostSummaryResponse> toSlice(Pageable pageable) {
        return new SliceImpl<>(content, pageable, hasNext);
    }
}