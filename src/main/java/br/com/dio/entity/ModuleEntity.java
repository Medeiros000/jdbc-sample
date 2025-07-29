package br.com.dio.entity;

import lombok.Data;

import java.util.List;

@Data
public class ModuleEntity {

  private long id;

  private String name;

  private List<EmployeeEntity> employees;
}
