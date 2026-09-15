create schema if not exists pc;
CREATE SEQUENCE pc.customer_checks_sequence MINVALUE 1 START 1 INCREMENT BY 1;

create table if not exists pc.customer_checks
(
    customer_checks_id bigint not null primary key,
    description varchar(255) not null
);

create schema if not exists pc;
CREATE SEQUENCE pc.order_status_sequence MINVALUE 1 START 1 INCREMENT BY 1;

create table if not exists pc.order_status
(
    order_status_id bigint not null primary key,
    order_id bigint not null,
    customer_checks_id bigint null,
    has_passed boolean not null default false,
    constraint fk_order_status_customer_checks
    foreign key (customer_checks_id)
    references pc.customer_checks(customer_checks_id)
);

INSERT INTO pc.customer_checks (customer_checks_id, description)
VALUES
    (nextval('pc.customer_checks_sequence'), 'KYC Check'),
    (nextval('pc.customer_checks_sequence'), 'Fraud Check'),
    (nextval('pc.customer_checks_sequence'), 'Living Status Check'),
    (nextval('pc.customer_checks_sequence'), 'Duplicate Id Status Check'),
    (nextval('pc.customer_checks_sequence'), 'Marital Status Check'),
    (nextval('pc.customer_checks_sequence'), 'Credit Check');


