package backend.airo.domain.post.enums;

public enum PostStatus {
    DRAFT("임시저장"),
    PUBLISHED("발행됨"),
    ARCHIVED("보관됨");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}