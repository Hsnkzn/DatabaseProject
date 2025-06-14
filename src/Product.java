import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Products")
public class Product {
    @Id
    @Column(name = "productCode")
    private String productCode;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productScale")
    private String productScale;

    @Column(name = "productVendor")
    private String productVendor;

    @Column(name = "productDescription")
    private String productDescription;

    @Column(name = "quantityInStock")
    private Integer quantityInStock;

    @Column(name = "buyPrice")
    private Double buyPrice;

    @Column(name = "MSRP")
    private Double MSRP;

    @ManyToOne
    @JoinColumn(name = "productLine")
    private ProductLine productLine;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // Getters and Setters
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductScale() { return productScale; }
    public void setProductScale(String productScale) { this.productScale = productScale; }

    public String getProductVendor() { return productVendor; }
    public void setProductVendor(String productVendor) { this.productVendor = productVendor; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public Integer getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }

    public Double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(Double buyPrice) { this.buyPrice = buyPrice; }

    public Double getMSRP() { return MSRP; }
    public void setMSRP(Double MSRP) { this.MSRP = MSRP; }

    public ProductLine getProductLine() { return productLine; }
    public void setProductLine(ProductLine productLine) { this.productLine = productLine; }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}
