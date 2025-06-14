import javax.persistence.*;
import java.util.*;
import java.sql.Date;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @Column(name = "orderNumber")
    private Integer orderNumber;

    @Column(name = "orderDate")
    private Date orderDate;

    @Column(name = "requiredDate")
    private Date requiredDate;

    @Column(name = "shippedDate")
    private Date shippedDate;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "customerNumber")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // Getters and Setters
    public Integer getOrderNumber() { return orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public Date getRequiredDate() { return requiredDate; }
    public void setRequiredDate(Date requiredDate) { this.requiredDate = requiredDate; }

    public Date getShippedDate() { return shippedDate; }
    public void setShippedDate(Date shippedDate) { this.shippedDate = shippedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}
