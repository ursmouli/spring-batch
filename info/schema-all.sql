DROP TABLE account IF EXISTS;
DROP TABLE customer IF EXISTS;

CREATE TABLE customer (
    customer_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    middle_name VARCHAR(20)
);

CREATE TABLE account (
    account_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    account_name VARCHAR(20),
    account_number VARCHAR(20),
    fk_customer_id BIGINT,
    FOREIGN KEY (fk_customer_id) REFERENCES customer(customer_id)
);
