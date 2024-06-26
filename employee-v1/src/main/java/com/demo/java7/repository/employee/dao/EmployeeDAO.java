package com.demo.java7.repository.employee.dao;

import com.demo.java7.commons.database.config.MySQLConnection;
import com.demo.java7.dto.EmployeeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Patrón de diseño: Data Access Object (DAO)
 * <p>
 * <br/>Clase DAO que permite interactuar directamente con la fuente de datos, haciendo uso de instrucciones nativas
 * SQL, NOSQL u otras tecnologías para acceder a la fuente de datos.<br/>
 */
public class EmployeeDAO {

  private Connection connection = null;
  private PreparedStatement statement = null;
  private ResultSet result = null;

  public EmployeeDTO findByCode(int employeeCode) {
    try {
      connection = MySQLConnection.getConnection();
      connection.setAutoCommit(false);
      statement = connection.prepareStatement("SELECT code, name, contract_date, department_code FROM employees WHERE code = ? ");
      statement.setInt(1, employeeCode);
      result = statement.executeQuery();

      EmployeeDTO employee = new EmployeeDTO();
      if (result.next()) {
        employee.setCode(result.getInt("code"));
        employee.setName(result.getString("name"));
        employee.setContractDate(result.getDate("contract_date"));
        employee.setDepartmentCode(result.getInt("department_code"));
      }
      connection.commit();
      return employee;

    } catch (Exception exception) {
      rollback();
      throw new RuntimeException("error to find employees by department code: " + exception.getMessage());

    } finally {
      closeResources();
    }
  }

  public List<EmployeeDTO> findLatestEmployees(int latest) {
    try {
      connection = MySQLConnection.getConnection();
      connection.setAutoCommit(false);

      //sql instruction is valid only for MySQL
      String selectQuery = "SELECT code, name, contract_date, department_code ";
      selectQuery += "FROM employees ";
      selectQuery += "ORDER BY contract_date DESC ";
      selectQuery += "LIMIT ? ";

      statement = connection.prepareStatement(selectQuery);
      statement.setInt(1, latest);
      result = statement.executeQuery();

      List<EmployeeDTO> employeeList = new ArrayList<>();
      while (result.next()) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setCode(result.getInt("code"));
        employee.setName(result.getString("name"));
        employee.setContractDate(result.getDate("contract_date"));
        employee.setDepartmentCode(result.getInt("department_code"));

        employeeList.add(employee);
      }
      connection.commit();
      return employeeList;

    } catch (Exception exception) {
      rollback();
      throw new RuntimeException("error to find latest employees: " + exception.getMessage());

    } finally {
      closeResources();
    }
  }

  private void rollback() {
    try {
      if (connection != null) {
        connection.rollback();
      }
    } catch (Exception exception) {
      throw new RuntimeException("error to rollback: " + exception.getMessage());
    }
  }

  private void closeResources() {
    try {
      if (statement != null) {
        statement.close();
      }
      if (result != null) {
        result.close();
      }
    } catch (Exception exception) {
      throw new RuntimeException("error to close resources: " + exception.getMessage());
    }
  }

}