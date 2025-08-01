//package backend.airo.application.thumbnail;
//
//import backend.airo.domain.thumbnail.LLMProvider;
//import backend.airo.domain.thumbnail.ThumbnailRequest;
//import backend.airo.domain.thumbnail.ThumbnailResult;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MockLLMProvider implements LLMProvider {
//
//    @Override
//    public ThumbnailResult generateThumbnail(ThumbnailRequest request) {
//        return new ThumbnailResult(
//                request.imageUrls().isEmpty() ? null : request.imageUrls().get(0),
//                List.of("추천태그1", "추천태그2", "추천태그3"),
//                "개선된 " + request.title()
//        );
//    }
//}