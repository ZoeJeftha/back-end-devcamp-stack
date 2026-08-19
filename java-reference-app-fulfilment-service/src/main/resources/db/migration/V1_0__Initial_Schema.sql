create schema if not exists fulfilment;

create table if not exists fulfilment.fulfilment_product
(
    fulfilment_product_id bigint not null primary key,
    product_id bigint not null,
    fulfilment_type varchar null
);

