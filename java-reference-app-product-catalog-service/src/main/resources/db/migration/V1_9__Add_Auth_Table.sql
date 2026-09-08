create schema if not exists pc;
CREATE SEQUENCE pc.user_sequence MINVALUE 1 START 1 INCREMENT BY 1;

create table if not exists pc.user
(
    user_id bigint not null primary key,
    email varchar(255) not null,
    password varchar null,
    role varchar(255) null
);

