CREATE TABLE IF NOT EXISTS tokens
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT                   NOT NULL,
    refresh_jti        UUID                     NOT NULL UNIQUE,
    access_jti         UUID                     NOT NULL UNIQUE,
    refresh_issued_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    refresh_expired_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_refresh_tokens_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE

);

CREATE INDEX IF NOT EXISTS idx_tokens_refresh_jti ON tokens (refresh_jti);
