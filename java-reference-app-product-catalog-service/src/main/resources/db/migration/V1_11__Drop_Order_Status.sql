
DROP TABLE IF EXISTS order_status;
DROP SEQUENCE IF EXISTS pc.order_status_sequence;

create schema if not exists pc;
CREATE SEQUENCE pc.order_customer_checks_sequence MINVALUE 1 START 1 INCREMENT BY 1;

create table if not exists pc.order_customer_checks
(
    order_customer_checks_id bigint not null primary key,
    order_id bigint not null,
    customer_checks_id bigint null,
    has_passed boolean not null default false,
    constraint fk_order_status_customer_checks
    foreign key (customer_checks_id)
    references pc.customer_checks(customer_checks_id)
);
