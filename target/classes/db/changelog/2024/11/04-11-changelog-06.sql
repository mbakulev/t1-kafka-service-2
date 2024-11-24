-- liquibase formatted sql

-- changeset mbakulev:6-1
CREATE SEQUENCE IF NOT EXISTS data_source_error_log_sequense START WITH 1 INCREMENT BY 50;

-- changeset mbakulev:6-2
CREATE TABLE IF NOT EXISTS data_source_error_log
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_data_source_error_log PRIMARY KEY (id)
);

-- changeset mbakulev:6-3
ALTER TABLE data_source_error_log
    ADD message TEXT;
ALTER TABLE data_source_error_log
    ADD stacktrace TEXT;
ALTER TABLE data_source_error_log
    ADD method_name VARCHAR(255);
