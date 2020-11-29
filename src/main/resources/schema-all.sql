DROP TABLE route IF EXISTS;

CREATE TABLE route  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    origin VARCHAR(10),
    destination VARCHAR(10),
    cost NUMBER
);
