package com.demo.employee.repository.employee;

import com.demo.commons.util.ObjectFactory;
import com.demo.employee.properties.EmployeeProperties;
import com.demo.employee.repository.employee.dao.EmployeeDAO;
import com.demo.employee.dto.EmployeeDTO;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeeDAO employeeDao;

  public EmployeeRepositoryImpl() {
    employeeDao = (EmployeeDAO) ObjectFactory.build(EmployeeProperties.getEmployeeDatabaseDAOClass());
  }

  @Override
  public List<EmployeeDTO> findLatestEmployees(int quantity) {
    return employeeDao.findLatestEmployees(quantity);
  }

  @Override
  public EmployeeDTO findByCode(int employeeCode) {
    return employeeDao.findByCode(employeeCode);
  }
}
