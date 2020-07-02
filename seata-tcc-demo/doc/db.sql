# Order
DROP DATABASE IF EXISTS seata_tcc_order;
CREATE DATABASE seata_tcc_order;
CREATE TABLE seata_tcc_order.orders
(
    id               BIGINT(11)        NOT NULL,
    user_id          BIGINT(11)        DEFAULT NULL,
    product_id       BIGINT(11)        DEFAULT NULL,
    number           INT(11)        DEFAULT NULL,
    status           VARCHAR(100)   DEFAULT NULL,
    add_time         DATETIME       DEFAULT CURRENT_TIMESTAMP,
    last_update_time DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;

# Storage
DROP DATABASE IF EXISTS seata_tcc_storage;
CREATE DATABASE seata_tcc_storage;
CREATE TABLE seata_tcc_storage.product
(
    id               BIGINT(11) NOT NULL AUTO_INCREMENT,
    price            DOUBLE   DEFAULT NULL,
    stock            INT(11)  DEFAULT NULL,
    trans_stock      INT(11)  DEFAULT NULL,
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;
INSERT INTO seata_tcc_storage.product (id, price, stock, trans_stock)
VALUES (1, 5, 10, 0);
CREATE TABLE seata_tcc_storage.product_unique
(
    unique_key varchar(255) NOT NULL,
    PRIMARY KEY (unique_key) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

# Account
DROP DATABASE IF EXISTS seata_tcc_account;
CREATE DATABASE seata_tcc_account;
CREATE TABLE seata_tcc_account.account
(
    id               INT(11) NOT NULL AUTO_INCREMENT,
    balance          DOUBLE   DEFAULT NULL,
    trans_balance    DOUBLE   DEFAULT NULL,
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;
INSERT INTO seata_tcc_account.account (id, balance, trans_balance)
VALUES (1, 20, 0);
CREATE TABLE seata_tcc_account.account_unique
(
    unique_key varchar(255) NOT NULL,
    PRIMARY KEY (unique_key) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
