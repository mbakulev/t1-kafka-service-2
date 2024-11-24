-- liquibase formatted sql

-- changeset mbakulev:7-1
ALTER TABLE account
    ADD status VARCHAR(255);
ALTER TABLE account
    ADD frozen_amount DECIMAL;

-- changeset mbakulev:7-2
UPDATE account SET status = 'OPEN';
UPDATE account SET frozen_amount = 0;