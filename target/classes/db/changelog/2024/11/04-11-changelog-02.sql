-- liquibase formatted sql

-- changeset mbakulev:2-1
CREATE SEQUENCE IF NOT EXISTS account_sequense START WITH 6 INCREMENT BY 50;

-- changeset mbakulev:2-2
CREATE TABLE IF NOT EXISTS account
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id)
);

-- changeset mbakulev:2-3
ALTER TABLE account
    ADD client_id BIGINT;
ALTER TABLE account
    ADD account_type VARCHAR(255);
ALTER TABLE account
    ADD balance DECIMAL;
ALTER TABLE account
    ADD CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES client(id);