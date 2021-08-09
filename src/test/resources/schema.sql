CREATE TABLE `users` (
  `id`      integer(10) NOT NULL,
  `name`         varchar(255) NOT NULL,
  `deleted_time` timestamp NULL DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;