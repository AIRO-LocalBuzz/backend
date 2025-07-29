CREATE TABLE `shop_entity` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `created_at` datetime(6) DEFAULT NULL,
                               `updated_at` datetime(6) DEFAULT NULL,
                               `road_addr` varchar(512) DEFAULT NULL,
                               `lot_addr` varchar(512) DEFAULT NULL,
                               `new_zipcd` varchar(255) DEFAULT NULL,
                               `brch_nm` varchar(255) DEFAULT NULL,
                               `flr_no` varchar(255) DEFAULT NULL,
                               `ho_no` varchar(255) DEFAULT NULL,
                               `ksic_code` varchar(255) DEFAULT NULL,
                               `inds_lcls_cd` varchar(255) DEFAULT NULL,
                               `inds_mcls_cd` varchar(255) DEFAULT NULL,
                               `inds_scls_cd` varchar(255) DEFAULT NULL,
                               `latitude` double DEFAULT NULL,
                               `longitude` double DEFAULT NULL,
                               `ctprvn_cd` varchar(255) DEFAULT NULL,
                               `signgu_cd` varchar(255) DEFAULT NULL,
                               `adong_cd` varchar(255) DEFAULT NULL,
                               `ldong_cd` varchar(255) DEFAULT NULL,
                               `shop_name` varchar(200) NOT NULL,
                               `shop_type` enum('I1','I2') DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci