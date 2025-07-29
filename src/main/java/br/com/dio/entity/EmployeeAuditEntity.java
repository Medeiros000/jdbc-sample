package br.com.dio.entity;

import br.com.dio.entity.enums.OperationEnum;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EmployeeAuditEntity(
    long employeeId,
    String name,
    String oldName,
    BigDecimal salary,
    BigDecimal oldSalary,
    OffsetDateTime birthDate,
    OffsetDateTime oldBirthDate,
    OperationEnum operation
) {
}
