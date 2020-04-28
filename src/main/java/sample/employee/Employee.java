package sample.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String position;
    private int age;
    private int salary;
    private Office office;

}