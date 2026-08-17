INSERT INTO cis.customer_types (customer_types_id, description, name)
VALUES (nextval('cis.customer_types_sequence'), 'Customer Type for System-To-System integration','SYSTEM');

INSERT INTO cis.customer (customer_id, email, first_name, id_number, last_name, password, role, customer_types_id)
VALUES (nextval('cis.customer_sequence'), 'admin@entelect.co.za', 'admin', '', 'admin', '$2a$10$FlBVaadiSi9X//j.z/8fb.EWzBTEOY1aGjH4zTo/w3j8daOSGmAky', 'ADMIN', currval('cis.customer_types_sequence'));
