CREATE TABLE user_settings (
    id             BIGSERIAL   NOT NULL,
    user_id        BIGINT      NOT NULL,
    two_fa_enabled BOOLEAN     NOT NULL DEFAULT FALSE,
    two_fa_method  VARCHAR(10),
    language       VARCHAR(10) NOT NULL DEFAULT 'en',
    CONSTRAINT pk_user_settings       PRIMARY KEY (id),
    CONSTRAINT uk_user_settings_user  UNIQUE (user_id),
    CONSTRAINT fk_user_settings_users FOREIGN KEY (user_id) REFERENCES users (id)
);
