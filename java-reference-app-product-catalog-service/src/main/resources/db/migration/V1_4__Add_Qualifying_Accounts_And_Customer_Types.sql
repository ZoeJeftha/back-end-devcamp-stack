ALTER TABLE pc.qualifying_accounts
    DROP CONSTRAINT IF EXISTS fk_qualifying_accounts_customer_accounts;

ALTER TABLE pc.qualifying_accounts
    ADD CONSTRAINT fk_qualifying_accounts_account_type
        FOREIGN KEY (account_id)
            REFERENCES cis.account_type(account_type_id);

CREATE SEQUENCE pc.qualifying_customer_types_sequence MINVALUE 1 START 1 INCREMENT BY 1;

INSERT INTO pc.qualifying_customer_types (qualifying_customer_types_id, product_id, customer_types_id)
VALUES
    (nextval('pc.qualifying_customer_types_sequence'),
     1,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     2,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     3,2),
    (nextval('pc.qualifying_customer_types_sequence'),
     3,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     3,4),
    (nextval('pc.qualifying_customer_types_sequence'),
     4,2),
    (nextval('pc.qualifying_customer_types_sequence'),
     4,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     4,4),
    (nextval('pc.qualifying_customer_types_sequence'),
     5,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     5,2),
    (nextval('pc.qualifying_customer_types_sequence'),
     5,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     5,4),
    (nextval('pc.qualifying_customer_types_sequence'),
     6,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     6,2),
    (nextval('pc.qualifying_customer_types_sequence'),
     6,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     6,4),
    (nextval('pc.qualifying_customer_types_sequence'),
     7,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     7,2),
    (nextval('pc.qualifying_customer_types_sequence'),
     7,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     7,4),
    (nextval('pc.qualifying_customer_types_sequence'),
     8,1),
    (nextval('pc.qualifying_customer_types_sequence'),
     8,3),
    (nextval('pc.qualifying_customer_types_sequence'),
     9,1);

CREATE SEQUENCE pc.qualifying_accounts_sequence MINVALUE 1 START 1 INCREMENT BY 1;

INSERT INTO pc.qualifying_accounts (qualifying_accounts_id, account_id, product_id)
VALUES
    (nextval('pc.qualifying_accounts_sequence'),
     1,1),
    (nextval('pc.qualifying_accounts_sequence'),
     2,1),
    (nextval('pc.qualifying_accounts_sequence'),
     3,1),
    (nextval('pc.qualifying_accounts_sequence'),
     4,1),

    (nextval('pc.qualifying_accounts_sequence'),
     1,2),
    (nextval('pc.qualifying_accounts_sequence'),
     2,2),
    (nextval('pc.qualifying_accounts_sequence'),
     3,2),
    (nextval('pc.qualifying_accounts_sequence'),
     4,2),

    (nextval('pc.qualifying_accounts_sequence'),
     6,3),
    (nextval('pc.qualifying_accounts_sequence'),
     7,3),
    (nextval('pc.qualifying_accounts_sequence'),
     8,3),

    (nextval('pc.qualifying_accounts_sequence'),
     6,4),
    (nextval('pc.qualifying_accounts_sequence'),
     7,4),
    (nextval('pc.qualifying_accounts_sequence'),
     8,4),

    (nextval('pc.qualifying_accounts_sequence'),
     1,5),
    (nextval('pc.qualifying_accounts_sequence'),
     2,5),
    (nextval('pc.qualifying_accounts_sequence'),
     3,5),
    (nextval('pc.qualifying_accounts_sequence'),
     4,5),
    (nextval('pc.qualifying_accounts_sequence'),
     5,5),

    (nextval('pc.qualifying_accounts_sequence'),
     1,6),
    (nextval('pc.qualifying_accounts_sequence'),
     2,6),
    (nextval('pc.qualifying_accounts_sequence'),
     4,6),

    (nextval('pc.qualifying_accounts_sequence'),
     1,7),
    (nextval('pc.qualifying_accounts_sequence'),
     2,7),
    (nextval('pc.qualifying_accounts_sequence'),
     4,7),

    (nextval('pc.qualifying_accounts_sequence'),
     4,8),

    (nextval('pc.qualifying_accounts_sequence'),
     3,9);



