import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(OrderDetailPK.class)
public class OrderDetail {
    @Id
    private int orderNumber;

    @Id
    private String productCode;

    private int quantityOrdered;
    private double priceEach;
    private int orderLineNumber;

    @ManyToOne
    @JoinColumn(name = "orderNumber", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productCode", insertable = false, updatable = false)
    private Product product;

    // Getters and setters

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(double priceEach) {
        this.priceEach = priceEach;
    }

    public int getOrderLineNumber() {
        return orderLineNumber;
    }

    public void setOrderLineNumber(int orderLineNumber) {
        this.orderLineNumber = orderLineNumber;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
