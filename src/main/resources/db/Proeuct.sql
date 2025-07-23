CREATE TABLE product (
                         id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                         item_name        VARCHAR(255)    NOT NULL,
                         item_url         VARCHAR(512)    NOT NULL,
                         item_price       BIGINT UNSIGNED NOT NULL,
                         item_description TEXT            NULL,

                         created_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,

                         PRIMARY KEY (id),
                         INDEX idx_item_name (item_name)
)
