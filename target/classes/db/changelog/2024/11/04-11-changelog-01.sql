-- liquibase formatted sql

-- changeset mbakulev:1-1
INSERT INTO client (id, first_name, last_name, middle_name)
VALUES
    (1, 'Платонов', 'Сергей', 'Савельевич'),
    (2, 'Васильева', 'Алиса', 'Александровна'),
    (3, 'Титова', 'Анна', 'Платоновна'),
    (4, 'Абрамова', 'Полина', 'Георгиевна'),
    (5, 'Андреева', 'Полина', 'Марковна');