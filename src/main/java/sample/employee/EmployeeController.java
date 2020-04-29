package sample.employee;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.DataTablesInput;
import org.springframework.data.mongodb.datatables.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class EmployeeController {
    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public DataTablesOutput<Employee> list(@Valid DataTablesInput input) {
        return employeeRepository.findAll(input);
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public DataTablesOutput<Employee> listPOST(@Valid @RequestBody DataTablesInput input) {
        return employeeRepository.findAll(input);
    }

    @RequestMapping(value = "/employees-advanced", method = RequestMethod.GET)
    public DataTablesOutput<Employee> listAdvanced(@Valid DataTablesInput input) {
        Criteria salaryCriteria = parseSalaryFilter(input);
        Criteria excludeAnalysts = where("position").ne("Analyst");
        return employeeRepository.findAll(input, salaryCriteria, excludeAnalysts);
    }

    private Pattern SALARY_PATTERN = Pattern.compile("(\\d+)?;(\\d+)?");

    private Criteria parseSalaryFilter(DataTablesInput input) {
        return input.getColumn("salary")
                .map(column -> {
                    String salaryFilter = column.getSearch().getValue();
                    Matcher matcher = SALARY_PATTERN.matcher(salaryFilter);
                    if (!matcher.matches()) {
                        return null;
                    }
                    String minSalary = matcher.group(1);
                    String maxSalary = matcher.group(2);
                    if (minSalary == null && maxSalary == null) {
                        return null;
                    }
                    Criteria criteria = where("salary");
                    if (minSalary != null) {
                        criteria.gte(Integer.parseInt(minSalary));
                    }
                    if (maxSalary != null) {
                        criteria.lte(Integer.parseInt(maxSalary));
                    }
                    return criteria;
                }).orElse(null);
    }

}