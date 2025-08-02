package backend.airo.domain.point_history.command;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.repository.PointHistoryRepository;
import backend.airo.domain.point_history.vo.PointType;
import org.hibernate.exception.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePointHistoryCommand {

    private final PointHistoryRepository pointHistoryRepository;

    public boolean handle(Long userId, Long point, Long postId, PointType pointType) {
        String idemKey = "REVIEW:" + postId + ":" + userId;
        try {
            pointHistoryRepository.save(
                    new PointHistory(
                            0L,
                            point,
                            userId,
                            pointType,
                            postId,
                            idemKey,
                            null
                    ));
            return true;
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateKey(e)) {
                return false; // 이미 존재(멱등)
            }
            throw e;
        }
    }
    private boolean isDuplicateKey(DataIntegrityViolationException e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof ConstraintViolationException cve) {
                String state = cve.getSQLState();
                int code = cve.getErrorCode();
                if ("23505".equals(state) || "23000".equals(state) || code == 1062 || code == 1) return true;
            }
            t = t.getCause();
        }
        String msg = e.getMessage();
        return msg != null && (msg.contains("Duplicate entry")
                || msg.contains("duplicate key value")
                || msg.contains("unique constraint"));
    }
}
