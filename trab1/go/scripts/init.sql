BEGIN;

CREATE TABLE users (
    email VARCHAR(100) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE auth_sessions(
    session_id VARCHAR(255) PRIMARY KEY,
    user_email VARCHAR(100) REFERENCES users(email),
    exp TIMESTAMPTZ DEFAULT NOW() + INTERVAL '1 hour'
);

CREATE FUNCTION delete_expired_sessions()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM auth_sessions
    WHERE exp < NOW();
    
    RETURN NEW;
END;
$$ LANGUAGE PLpgSQL;

CREATE TRIGGER delete_expired_sessions
AFTER INSERT ON auth_sessions
FOR EACH STATEMENT EXECUTE FUNCTION delete_expired_sessions();

CREATE TABLE projects(
    project_name VARCHAR(100) PRIMARY KEY,
    created_at TIMESTAMPTZ DEFAULT NOW()
    projetct_description TEXT
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
