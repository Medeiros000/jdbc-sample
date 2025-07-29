package br.com.dio.dao;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.dio.entity.EmployeeEntity;
import br.com.dio.persistence.ConnectionUtil;
import com.mysql.cj.jdbc.StatementImpl;

import static java.time.ZoneOffset.UTC;

public class EmployeeDAO {

  public void insert(final EmployeeEntity entity) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      var sql = "INSERT INTO employees (name, salary, birthdate) VALUES ('" +
          entity.getName() + "', " +
          entity.getSalary().toString() + ", '" +
          formatOffsetDateTime(entity.getBirthDate()) + "'); ";
      statement.execute(sql);
      if (statement instanceof StatementImpl impl) {
        entity.setId(impl.getLastInsertID());
      }
      int affectedRows = statement.getUpdateCount();
      System.out.printf("Fetched %s employee record(s) from the database.%n", affectedRows);

    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }

  public void update(final EmployeeEntity entity) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      var sql = "UPDATE employees set " +
          "name = '" + entity.getName() + "', " +
          "salary = " + entity.getSalary() + ", " +
          "birthdate = '" + formatOffsetDateTime(entity.getBirthDate()) + "' " +
          "WHERE id = " + entity.getId();
      statement.execute(sql);
      if (statement instanceof StatementImpl impl) {
        entity.setId(impl.getLastInsertID());
      }
      int affectedRows = statement.getUpdateCount();
      System.out.printf("Updated %s employee record(s) from the database.%n", affectedRows);

    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }

  public void delete(final long id) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      var sql = "DELETE FROM employees WHERE id = " + id;
      statement.execute(sql);
    } catch (SQLException e) {
      System.err.println("Error in deleting employee: " + e.getMessage());
    }
  }

  public List<EmployeeEntity> findAll() {
    List<EmployeeEntity> employees = new ArrayList<>();
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      statement.executeQuery("SELECT * FROM employees ORDER BY name");
      var resultSet = statement.getResultSet();
      while (resultSet.next()) {
        var entity = new EmployeeEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));
        entity.setSalary(resultSet.getBigDecimal("salary"));
        var birthdateIstant = resultSet.getTimestamp("birthdate").toInstant();
        entity.setBirthDate(OffsetDateTime.ofInstant(birthdateIstant, UTC));
        employees.add(entity);
      }
    } catch (SQLException e) {
      System.err.println("Error in fetching all employees: " + e.getMessage());
    }
    return employees;
  }

  public EmployeeEntity findById(final long id) {
    var entity = new EmployeeEntity();
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.createStatement();
    ) {
      statement.executeQuery("SELECT * FROM employees WHERE id = " + id);
      var resultSet = statement.getResultSet();
      if (resultSet.next()) {
        entity.setId(resultSet.getLong("id"));
        entity.setName(resultSet.getString("name"));
        entity.setSalary(resultSet.getBigDecimal("salary"));
        var birthdateIstant = resultSet.getTimestamp("birthdate").toInstant();
        entity.setBirthDate(OffsetDateTime.ofInstant(birthdateIstant, UTC));
      }
    } catch (SQLException e) {
      System.err.println("Error in fetching all employees: " + e.getMessage());
    }
    return entity;
  }

  private String formatOffsetDateTime(final OffsetDateTime dateTime) {
    var utcDateTime = dateTime.withOffsetSameInstant(UTC);
    return utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
