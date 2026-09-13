CREATE TABLE event_publication (
    id                     UUID                     NOT NULL,
    listener_id            VARCHAR(255)             NOT NULL,
    event_type             VARCHAR(255)             NOT NULL,
    serialized_event       TEXT                     NOT NULL,
    publication_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date        TIMESTAMP WITH TIME ZONE,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    completion_attempts    INTEGER                  NOT NULL DEFAULT 0,
    status                 VARCHAR(255),
    CONSTRAINT pk_event_publication PRIMARY KEY (id)
);

CREATE TABLE event_publication_archive (
    id                     UUID                     NOT NULL,
    listener_id            VARCHAR(255)             NOT NULL,
    event_type             VARCHAR(255)             NOT NULL,
    serialized_event       TEXT                     NOT NULL,
    publication_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date        TIMESTAMP WITH TIME ZONE,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    completion_attempts    INTEGER                  NOT NULL DEFAULT 0,
    status                 VARCHAR(255),
    CONSTRAINT pk_event_publication_archive PRIMARY KEY (id)
);
