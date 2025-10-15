-- DATABASE EXTENSIONS 
CREATE EXTENSION IF NOT EXISTS pg_cron;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS citext;

-- DATABASE INDEXES

DROP INDEX IF EXISTS idx_user_username;
DROP INDEX IF EXISTS idx_user_email;
DROP INDEX IF EXISTS idx_user_username_lower;
DROP INDEX IF EXISTS idx_user_email_lower;
DROP INDEX IF EXISTS idx_history_login_time;
DROP INDEX IF EXISTS idx_history_user_id;


CREATE INDEX IF NOT EXISTS idx_user_username_citext ON users (username);
CREATE INDEX IF NOT EXISTS idx_user_email_citext ON users (email);

CREATE INDEX IF NOT EXISTS idx_user_username_trgm ON users USING GIN (username gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_user_email_trgm ON users USING GIN (email gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_tokens_user_id ON tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_tokens_token ON tokens (token);
CREATE INDEX IF NOT EXISTS idx_token_user_validity ON tokens (user_id, expired, revoked);

CREATE INDEX IF NOT EXISTS idx_history_user_time_desc ON login_history (user_id, login_time DESC);