CREATE TABLE refresh_tokens (
    id         BIGSERIAL                NOT NULL,
    token      VARCHAR(512)             NOT NULL,
    user_id    BIGINT                   NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked    BOOLEAN                  NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_refresh_tokens       PRIMARY KEY (id),
    CONSTRAINT uk_refresh_tokens_token UNIQUE (token)
);
