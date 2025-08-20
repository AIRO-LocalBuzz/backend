package backend.airo.infra.promotion;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
public class PlaywrightBrowserPool {

    @Value("${thumbnail.browser.pool-size:3}")
    private int poolSize;

    @Value("${thumbnail.browser.timeout:10}")
    private int timeoutSeconds;

    @Value("${thumbnail.browser.headless:true}")
    private boolean headless;

    private Playwright playwright;
    private BlockingQueue<Browser> browserPool;
    private Semaphore semaphore;
    private volatile boolean isInitialized = false;

    @PostConstruct
    public void initialize() {
        try {
            log.info("Playwright 브라우저 풀 초기화 시작 (풀 크기: {})", poolSize);

            playwright = Playwright.create();
            browserPool = new LinkedBlockingQueue<>();
            semaphore = new Semaphore(poolSize);

            // 브라우저 인스턴스들을 미리 생성
            for (int i = 0; i < poolSize; i++) {
                Browser browser = createBrowser();
                browserPool.offer(browser);
                log.debug("브라우저 인스턴스 {} 생성 완료", i + 1);
            }

            isInitialized = true;
            log.info("Playwright 브라우저 풀 초기화 완료");

        } catch (Exception e) {
            log.error("브라우저 풀 초기화 실패", e);
            throw new RuntimeException("브라우저 풀 초기화에 실패했습니다", e);
        }
    }

    /**
     * 브라우저를 사용하여 작업 실행
     */
    public <T> T executeWithBrowser(Function<Browser, T> task) {
        if (!isInitialized) {
            throw new IllegalStateException("브라우저 풀이 초기화되지 않았습니다");
        }

        Browser browser = null;
        try {
            // 세마포어로 동시 접근 제어
            if (!semaphore.tryAcquire(timeoutSeconds, TimeUnit.SECONDS)) {
                throw new RuntimeException("브라우저 획득 타임아웃: " + timeoutSeconds + "초");
            }

            // 브라우저 인스턴스 획득
            browser = browserPool.poll(timeoutSeconds, TimeUnit.SECONDS);
            if (browser == null) {
                throw new RuntimeException("사용 가능한 브라우저 인스턴스가 없습니다");
            }

            // 브라우저가 닫혔다면 새로 생성
            if (!browser.isConnected()) {
                log.warn("브라우저 연결이 끊어져서 새로 생성합니다");
                browser.close();
                browser = createBrowser();
            }

            log.debug("브라우저 인스턴스 사용 시작");
            return task.apply(browser);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("브라우저 작업이 중단되었습니다", e);
        } catch (Exception e) {
            log.error("브라우저 작업 실행 중 오류 발생", e);

            // 오류 발생 시 브라우저 재생성
            if (browser != null && browser.isConnected()) {
                try {
                    browser.close();
                    browser = createBrowser();
                } catch (Exception closeError) {
                    log.error("브라우저 재생성 실패", closeError);
                }
            }

            throw new RuntimeException("브라우저 작업 실행에 실패했습니다", e);
        } finally {
            // 브라우저 반납
            if (browser != null) {
                try {
                    browserPool.offer(browser);
                    log.debug("브라우저 인스턴스 반납 완료");
                } catch (Exception e) {
                    log.error("브라우저 반납 중 오류", e);
                }
            }
            semaphore.release();
        }
    }

    /**
     * 새로운 브라우저 인스턴스 생성
     */
    private Browser createBrowser() {
        try {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(headless)
                    .setArgs(java.util.List.of(
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--disable-gpu",
                            "--disable-extensions",
                            "--disable-plugins",
                            "--disable-images", // 이미지 로딩 비활성화로 성능 향상
                            "--disable-javascript" // JavaScript 비활성화로 성능 향상
                    ));

            return playwright.chromium().launch(launchOptions);
        } catch (Exception e) {
            log.error("브라우저 생성 실패", e);
            throw new RuntimeException("브라우저 생성에 실패했습니다", e);
        }
    }

    /**
     * 풀 상태 정보 조회
     */
    public PoolStatus getPoolStatus() {
        return new PoolStatus(
                poolSize,
                browserPool.size(),
                semaphore.availablePermits(),
                isInitialized
        );
    }

    /**
     * 리소스 정리
     */
    @PreDestroy
    public void cleanup() {
        log.info("브라우저 풀 정리 시작");

        isInitialized = false;

        try {
            // 모든 브라우저 인스턴스 정리
            Browser browser;
            while ((browser = browserPool.poll()) != null) {
                try {
                    if (browser.isConnected()) {
                        browser.close();
                    }
                } catch (Exception e) {
                    log.error("브라우저 정리 중 오류", e);
                }
            }

            // Playwright 정리
            if (playwright != null) {
                playwright.close();
            }

            log.info("브라우저 풀 정리 완료");
        } catch (Exception e) {
            log.error("브라우저 풀 정리 중 오류 발생", e);
        }
    }

    /**
     * 풀 상태 정보 클래스
     */
    public static class PoolStatus {
        public final int totalSize;
        public final int availableBrowsers;
        public final int availablePermits;
        public final boolean isInitialized;

        public PoolStatus(int totalSize, int availableBrowsers, int availablePermits, boolean isInitialized) {
            this.totalSize = totalSize;
            this.availableBrowsers = availableBrowsers;
            this.availablePermits = availablePermits;
            this.isInitialized = isInitialized;
        }

        @Override
        public String toString() {
            return String.format("PoolStatus{total=%d, available=%d, permits=%d, initialized=%s}",
                    totalSize, availableBrowsers, availablePermits, isInitialized);
        }
    }
}