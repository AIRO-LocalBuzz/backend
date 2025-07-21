package backend.airo.domain.user.query;

import lombok.Getter;

@Getter
public class GetUserByFirebaseUidQuery {
    private final String firebaseUid;

    public GetUserByFirebaseUidQuery(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
}