package br.com.dio.dao;

import br.com.dio.entity.EmployeeAuditEntity;
import br.com.dio.entity.enums.OperationEnum;
import br.com.dio.persistence.ConnectionUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.isNull;

public class EmployeeAuditDAO {
  public List<EmployeeAuditEntity> findAll() {
    List<EmployeeAuditEntity> entities = new ArrayList<>();
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      statement.executeQuery("SELECT * FROM view_employee_audit");
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        entities.add(new EmployeeAuditEntity(
            resultSet.getLong("employee_id"),
            resultSet.getString("name"),
            resultSet.getString("old_name"),
            resultSet.getBigDecimal("salary"),
            resultSet.getBigDecimal("old_salary"),
            getDateTimeOrNull(resultSet, "birthdate"),
            getDateTimeOrNull(resultSet, "old_birthdate"),
            OperationEnum.getOperation(resultSet.getString("operation"))
        ));
      }
    } catch (SQLException e) {
      System.err.println("Error in fetching all employees: " + e.getMessage());
    }
    return entities;
  }

  private static OffsetDateTime getDateTimeOrNull(ResultSet resultSet, final String field) throws SQLException {
    return isNull(resultSet.getTimestamp(field)) ? null :
        ofInstant(resultSet.getTimestamp(field).toInstant(), UTC);
  }
}
