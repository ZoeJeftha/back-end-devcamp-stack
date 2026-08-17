create schema IF NOT EXISTS auth;

CREATE SEQUENCE IF NOT EXISTS auth.app_user_sequence MINVALUE 1 START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS auth.application_user (
    user_id BIGINT DEFAULT nextval('auth.app_user_sequence') PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);


INSERT INTO auth.application_user (user_id, email, password, role)
VALUES (nextval('auth.app_user_sequence'), 'admin@entelect.co.za', '$2a$10$FlBVaadiSi9X//j.z/8fb.EWzBTEOY1aGjH4zTo/w3j8daOSGmAky', 'admin');
