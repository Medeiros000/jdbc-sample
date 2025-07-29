package br.com.dio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeEntity {

  private Long id;

  private String name;

  private BigDecimal salary;

  private OffsetDateTime birthDate;

  private List<ContactEntity> contacts;

  private List<ModuleEntity> modules;
}
