import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ProductLines")
public class ProductLine {
    @Id
    @Column(name = "productLine")
    private String productLine;

    @Column(name = "textDescription")
    private String textDescription;

    @Column(name = "htmlDescription")
    private String htmlDescription;

    @Transient
    private byte[] image;

    @OneToMany(mappedBy = "productLine")
    private List<Product> products = new ArrayList<>();

    // Getters and Setters
    public String getProductLine() { return productLine; }
    public void setProductLine(String productLine) { this.productLine = productLine; }

    public String getTextDescription() { return textDescription; }
    public void setTextDescription(String textDescription) { this.textDescription = textDescription; }

    public String getHtmlDescription() { return htmlDescription; }
    public void setHtmlDescription(String htmlDescription) { this.htmlDescription = htmlDescription; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
