package backend.airo.domain.image.command;


import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UpdateImageCommand {

    private final ImageRepository imageRepository;

    public Image updateSortOrder(Long imageId, Integer newSortOrder) {
        if (imageId == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        if (newSortOrder == null || newSortOrder < 0) {
            throw new IllegalArgumentException("정렬 순서는 0 이상이어야 합니다");
        }

        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }

        // 정렬 순서 업데이트
        existingImage.setSortOrder(newSortOrder);
        return imageRepository.save(existingImage);
    }

    public Image updateCaption(Long imageId, String newCaption) {
        if (imageId == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }

        // 캡션 업데이트
        existingImage.setCaption(newCaption);
        return imageRepository.save(existingImage);
    }


    public Image updateAltText(Long imageId, String newAltText) {
        if (imageId == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        Image existingImage = imageRepository.findById(imageId);
        if (existingImage == null) {
            throw new ImageNotFoundException(imageId);
        }

        // Alt 텍스트 업데이트
        existingImage.setAltText(newAltText);
        return imageRepository.save(existingImage);
    }



    public Collection<Image> reorderImages(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("이미지 ID 목록은 필수입니다");
        }

        Collection<Image> images = imageRepository.findAllById(imageIds);
        if (images.size() != imageIds.size()) {
            throw new ImageNotFoundException("일부 이미지를 찾을 수 없습니다");
        }

        // Map으로 변환하여 더 안전하게 처리
        Map<Long, Image> imageMap = images.stream()
                .collect(Collectors.toMap(Image::getId, Function.identity()));

        // 순서 재정렬
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            Image image = imageMap.get(imageId);
            if (image == null) {
                throw new ImageNotFoundException(imageId);
            }
            image.setSortOrder(i + 1);
        }

        return imageRepository.saveAll(images);
    }



    public List<Image> updateMultipleImages(List<Image> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("이미지 목록은 필수입니다");
        }

        // 모든 이미지가 존재하는지 확인
        for (Image image : images) {
            if (!imageRepository.existsById(image.getId())) {
                throw new ImageNotFoundException(image.getId());
            }
        }

        return (List<Image>) imageRepository.saveAll(images);
    }
}