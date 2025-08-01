package backend.airo.domain.post.enums;

public enum PostEmotionTag {
    // 기쁨과 즐거움
    EXCITED("설렘"),
    THRILLED("신남"),
    FASCINATING("흥미진진"),
    EXHILARATING("짜릿함"),
    ELATED("들뜸"),
    JOYFUL("기쁨"),
    PLEASANT("즐거움"),

    // 만족과 성취
    PROUD("뿌듯함"),
    ACCOMPLISHED("성취감"),
    SATISFIED("만족감"),
    FULFILLED("충만함"),
    CONFIDENT("자부심"),

    // 평온과 안정
    PEACEFUL("평온함"),
    CALM("차분함"),
    STABLE("안정감"),
    RELAXED("여유로움"),
    COMFORTABLE("편안함"),
    QUIET("고요함"),

    // 감동과 벅참
    MOVED("감동"),
    OVERWHELMED("벅차오름"),
    EMOTIONAL("먹먹함"),
    TOUCHED("뭉클함"),
    BITTER_SWEET("짠함"),

    // 자유와 해방
    FREE("자유로움"),
    LIBERATED("해방감"),
    REFRESHING("시원함"),
    CRISP("상쾌함"),

    // 새로움과 놀라움
    FRESH("신선함"),
    SURPRISED("놀라움"),
    AMAZED("경이로움"),
    CURIOUS("신기함"),

    // 그리움과 향수
    NOSTALGIC("그리움"),
    WISTFUL("향수"),
    REGRETFUL("아쉬움"),
    MELANCHOLY("아련함"),

    // 긴장과 스릴
    TENSE("긴장감"),
    TREMBLING("떨림"),
    POUNDING("두근거림"),
    THRILLING("스릴감"),

    // 깊이와 성찰
    SOLEMN("숙연함"),
    REVERENT("경건함"),
    REFLECTIVE("성찰적"),
    ENLIGHTENED("깨달음"),

    // 친밀과 소통
    FRIENDLY("친근함"),
    AFFECTIONATE("정겨움"),
    WARM("따뜻함"),
    INTIMATE("정다움"),
    BONDED("유대감");

    private final String description;

    PostEmotionTag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
