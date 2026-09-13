CREATE TABLE users (
    id            BIGSERIAL                NOT NULL,
    uuid          UUID                     NOT NULL,
    username      VARCHAR(50)              NOT NULL,
    email         VARCHAR(255)             NOT NULL,
    password_hash VARCHAR(60)              NOT NULL,
    display_name  VARCHAR(100),
    bio           VARCHAR(500),
    avatar_url    VARCHAR(500),
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_users          PRIMARY KEY (id),
    CONSTRAINT uk_users_uuid     UNIQUE (uuid),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email    UNIQUE (email)
);
