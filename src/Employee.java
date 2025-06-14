import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Employees")
public class Employee {
    @Id
    @Column(name = "employeeNumber")
    private Integer employeeNumber;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "email")
    private String email;

    @Column(name = "jobTitle")
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "officeCode")
    private Office office;

    @ManyToOne
    @JoinColumn(name = "reportsTo")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> subordinates = new ArrayList<>();

    @OneToMany(mappedBy = "salesRep")
    private List<Customer> customers = new ArrayList<>();

    // Getters and Setters
    public Integer getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(Integer employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public Office getOffice() { return office; }
    public void setOffice(Office office) { this.office = office; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }

    public List<Employee> getSubordinates() { return subordinates; }
    public void setSubordinates(List<Employee> subordinates) { this.subordinates = subordinates; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }
}
