package backend.airo.domain.post.enums;

public enum PostStatus {
    DRAFT, // Post is in draft status
    PUBLISHED, // Post is published and visible to users
    ARCHIVED, // Post is archived and not visible to users
    DELETED // Post is deleted and not recoverable
}
