package backend.airo.api.post.dto;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.vo.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "게시물 생성 요청")
public record PostCreateRequest(
        @Schema(description = "게시물 제목", example = "부산 여행 후기")
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
        String title,

        @Schema(description = "게시물 내용", example = "정말 좋은 여행이었습니다...")
        @NotBlank(message = "게시물 내용은 필수입니다")
        @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
        String content,

        @Schema(description = "게시물 상태", example = "PUBLISHED")
        @NotNull(message = "게시물 상태는 필수입니다") // @NotBlank → @NotNull
        PostStatus status,

        @Schema(description = "사업장 이름", example = "부산 맛집")
        @Size(max = 100, message = "사업장 이름은 100자를 초과할 수 없습니다") // 길이 제한 추가 권장
        @NotNull(message = "상호명은 필수입니다")
        String businessName,

        @Schema(description = "목적 태그", example = "HEALING")
        @NotNull(message = "forWhatTag는 필수입니다") // 필수라면 @NotNull 추가
        PostForWhatTag forWhatTag,

        @Schema(description = "감정 태그", example = "[\"EXCITED\", \"JOYFUL\"]")
        @NotNull(message = "감정태그는 필수입니다") // List에 @NotNull 추가
        @NotEmpty(message = "최소 1개의 감정태그를 선택해야 합니다")
        @Size(max = 5, message = "감정 태그는 최대 5개까지 추가 가능합니다")
        List<PostEmotionTag> emotionTags,

        @Schema(description = "카테고리", example = "RESTAURANT")
        @NotNull(message = "카테고리는 필수입니다") // @NotBlank → @NotNull
        PostCategory category,

        @Schema(description = "여행 날짜", example = "2024-08-15")
        @NotNull(message = "여행 날짜는 필수입니다") // 필수라면 @NotNull 추가
        @PastOrPresent(message = "여행 날짜는 현재 또는 과거여야 합니다")
        LocalDate travelDate,

        @Schema(description = "위치 정보", example = "{ \"latitude\": 35.1796, \"longitude\": 129.0756 }")
        @Valid // Location 객체 검증을 위해 추가
        Location location,

        @Schema(description = "주소", example = "부산시 해운대구")
        @Size(max = 500, message = "주소는 500자를 초과할 수 없습니다") // 길이 제한 추가 권장
        String address,

        @Schema(description = "이미지 생성 요청 목록")
        @Size(max = 5, message = "이미지는 최대 5개까지 업로드 가능합니다")
        @Valid // List 내부 객체 검증을 위해 추가
        List<ImageCreateRequest> images,

        @Schema(description = "추천 게시물 여부", example = "false")
        Boolean isFeatured
) {


}