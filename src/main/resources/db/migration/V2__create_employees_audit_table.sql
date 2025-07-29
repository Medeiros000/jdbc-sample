CREATE TABLE employees_audit
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    employee_id   BIGINT,
    name          VARCHAR(150),
    old_name      VARCHAR(150),
    salary        DECIMAL(10, 2),
    old_salary    DECIMAL(10, 2),
    birthdate     DATE,
    old_birthdate DATE,
    operation     CHAR(1) CHECK (operation IN ('I', 'U', 'D')),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
