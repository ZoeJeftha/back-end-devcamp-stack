ALTER TABLE pc.products
    ADD COLUMN fulfilment_type varchar(1);

UPDATE pc.products
SET fulfilment_type = 'C'
WHERE product_id IN (1,2,3,4);

UPDATE pc.products
SET fulfilment_type = 'A'
WHERE product_id = 5;

UPDATE pc.products
SET fulfilment_type = 'C'
WHERE product_id IN (6,7,8,9);