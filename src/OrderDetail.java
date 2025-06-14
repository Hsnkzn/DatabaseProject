import javax.persistence.*;

@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "orderNumber")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "productCode")
    private Product product;

    @Column(name = "quantityOrdered")
    private Integer quantityOrdered;

    @Column(name = "priceEach")
    private Double priceEach;

    @Column(name = "orderLineNumber")
    private Integer orderLineNumber;

    // Getters and Setters
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(Integer quantityOrdered) { this.quantityOrdered = quantityOrdered; }

    public Double getPriceEach() { return priceEach; }
    public void setPriceEach(Double priceEach) { this.priceEach = priceEach; }

    public Integer getOrderLineNumber() { return orderLineNumber; }
    public void setOrderLineNumber(Integer orderLineNumber) { this.orderLineNumber = orderLineNumber; }
}
