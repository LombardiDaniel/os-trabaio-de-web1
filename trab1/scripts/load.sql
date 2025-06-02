BEGIN;
-- Adminer 5.3.0 PostgreSQL 16.6 dump

DROP FUNCTION IF EXISTS "delete_expired_sessions";;
CREATE FUNCTION "delete_expired_sessions" () RETURNS trigger LANGUAGE plpgsql AS '
BEGIN
    DELETE FROM auth_sessions
    WHERE exp < NOW();
    
    RETURN NEW;
END;
';

INSERT INTO "auth_sessions" ("session_id", "user_email", "exp") VALUES
('GDBdvcTJ34tipJCACbIHV9BZBdBAWO5A',	'admin@patos.dev',	'2025-05-30 14:50:20.782281+00'),
('8Qo5QBzU41t4DEcr1kZbzVqSgWmBLO7a',	'lombardi@patos.dev',	'2025-05-30 15:03:26.535859+00'),
('j68ZEdAtexAvuAFSMW7KmPdJGwHu9y6d',	'admin@patos.dev',	'2025-05-30 15:04:29.583538+00'),
('7WssBVZ5BsfPcPIa6UciaruAsey9xTT7',	'lombardi@patos.dev',	'2025-05-30 15:05:05.730836+00'),
('g38s8NYXnWsELOEh3DBPQvVd6pL50rzb',	'lombardi@patos.dev',	'2025-05-30 15:10:06.659363+00'),
('gmAlWPG8TR3SIMyBK7LTQxBaEFFYQSxx',	'admin@patos.dev',	'2025-05-30 15:22:05.718766+00'),
('y3fnWT9H85xS2jHqbu2D5ZjCvjsHrDvD',	'lombardi@patos.dev',	'2025-05-30 15:22:33.691128+00');

INSERT INTO "project_users" ("project_name", "user_email") VALUES
('aaa',	'admin@patos.dev'),
('test-project',	'lombardi@patos.dev'),
('test-project-for-lombas',	'lombardi@patos.dev');

INSERT INTO "projects" ("project_name", "created_at", "project_description") VALUES
('test-project',	'2025-05-26 10:15:14.874745+00',	'oi teste'),
('aaa',	'2025-05-26 11:51:52.402055+00',	'teste desc'),
('test-project-for-lombas',	'2025-05-30 14:22:20.818238+00',	'test');

INSERT INTO "strategies" ("strategy_name", "strategy_description", "examples", "hints", "images_urls_comma_sep") VALUES
('primeira',	'primeira estrategia',	'exemplo 1;',	'dica 1',	'');

INSERT INTO "test_session" ("session_id", "project_name", "tester_email", "strategy", "duration_minutes", "session_description", "session_status") VALUES
(1,	'test-project',	'lombardi@patos.dev',	'primeira',	0,	'',	'created');

INSERT INTO "users" ("email", "password_hash", "is_admin") VALUES
('admin@patos.dev',	'$2a$10$1JYcStrH28RDRFTXmljYxeXPsNbJHzM4hCaKuZ1WF547on7rcEq8i',	'1'),
('lombardi@patos.dev',	'$2a$10$1JYcStrH28RDRFTXmljYxeXPsNbJHzM4hCaKuZ1WF547on7rcEq8i',	'0');

-- 2025-05-30 14:25:47 UTC

COMMIT;