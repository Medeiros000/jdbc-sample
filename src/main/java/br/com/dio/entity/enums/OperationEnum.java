package br.com.dio.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum OperationEnum {
  INSERT,
  UPDATE,
  DELETE;

  public static OperationEnum getOperation(String operation) {
    return Stream.of(OperationEnum.values())
        .filter(o -> o.name().startsWith(operation.toUpperCase()))
        .findFirst()
        .orElseThrow();
  }
}
