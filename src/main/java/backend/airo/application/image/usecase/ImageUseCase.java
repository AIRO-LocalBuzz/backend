package backend.airo.application.image.usecase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.command.CreateImageCommandService;
import backend.airo.domain.image.command.DeleteImageCommandService;
import backend.airo.domain.image.command.UpdateImageCommandService;
import backend.airo.domain.image.query.GetImageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUseCase {

    private final CreateImageCommandService createImageCommandService;
    private final GetImageQueryService getImageQueryService;
    private final UpdateImageCommandService updateImageCommandService;
    private final DeleteImageCommandService deleteImageCommandService;

    // ========== 이미지 업로드 (Create) ==========

    /**
     * 단일 이미지 업로드
     * @param image 업로드할 이미지 정보
     * @return 저장된 이미지 정보
     */
    public Image uploadSingleImage(Image image) {
        return createImageCommandService.handle(image);
    }

    /**
     * 다중 이미지 업로드
     * @param images 업로드할 이미지 목록
     * @return 저장된 이미지 목록
     */
    public List<Image> uploadMultipleImages(List<Image> images) {
        return images.stream()
                .map(createImageCommandService::handle)
                .toList();
    }

    /**
     * 재시도 로직이 포함된 이미지 업로드
     * @param image 업로드할 이미지 정보
     * @return 저장된 이미지 정보
     */
    public Image uploadImageWithRetry(Image image) {
        return createImageCommandService.handleWithRetry(image);
    }

    /**
     * 동시성 제어가 포함된 이미지 업로드
     * @param image 업로드할 이미지 정보
     * @return 저장된 이미지 정보
     */
    public Image uploadImageWithLock(Image image) {
        return createImageCommandService.handleWithLock(image);
    }

    /**
     * 썸네일 자동 생성
     * @param originalImageUrl 원본 이미지 URL
     * @return 생성된 썸네일 URL
     */
    public String generateThumbnail(String originalImageUrl) {
        return createImageCommandService.generateThumbnail(originalImageUrl);
    }

    // ========== 이미지 조회 (Read) ==========

    /**
     * 단일 이미지 조회
     * @param imageId 이미지 ID
     * @return 이미지 정보
     */
    public Image getSingleImage(Long imageId) {
        return getImageQueryService.getSingleImage(imageId);
    }

    /**
     * 이미지 존재 여부 확인
     * @param imageId 이미지 ID
     * @return 존재 여부
     */
    public boolean isImageExists(Long imageId) {
        return getImageQueryService.imageExists(imageId);
    }

    /**
     * 게시물별 이미지 목록 조회
     * @param postId 게시물 ID
     * @return 이미지 목록
     */
    public Collection<Image> getImagesByPost(Long postId) {
        return getImageQueryService.getImagesBelongsPost(postId);
    }

    /**
     * 페이징된 이미지 목록 조회
     * @param pageable 페이지 정보
     * @return 페이징된 이미지 목록
     */
    public Page<Image> getPagedImages(Pageable pageable) {
        return getImageQueryService.getPagedImages(pageable);
    }

    /**
     * 정렬된 이미지 목록 조회 (게시물별)
     * @param postId 게시물 ID
     * @return 정렬된 이미지 목록
     */
    public List<Image> getSortedImagesByPost(Long postId) {
        return getImageQueryService.getSortedImagesByPost(postId);
    }

    /**
     * MIME 타입별 이미지 조회
     * @param mimeType MIME 타입
     * @return 이미지 목록
     */
    public List<Image> getImagesByMimeType(String mimeType) {
        return getImageQueryService.getImagesByMimeType(mimeType);
    }


    // ========== 이미지 수정 (Update) ==========

    /**
     * 이미지 순서 변경
     * @param imageId 이미지 ID
     * @param newSortOrder 새로운 순서
     * @return 수정된 이미지 정보
     */
    public Image updateImageSortOrder(Long imageId, Integer newSortOrder) {
        return updateImageCommandService.updateSortOrder(imageId, newSortOrder);
    }

    /**
     * 이미지 캡션 수정
     * @param imageId 이미지 ID
     * @param newCaption 새로운 캡션
     * @return 수정된 이미지 정보
     */
    public Image updateImageCaption(Long imageId, String newCaption) {
        return updateImageCommandService.updateCaption(imageId, newCaption);
    }



    /**
     * 이미지 Alt 텍스트 수정
     * @param imageId 이미지 ID
     * @param newAltText 새로운 Alt 텍스트
     * @return 수정된 이미지 정보
     */
    public Image updateImageAltText(Long imageId, String newAltText) {
        return updateImageCommandService.updateAltText(imageId, newAltText);
    }



    /**
     * 이미지 순서 재정렬
     * @param imageIds 재정렬할 이미지 ID 목록
     * @return 재정렬된 이미지 목록
     */
    public List<Image> reorderImages(List<Long> imageIds) {
        Collection<Image> result = updateImageCommandService.reorderImages(imageIds);
        return result instanceof List ? (List<Image>) result : new ArrayList<>(result);
    }



    /**
     * 다중 이미지 일괄 수정
     * @param images 수정할 이미지 목록
     * @return 수정된 이미지 목록
     */
    public List<Image> updateMultipleImages(List<Image> images) {
        return updateImageCommandService.updateMultipleImages(images);
    }

    // ========== 이미지 삭제 (Delete) ==========

    /**
     * 단일 이미지 삭제
     * @param imageId 이미지 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteSingleImage(Long imageId) {
        return deleteImageCommandService.deleteById(imageId);
    }

    /**
     * 권한 확인 후 이미지 삭제
     * @param imageId 이미지 ID
     * @param currentUserId 현재 사용자 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteImageWithAuth(Long imageId, Long currentUserId) {
        return deleteImageCommandService.deleteById(imageId, currentUserId);
    }

    /**
     * 다중 이미지 삭제
     * @param imageIds 삭제할 이미지 ID 목록
     */
    public void deleteMultipleImages(List<Long> imageIds) {
        deleteImageCommandService.deleteAllById(imageIds);
    }

    /**
     * 게시물 삭제 시 연관 이미지 모두 삭제
     * @param postId 게시물 ID
     */
    public void deleteImagesByPost(Long postId) {
        deleteImageCommandService.deleteByPostId(postId);
    }

    /**
     * 권한 확인 후 게시물의 모든 이미지 삭제
     * @param postId 게시물 ID
     * @param currentUserId 현재 사용자 ID
     */
    public void deleteImagesByPostWithAuth(Long postId, Long currentUserId) {
        deleteImageCommandService.deleteByPostId(postId, currentUserId);
    }



    // ========== 비즈니스 로직 ==========


    /**
     * 이미지 통계 정보 조회
     * @param postId 게시물 ID
     * @return 이미지 개수
     */
    public int getImageCountByPost(Long postId) {
        return getImagesByPost(postId).size();
    }

    /**
     * 게시물의 총 이미지 용량 계산
     * @param postId 게시물 ID
     * @return 총 용량 (bytes)
     */
    public long getTotalImageSizeByPost(Long postId) {
        return getImagesByPost(postId).stream()
                .mapToLong(Image::getFileSize)
                .sum();
    }
}