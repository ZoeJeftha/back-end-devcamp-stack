INSERT INTO  cis.account_type (account_type_id, description, name)
VALUES (nextval('cis.account_type_sequence'), 'Cheque Account for Small Businesses', 'SME Checking Account');

INSERT INTO  cis.account_type (account_type_id, description, name)
VALUES (nextval('cis.account_type_sequence'), 'Cheque Account for Medium Businesses', 'Medium Enterprise Checking Account');

INSERT INTO  cis.account_type (account_type_id, description, name)
VALUES (nextval('cis.account_type_sequence'), 'Cheque Account for Large Businesses', 'Large Enterprise Checking Account');