-- liquibase formatted sql

-- changeset mbakulev:3-1
INSERT INTO account (id, client_id, account_type, balance)
VALUES
    (1, 1, 'DEBIT', 1000.00),
    (2, 2, 'DEBIT', 2000.00),
    (3, 3, 'DEBIT', 3000.00),
    (4, 4, 'CREDIT', 4000.00),
    (5, 5, 'CREDIT', 5000.00);
