DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS comp_event CASCADE;


CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS location (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    annotation VARCHAR(2024) NOT NULL,
    category_id BIGINT REFERENCES categories(id) on delete cascade,
    confirmed_request BIGINT,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(8048) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    location_id BIGINT REFERENCES location(id) on delete cascade,
    user_id BIGINT REFERENCES users(id) on delete cascade,
    paid BOOLEAN NOT NULL,
    participant_limit BIGINT,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR(32) NOT NULL,
    views BIGINT,
    title VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    state VARCHAR(124) NOT NULL,
    event BIGINT REFERENCES events(id) on delete cascade,
    requester BIGINT REFERENCES users(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS comp_event (
    compilation_id BIGINT REFERENCES compilations(id) on delete cascade,
    event_id BIGINT REFERENCES events(id) on delete cascade
);

