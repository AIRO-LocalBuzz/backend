package backend.airo.domain.image.command;


import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageException;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UpdateImageCommandService {

    private final ImageRepository imageRepository;

    public Image updateSortOrder(Long imageId, Integer newSortOrder) {

        Image existingImage = imageRepository.findById(imageId);

        Image updatedImage = existingImage.updateSortOrder(newSortOrder);
        return imageRepository.save(updatedImage);
    }

    public Image updateCaption(Long imageId, String newCaption) {


        Image existingImage = imageRepository.findById(imageId);

        Image updatedImage = existingImage.updateCaption(newCaption);
        return imageRepository.save(updatedImage);
    }


    public Image updateAltText(Long imageId, String newAltText) {

        Image existingImage = imageRepository.findById(imageId);

        Image updatedImage = existingImage.updateAltText(newAltText);
        return imageRepository.save(updatedImage);
    }

    public Collection<Image> reorderImages(List<Long> imageIds) {

        Collection<Image> images = imageRepository.findAllById(imageIds);

        if (images.size() != imageIds.size()) {
            Set<Long> foundIds = images.stream()
                    .map(Image::getId)
                    .collect(Collectors.toSet());

            Long missingId = imageIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .findFirst()
                    .orElse(null);

            throw ImageException.notFound(missingId);
        }

        Map<Long, Image> imageMap = images.stream()
                .collect(Collectors.toMap(Image::getId, Function.identity()));

        // 순서 재정렬 - 새로운 인스턴스 생성
        List<Image> updatedImages = imageIds.stream()
                .map(imageId -> {
                    Image image = imageMap.get(imageId);
                    if (image == null) {
                        throw ImageException.notFound(imageId);
                    }
                    return image.updateSortOrder(imageIds.indexOf(imageId) + 1);
                })
                .collect(Collectors.toList());

        return imageRepository.saveAll(updatedImages);
    }


    public List<Image> updateMultipleImages(List<Image> images) {


        for (Image image : images) {
            if (!imageRepository.existsById(image.getId())) {
                throw ImageException.notFound(image.getId());
            }
        }

        return (List<Image>) imageRepository.saveAll(images);
    }
}