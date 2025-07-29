DELIMITER $$
CREATE PROCEDURE prc_insert_employee(
    OUT p_id BIGINT,
    IN p_name VARCHAR(150),
    IN p_salary DECIMAL(10, 2),
    IN p_birthdate TIMESTAMP
)
BEGIN
    INSERT INTO employees (name, salary, birthdate)
    VALUES (p_name, p_salary, p_birthdate);
    SET p_id = LAST_INSERT_ID();
END $$