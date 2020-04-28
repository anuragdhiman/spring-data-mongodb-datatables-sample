package sample.employee;

import org.springframework.data.mongodb.datatables.DataTablesRepository;

public interface EmployeeRepository extends DataTablesRepository<Employee, Integer> {

}
