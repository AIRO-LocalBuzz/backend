package backend.airo.infra.promotion.service;

import backend.airo.domain.promotion.PromotionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class HtmlTemplateService {

    /**
     * PromotionResult를 HTML 템플릿으로 변환
     */
    public String generateHtmlTemplate(PromotionResult promotionResult) {
        log.info("HTML 템플릿 생성: {}", promotionResult.spotName());

        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap" rel="stylesheet">
                <style>
                    %s
                </style>
            </head>
            <body>
                <div class="promotion-container">
                    <div class="spot-name">%s</div>
                    <div class="title">%s</div>
                    <div class="tags">
                        %s
                    </div>
                    <div class="emotions">%s</div>
                </div>
            </body>
            </html>
            """,
                getStylesheet(),
                escapeHtml(promotionResult.spotName()),
                escapeHtml(promotionResult.suggestedTitle()),
                generateTagsHtml(promotionResult.recommendedTags()),
                escapeHtml(String.join(" • ", promotionResult.emotions()))
        );
    }

    /**
     * CSS 스타일시트 정의
     */
    private String getStylesheet() {
        return """
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            
            body {
                width: 1200px;
                height: 630px;
                font-family: 'Noto Sans KR', sans-serif;
                color: white;
                overflow: hidden;
                background: transparent;
            }
            
            .promotion-container {
                width: 100%;
                height: 100%;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                text-align: center;
                padding: 60px;
                background: linear-gradient(
                    135deg,
                    rgba(0, 0, 0, 0.7) 0%,
                    rgba(0, 0, 0, 0.4) 50%,
                    rgba(0, 0, 0, 0.7) 100%
                );
            }
            
            .spot-name {
                font-size: 32px;
                font-weight: 700;
                margin-bottom: 20px;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
                letter-spacing: -0.5px;
            }
            
            .title {
                font-size: 48px;
                font-weight: 500;
                margin-bottom: 30px;
                line-height: 1.3;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
                max-width: 900px;
                word-break: keep-all;
            }
            
            .tags {
                display: flex;
                flex-wrap: wrap;
                justify-content: center;
                gap: 12px;
                margin-bottom: 25px;
            }
            
            .tag {
                background: rgba(255, 255, 255, 0.2);
                backdrop-filter: blur(10px);
                border: 1px solid rgba(255, 255, 255, 0.3);
                padding: 8px 16px;
                border-radius: 25px;
                font-size: 16px;
                font-weight: 400;
                text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.6);
            }
            
            .emotions {
                font-size: 18px;
                font-weight: 300;
                opacity: 0.9;
                text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8);
                letter-spacing: 0.5px;
            }
            """;
    }

    /**
     * 태그 목록을 HTML로 변환
     */
    private String generateTagsHtml(java.util.List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return tags.stream()
                .map(tag -> String.format("<span class=\"tag\">#%s</span>", escapeHtml(tag)))
                .collect(Collectors.joining("\n"));
    }

    /**
     * HTML 이스케이프 처리
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}