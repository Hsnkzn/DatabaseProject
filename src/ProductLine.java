import javax.persistence.*;
import java.util.List;

@Entity
public class ProductLine {
    @Id
    private String productLine;
    private String textDescription;

    @OneToMany(mappedBy = "productLine")
    private List<Product> products;

    // Getters and setters

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
