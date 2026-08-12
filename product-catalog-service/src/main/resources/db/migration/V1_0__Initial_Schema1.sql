create schema if not exists pc;

create table if not exists pc.products
(
    product_id bigint not null primary key,
    name varchar(255) not null,
    description varchar null,
    price decimal not null,
    image_url varchar(255) null
);


create table if not exists pc.qualifying_customer_types
(
    qualifying_customer_types_id bigint not null   primary key,
    product_id bigint not null,
    constraint fk_qualifying_customer_types_products
    Foreign key (product_id)
    references pc.products,
    customer_types_id bigint    not null,
    constraint fk_qualifying_customer_types_customer_types
    Foreign key (customer_types_id)
    references cis.customer_types(customer_types_id)
);


create table if not exists pc.qualifying_accounts
(
    qualifying_accounts_id bigint not null   primary key,
    account_id bigint not null,
    constraint fk_qualifying_accounts_customer_accounts
    Foreign key (account_id)
    references cis.customer_accounts(customer_accounts_id),
    product_id bigint not null,
    constraint fk_qualifying_accounts_products
    Foreign key (product_id)
    references pc.products
);

create table if not exists pc.fulfilment_type
(
    fulfilment_type_id bigint not null   primary key,
    name varchar(255) not null,
    description varchar null,
    product_id bigint not null,
    constraint fk_qualifying_accounts_products
    Foreign key (product_id)
    references pc.products
);

create table if not exists pc.orders
(
    order_id bigint not null   primary key,
    customer_id bigint not null,
    created_at timestamp null,
    status varchar(255) not null,
    contract_url varchar(255) null,
    constraint fk_orders_customer
    Foreign key (customer_id)
    references cis.customer(customer_id)
);

create table if not exists pc.order_items
(
    order_items_id bigint not null   primary key,
    product_id bigint not null,
    constraint fk_order_items_products
    Foreign key (product_id)
    references pc.products,
    order_id bigint not null,
    constraint fk_order_items_orders
    Foreign key (order_id)
    references pc.orders
);

