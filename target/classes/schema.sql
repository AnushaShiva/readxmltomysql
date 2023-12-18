DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `user_id` bigint DEFAULT NULL,
                        `first_name` varchar(100) DEFAULT NULL,
                        `last_name` varchar(100) DEFAULT NULL,
                        `age` bigint DEFAULT NULL,
                        `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB;