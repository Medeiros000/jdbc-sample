DELIMITER $$

CREATE TRIGGER tgr_employee_audit_update
    BEFORE UPDATE
    ON employees
    FOR EACH ROW
BEGIN
    INSERT INTO employees_audit (employee_id,
                                 name,
                                 old_name,
                                 salary,
                                 old_salary,
                                 birthdate,
                                 old_birthdate,
                                 operation)
    VALUES (NEW.id,
            NEW.name,
            OLD.name,
            NEW.salary,
            OLD.salary,
            NEW.birthdate,
            OLD.birthdate,
            'U');
END$$

DELIMITER ;
