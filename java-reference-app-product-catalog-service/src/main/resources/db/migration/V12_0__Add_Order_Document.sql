create schema if not exists pc;
CREATE SEQUENCE pc.order_document_sequence MINVALUE 1 START 1 INCREMENT BY 1;

create table if not exists pc.order_document
(
    order_document_id bigint not null primary key,
    customer_id bigint not null,
    document BYTEA not null,
    constraint fk_order_document_customer
    Foreign key (customer_id)
    references cis.customer(customer_id)
);


