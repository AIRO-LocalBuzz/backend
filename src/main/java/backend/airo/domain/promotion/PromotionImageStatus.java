package backend.airo.domain.promotion;

/**
 * 홍보물 이미지 생성 상태
 */
public enum PromotionImageStatus {
    PROCESSING("이미지 생성 중"),
    COMPLETED("생성 완료"),
    FAILED("생성 실패");

    private final String description;

    PromotionImageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}