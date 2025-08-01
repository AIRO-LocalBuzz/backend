package backend.airo.domain.post.enums;

public enum PostCategory {
    RESTORANT("음식점"), // 음식점
    CAFE("카페"), // 카페
    ACCOMMODATION("숙소"), // 숙소
    EVENT("행사"), // 행사
    EXPERIENCE("체험"), // 체험
    CHALLENGE("챌린지"), // 챌린지
    LEISURE("여가"); // 여가

    private final String description;

    PostCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}