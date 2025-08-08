package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageErrorCode;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.image.exception.InvalidImageException;
import backend.airo.domain.image.exception.UnauthorizedException;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteImageCommandService {

    private final ImageRepository imageRepository;


    public boolean deleteById(Long imageId, Long userId) {
        getImagesAndCheckOwner(imageId, userId);
        imageRepository.deleteById(imageId);
        return true;
    }


    public void deleteAllById(List<Long> imageIds, Long userId) {
        List<Long> existingIds = imageIds.stream()
                .filter(imageRepository::existsById)
                .toList();
        if (existingIds.isEmpty()) {
            throw new ImageNotFoundException(imageIds);
        }else {
            for (Long imageId : existingIds) {
                getImagesAndCheckOwner(imageId, userId);
            }
            imageRepository.deleteAllById(existingIds);
        }
    }


    public void deleteByPostId(Long postId, Long userId) {

        // 게시물의 모든 이미지 조회
        List<Image> postImages = (List<Image>) imageRepository.findImagesAllByPostId(postId);

        for (Image image : postImages) {
            getImagesAndCheckOwner(image.getId(), userId);
        }

        imageRepository.deleteByPostId(postId);
    }



    private void getImagesAndCheckOwner(Long imageId, Long userId) {
        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }
        // 권한 확인
        if (!existingImage.getUserId().equals(userId)) {
            throw new UnauthorizedException("이미지 삭제");
        }
    }

}