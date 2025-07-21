USE `local_buzz`;

-- 1. users: 사용자 기본 정보
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       provider VARCHAR(20) NOT NULL,            -- ex) 'GOOGLE', 'KAKAO'
                       provider_id VARCHAR(100) NOT NULL UNIQUE, -- 소셜 로그인 고유 ID
                       nickname VARCHAR(50) NOT NULL UNIQUE,
                       name VARCHAR(50),
                       phone_number VARCHAR(20),
                       birth_date DATE,
                       email VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. user_points: 일별 포인트 적립 이력
CREATE TABLE user_points (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             point INT NOT NULL DEFAULT 0,
                             history_date DATE NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. user_reviews: 후기 작성 및 AI 분석 결과
CREATE TABLE user_reviews (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              content TEXT NOT NULL,
                              images JSON,                              -- 이미지 URL 배열
                              location VARCHAR(255),
                              emotion VARCHAR(30) NOT NULL,             -- 감정 문자열
                              summary TEXT,
                              hashtags JSON,                            -- 해시태그 배열
                              title VARCHAR(255),
                              translated_text TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. user_point_exchanges: 포인트 교환 이력
CREATE TABLE user_point_exchanges (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      item_name VARCHAR(100) NOT NULL,
                                      point_used INT NOT NULL,
                                      exchanged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. user_login_logs: 로그인 기록 (선택)
CREATE TABLE user_login_logs (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 user_id BIGINT NOT NULL,
                                 logged_in_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 ip_address VARCHAR(45),
                                 device_info VARCHAR(255),
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
