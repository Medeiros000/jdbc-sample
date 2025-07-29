package br.com.dio.dao;

import br.com.dio.persistence.ConnectionUtil;

import java.sql.SQLException;

public class AccessDAO {

  public void insert(final long employeeId, final long moduleId) {
    try (
        var connection = ConnectionUtil.getConnection();
        var statement = connection.prepareStatement("INSERT INTO accesses (employee_id, module_id) VALUES (?, ?);");
    ) {
      statement.setLong(1, employeeId);
      statement.setLong(2, moduleId);
      statement.executeUpdate();
      int affectedRows = statement.getUpdateCount();
      System.out.printf("Fetched %s employee record(s) from the database.%n", affectedRows);

    } catch (SQLException e) {
      System.err.println("Error inserting employee: " + e.getMessage());
    }
  }
}
