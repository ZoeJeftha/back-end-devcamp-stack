create schema if not exists f;
CREATE SEQUENCE f.failed_message_sequence MINVALUE 1 START 1 INCREMENT BY 1;


create table if not exists f.failed_messages
(
    failed_message_id bigint not null primary key,
    message_id varchar(255) not null unique ,
    original_queue varchar(255) not null,
    payload text not null,
    failure_reason text,
    retry_count integer,
    status varchar(50) not null,
    created_at timestamp not null,
    replayed_at timestamp,
    replayed_by varchar(255)
);
