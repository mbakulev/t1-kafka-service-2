-- liquibase formatted sql

-- changeset mbakulev:8-1
ALTER TABLE transaction
    ADD status VARCHAR(255);

-- changeset mbakulev:8-2
UPDATE transaction SET status = 'ACCECPTED';