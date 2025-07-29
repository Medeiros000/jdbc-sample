package br.com.dio.dao;

import br.com.dio.entity.EmployeeEntity;
import br.com.dio.persistence.ConnectionUtil;
import com.mysql.cj.jdbc.StatementImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.LONG;

public class EmployeeParamDAO {

  public void insert(final EmployeeEntity entity) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.prepareStatement("INSERT INTO employees (name, salary, birthdate) VALUES (?, ?, ?);");
    ) {
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getSalary());
      statement.setTimestamp(3, Timestamp.valueOf(entity.getBirthDate().atZoneSameInstant(UTC).toLocalDateTime()));
      statement.executeUpdate();
      if (statement instanceof StatementImpl impl) {
        entity.setId(impl.getLastInsertID());
      }
      int affectedRows = statement.getUpdateCount();
      System.out.printf("Fetched %s employee record(s) from the database.%n", affectedRows);

    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }

  public void insertWithProcedure(final EmployeeEntity entity) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.prepareCall(" CALL prc_insert_employee (?, ?, ?, ?);");
    ) {
      statement.registerOutParameter(1, LONG);
      statement.setString(2, entity.getName());
      statement.setBigDecimal(3, entity.getSalary());
      statement.setTimestamp(4, Timestamp.valueOf(entity.getBirthDate().atZoneSameInstant(UTC).toLocalDateTime()));
      statement.execute();

      entity.setId(statement.getLong(1));

      int affectedRows = statement.getUpdateCount();
      System.out.printf("Fetched %s employee record(s) from the database.%n", affectedRows);

    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }

  public void insertBatch(final List<EmployeeEntity> entity) {
    try (var connection = ConnectionUtil.getConnection()) {
      var sql = "INSERT INTO employees (name, salary, birthdate) VALUES (?, ?, ?);";
      try (var statement = connection.prepareStatement(sql)) {
        connection.setAutoCommit(false);
        for (var entityEntity : entity) {
          statement.setString(1, entityEntity.getName());
          statement.setBigDecimal(2, entityEntity.getSalary());
          var timestamp = Timestamp.valueOf(entityEntity.getBirthDate().atZoneSameInstant(UTC).toLocalDateTime());
          statement.setTimestamp(3, timestamp);
          statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }


  public void update(final EmployeeEntity entity) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.prepareStatement("UPDATE employees set name = ?, salary = ?, birthdate = ? WHERE id = ?;");
    ) {
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getSalary());
      statement.setTimestamp(3, Timestamp.valueOf(entity.getBirthDate().atZoneSameInstant(UTC).toLocalDateTime()));
      statement.setLong(4, entity.getId());
      statement.executeUpdate();
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
        var statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?;");
    ) {
      statement.setLong(1, id);
      statement.executeUpdate();
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
        var statement = connection.prepareStatement("SELECT * FROM employees WHERE id = ?;");
    ) {
      statement.setLong(1, id);
      statement.executeQuery();
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
