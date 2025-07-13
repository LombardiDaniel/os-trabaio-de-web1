-- Adminer 5.3.0 MySQL 9.3.0 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

INSERT INTO `project_tester` (`tester_id`, `project_id`) VALUES
    (1,	1);

INSERT INTO `projects` (`id`, `created_at`, `project_description`, `project_name`, `updated_at`) VALUES
    (1,	'2025-06-20 19:21:33.000000',	'My test project',	'First Project',	'2025-06-20 19:21:33.000000');

INSERT INTO `strategies` (`id`, `description`, `examples`, `hints`, `name`) VALUES
    (1,	'just a simple strategy for demonstration purposes',	'hm...',	'no hint as of now',	'Demo Strategy');

INSERT INTO `test_sessions` (`id`, `status`, `project_id`, `strategy_id`, `tester_id`) VALUES
    (1,	'CREATED',	1,	1,	1);

INSERT INTO `testers` (`id`, `created_at`, `email`, `firstname`, `is_user_admin`, `lastname`, `updated_at`) VALUES
    (1,	'2025-06-20 19:20:40.000000',	'lombardi@patos.dev',	'Daniel',	CONVERT(b'1', UNSIGNED),	'Lombardi',	'2025-06-20 19:20:40.000000');


-- 2025-06-20 19:38:15 UTC