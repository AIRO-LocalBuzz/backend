package backend.airo.support;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeCatch {

    private long startTime;
    private String label;

    public TimeCatch() {
        this.label = "";
    }

    public TimeCatch(String label) {
        this.label = label;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public long end() {
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        if (label != null && !label.isEmpty()) {
            log.info("[{}] 실행 시간: {}ms", label, elapsed);
        } else {
            log.info("실행 시간: {}ms", elapsed);
        }
        return elapsed;
    }

    public static long measure(Runnable task, String label) {
        long start = System.currentTimeMillis();
        task.run();
        long end = System.currentTimeMillis();
        long elapsed = end - start;
        log.info("[{}] 실행 시간: {}ms", label, elapsed);
        return elapsed;
    }
}
