package br.com.dio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import static java.time.ZoneOffset.UTC;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.flywaydb.core.Flyway;

import br.com.dio.dao.EmployeeAuditDAO;
import br.com.dio.dao.EmployeeParamDAO;
import br.com.dio.entity.EmployeeEntity;
import net.datafaker.Faker;

public class Main {

  private final static EmployeeParamDAO employeeParamDAO = new EmployeeParamDAO();
  private final static EmployeeAuditDAO employeeAuditDAO = new EmployeeAuditDAO();
  private final static Faker faker = new Faker(Locale.of("pt", "BR"));

  private static void stopAndGo(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    var flyway = Flyway.configure()
        .dataSource("jdbc:mysql://localhost/jdbc_sample", "root", "")
        .load();
    flyway.migrate();
    stopAndGo(1);

    var entities = Stream.generate(() -> {
      var employee = new EmployeeEntity();
      employee.setName(faker.name().fullName());
      employee.setSalary(new BigDecimal(faker.number().digits(4)));
      employee.setBirthDate(OffsetDateTime.of(
          faker.timeAndDate().birthday(1, 50),
          LocalTime.MIN,
          UTC));
      return employee;
    }).limit(4000).toList();

    employeeParamDAO.insertBatch(entities);

    /*var result = employeeDAO.findAll();
    result.forEach(System.out::println);

    var employee1 = new EmployeeEntity();
    employee1.setName("Junior");
    employee1.setSalary(new BigDecimal("3500"));
    employee1.setBirthDate(OffsetDateTime.now().minusYears(44));
    System.out.println(employee1);
    employeeDAO.insertWithProcedure(employee1);
    System.out.println(employee1);
    stopAndGo(1);

    var employee2 = new EmployeeEntity();
    employee2.setName("Joao");
    employee2.setSalary(new BigDecimal("2800"));
    employee2.setBirthDate(OffsetDateTime.now().minusYears(18));
    System.out.println(employee2);
    employeeDAO.insertWithProcedure(employee2);
    System.out.println(employee2);
    stopAndGo(1);

    var employee3 = new EmployeeEntity();
    employee3.setName("Miguel");
    employee3.setSalary(new BigDecimal("4800"));
    employee3.setBirthDate(OffsetDateTime.now().minusYears(24));
    System.out.println(employee3);
    employeeDAO.insertWithProcedure(employee3);
    System.out.println(employee3);
    stopAndGo(1);

    var employeeDB = employeeDAO.findById(1);
    System.out.println(employeeDB);
    employeeDB.setName("Júnior");
    employeeDB.setSalary(new BigDecimal(4000));
    employeeDB.setBirthDate(OffsetDateTime.now().minusYears(44).plusMonths(3).plusDays(12));
    employeeDAO.update(employeeDB);
    stopAndGo(1);

    employeeDAO.delete(3);
    stopAndGo(1);

    var found = employeeDAO.findById(1);
    System.out.println(found);
    result = employeeDAO.findAll();
//    result.stream().map(c -> c.getId() + 2).forEach(System.out::println);
//    result.stream().filter(c -> c.getId() % 2 == 0).forEach(System.out::println);
    result.forEach(System.out::println);

    employeeAuditDAO.findAll().forEach(System.out::println);*/

  }
}