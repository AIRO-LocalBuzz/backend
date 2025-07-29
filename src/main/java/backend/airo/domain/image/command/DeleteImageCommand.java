package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.image.exception.UnauthorizedException;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteImageCommand {

    private final ImageRepository imageRepository;

    public boolean deleteById(Long imageId) {
        if (imageId == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }

        imageRepository.deleteById(imageId);
        return true;
    }

    public boolean deleteById(Long imageId, Long currentUserId) {
        if (imageId == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        if (currentUserId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다");
        }

        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }

        // 권한 확인 (이미지 소유자인지 확인)
        if (!existingImage.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("이미지 삭제 권한이 없습니다");
        }

        imageRepository.deleteById(imageId);
        return true;
    }

    public void deleteAllById(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("이미지 ID 목록은 필수입니다");
        }

        // 존재하는 이미지들만 필터링하여 삭제
        List<Long> existingIds = imageIds.stream()
                .filter(imageRepository::existsById)
                .toList();

        if (!existingIds.isEmpty()) {
            imageRepository.deleteAllById(existingIds);
        }
    }

    public void deleteByPostId(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시물 ID는 필수입니다");
        }

        imageRepository.deleteByPostId(postId);
    }

    public void deleteByPostId(Long postId, Long currentUserId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시물 ID는 필수입니다");
        }

        if (currentUserId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다");
        }

        // 게시물의 모든 이미지 조회
        List<Image> postImages = (List<Image>) imageRepository.findImagesAllByPostId(postId);

        // 권한 확인 (모든 이미지가 현재 사용자의 것인지 확인)
        boolean hasPermission = postImages.stream()
                .allMatch(image -> image.getUserId().equals(currentUserId));

        if (!hasPermission) {
            throw new UnauthorizedException("게시물 이미지 삭제");
        }

        imageRepository.deleteByPostId(postId);
    }

}