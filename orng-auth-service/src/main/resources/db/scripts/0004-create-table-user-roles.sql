CREATE TABLE IF NOT EXISTS "user_role"
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role UNIQUE (user_id, role_id),

    CONSTRAINT fk_user_role_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_role_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE

);
