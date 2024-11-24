-- liquibase formatted sql

-- changeset mbakulev:4-1
CREATE SEQUENCE IF NOT EXISTS transaction_sequense START WITH 6 INCREMENT BY 50;

-- changeset mbakulev:4-2
CREATE TABLE IF NOT EXISTS transaction
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

-- changeset mbakulev:4-3
ALTER TABLE transaction
    ADD account_id BIGINT;
ALTER TABLE transaction
    ADD timestamp TIMESTAMP;
ALTER TABLE transaction
    ADD amount DECIMAL;
ALTER TABLE transaction
    ADD CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id);
