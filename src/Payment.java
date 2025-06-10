import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(PaymentPK.class)
public class Payment {
    @Id
    private int customerNumber;

    @Id
    private String checkNumber;

    private Date paymentDate;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "customerNumber", insertable = false, updatable = false)
    private Customer customer;

    // Getters and setters

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
