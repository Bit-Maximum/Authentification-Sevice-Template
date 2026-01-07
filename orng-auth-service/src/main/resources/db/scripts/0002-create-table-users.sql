CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    is_enabled BOOLEAN      NOT NULL DEFAULT TRUE,
    password   VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_user_id ON users(id);
CREATE INDEX IF NOT EXISTS idx_username ON users(username);