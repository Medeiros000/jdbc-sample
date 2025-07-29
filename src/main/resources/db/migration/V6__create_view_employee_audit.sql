CREATE VIEW view_employee_audit AS
SELECT employee_id,
       name,
       old_name,
       salary,
       old_salary,
       birthdate,
       old_birthdate,
       operation
FROM employees_audit
ORDER BY created_at;
