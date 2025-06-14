import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Payments")
public class Payment {
    @Id
    @ManyToOne
    @JoinColumn(name = "customerNumber")
    private Customer customer;

    @Id
    @Column(name = "checkNumber")
    private String checkNumber;

    @Column(name = "paymentDate")
    private Date paymentDate;

    @Column(name = "amount")
    private Double amount;

    // Getters and Setters
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getCheckNumber() { return checkNumber; }
    public void setCheckNumber(String checkNumber) { this.checkNumber = checkNumber; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
