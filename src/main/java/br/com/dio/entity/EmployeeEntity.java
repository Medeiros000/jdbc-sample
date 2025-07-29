package br.com.dio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeEntity {
  private Long id;
  private String name;
  private BigDecimal salary;
  private OffsetDateTime birthDate;
}
