CREATE TABLE user_preferences (
    id                BIGSERIAL    NOT NULL,
    user_id           BIGINT       NOT NULL,
    notification_type VARCHAR(20)  NOT NULL,
    channel           VARCHAR(20)  NOT NULL,
    enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_user_preferences       PRIMARY KEY (id),
    CONSTRAINT uk_user_preferences_combo UNIQUE (user_id, notification_type, channel),
    CONSTRAINT fk_user_preferences_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_user_preferences_routing ON user_preferences (user_id, notification_type);
