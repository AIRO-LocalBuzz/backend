-- ▼ 축제 정보 테이블
CREATE TABLE clutr_fatvl (
                             id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                             fstvl_nm         VARCHAR(255)    NOT NULL,          -- 축제명
                             opar             VARCHAR(255)    NULL,              -- 개최 장소
                             fstvl_start_date DATE            NULL,              -- 시작일
                             fstvl_end_date   DATE            NULL,              -- 종료일
                             fstvl_co         TEXT            NULL,              -- 축제 내용
                             mnnst_nm         VARCHAR(255)    NULL,              -- 주관 기관명
                             auspc_instt_nm   VARCHAR(255)    NULL,              -- 주최 기관명
                             suprt_instt_nm   VARCHAR(255)    NULL,              -- 후원 기관명
                             phone_number     VARCHAR(50)     NULL,              -- 전화번호
                             homepage_url     VARCHAR(512)    NULL,              -- 홈페이지 주소
                             relate_info      TEXT            NULL,              -- 관련 정보
                             rdnmadr          VARCHAR(512)    NULL,              -- 도로명 주소
                             lnmadr           VARCHAR(512)    NULL,              -- 지번 주소
                             latitude         DECIMAL(10,7)   NULL,              -- 위도  ±90.0000000
                             longitude        DECIMAL(10,7)   NULL,              -- 경도 ±180.0000000
                             reference_date   DATE            NULL,              -- 데이터 기준 일자
                             instt_code       VARCHAR(50)     NULL,              -- 제공 기관 코드
                             instt_nm         VARCHAR(255)    NULL,              -- 제공 기관 명

                             PRIMARY KEY (id),
                             INDEX idx_fstvl_nm (fstvl_nm),
                             INDEX idx_start_date (fstvl_start_date),
                             INDEX idx_location (latitude, longitude)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
