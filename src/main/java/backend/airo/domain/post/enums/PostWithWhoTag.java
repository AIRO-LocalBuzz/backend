package backend.airo.domain.post.enums;

public enum PostWithWhoTag {
    ALLONE("혼자"),
    FRIEND("친구"),
    FAMILY("가족"),
    PARTNER("연인");

    private final String description;

    PostWithWhoTag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}