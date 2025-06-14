import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @Column(name = "customerNumber")
    private Integer customerNumber;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "contactLastName")
    private String contactLastName;

    @Column(name = "contactFirstName")
    private String contactFirstName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "creditLimit")
    private Double creditLimit;

    @ManyToOne
    @JoinColumn(name = "salesRepEmployeeNumber")
    private Employee salesRep;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Payment> payments = new ArrayList<>();

    // Getters and Setters
    public Integer getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(Integer customerNumber) { this.customerNumber = customerNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContactLastName() { return contactLastName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }

    public String getContactFirstName() { return contactFirstName; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(Double creditLimit) { this.creditLimit = creditLimit; }

    public Employee getSalesRep() { return salesRep; }
    public void setSalesRep(Employee salesRep) { this.salesRep = salesRep; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}
