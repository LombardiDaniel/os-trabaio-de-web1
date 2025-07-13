SELECT
    'SET FOREIGN_KEY_CHECKS = 0;' AS stmt
UNION ALL
SELECT
    CONCAT('DROP TABLE IF EXISTS `', table_name, '`;') AS stmt
FROM information_schema.tables
WHERE table_schema = DATABASE()
UNION ALL
SELECT
    'SET FOREIGN_KEY_CHECKS = 1;' AS stmt;