BEGIN;

CREATE TABLE users (
    email VARCHAR(100) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE projects(
    project_name VARCHAR(100) PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE project_users(
    project_name VARCHAR(100) REFERENCES projects(project_name) ON DELETE CASCADE,
    user_email VARCHAR(100) REFERENCES users(email) ON DELETE CASCADE,
    PRIMARY KEY (project_name, user_email)
);

CREATE TABLE strategies(
    name VARCHAR(100) PRIMARY KEY
);

CREATE TABLE test_session(
    session_id SERIAL PRIMARY KEY,
    tester_email VARCHAR(100) REFERENCES users(email),
    strategy VARCHAR(100) REFERENCES strategies(name),
    duration_minutes INTEGER,
    session_description TEXT,
    is_active BOOLEAN DEFAULT true
);

COMMIT;
