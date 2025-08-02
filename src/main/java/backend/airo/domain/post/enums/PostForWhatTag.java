package backend.airo.domain.post.enums;

public enum PostForWhatTag {
    WORK("업무"),
    SEMINAR("세미나"),
    SCHOOL("학교"),
    HEALING("힐링"),
    STUDY("공부"),
    CULINARY("식도락");

    private final String description;

    PostForWhatTag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}