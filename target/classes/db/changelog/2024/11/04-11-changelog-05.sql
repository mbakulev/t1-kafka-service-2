-- liquibase formatted sql

-- changeset mbakulev:5-1
INSERT INTO transaction(id, account_id, amount, timestamp)
VALUES
    (1, 1, 100.00, '2024-11-04 01:00:00.000'),
    (2, 2, 200.00, '2024-11-04 02:00:00.000'),
    (3, 3, 300.00, '2024-11-04 03:00:00.000'),
    (4, 4, 400.00, '2024-11-04 04:00:00.000'),
    (5, 5, 500.00, '2024-11-04 05:00:00.000');