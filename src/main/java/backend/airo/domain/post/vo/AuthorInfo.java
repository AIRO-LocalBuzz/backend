package backend.airo.domain.post.vo;


public record AuthorInfo(
    Long id,
    String nickname,
    String profileImageUrl
){

    public AuthorInfo (Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;

    }
}
