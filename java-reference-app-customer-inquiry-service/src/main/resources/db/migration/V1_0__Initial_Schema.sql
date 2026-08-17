create schema if not exists cis;

create table if not exists cis.account_type
(
    account_type_id bigint       not null
    primary key,
    description     varchar(255) not null,
    name            varchar(255) not null
    );

alter table cis.account_type
    owner to "user";

create table if not exists cis.customer_types
(
    customer_types_id bigint       not null
    primary key,
    description       varchar(255) not null,
    name              varchar(255) not null
    );

alter table cis.customer_types
    owner to "user";

create table if not exists cis.customer
(
    customer_id       bigint       not null
    primary key,
    email             varchar(255) not null,
    first_name        varchar(255),
    id_number         varchar(255) not null,
    last_name         varchar(255),
    password          varchar(255) not null,
    role              varchar(255) not null,
    customer_types_id bigint
    constraint fkr6l9397m2fu3g8571mo27lhjd
    references cis.customer_types
    );

alter table cis.customer
    owner to "user";

create table if not exists cis.customer_accounts
(
    customer_accounts_id bigint not null
    primary key,
    account_type_id      bigint
    constraint fkq80d0d6q1fnr82gr0m0301lba
    references cis.account_type,
    customer_id          bigint
    constraint fkcx0fjock6ehel4wul0c0mmeb5
    references cis.customer
);

alter table cis.customer_accounts
    owner to "user";

create table if not exists cis.document
(
    document_id bigint       not null
    primary key,
    document bytea not null
    );

alter table cis.document
    owner to "user";

create table if not exists cis.customer_documents
(
    customer_documents_id bigint not null
    primary key,
    customer_id           bigint
    constraint fkoqdywuig7fbo40qnfhlp8iam8
    references cis.customer,
    document_id           bigint
    constraint fk38gjfv8qavnr8152ur7fufcub
    references cis.document
);

alter table cis.customer_documents
    owner to "user";

