import javax.persistence.*;
import java.util.List;

public class VerifyMigration {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // Connect to the ObjectDB database
            emf = Persistence.createEntityManagerFactory("objectdb:$objectdb/db/SMarket.odb");
            em = emf.createEntityManager();

            // Query and display counts for each entity type
            System.out.println("\n=== Migration Results ===");
            
            // Count Offices
            TypedQuery<Long> officeQuery = em.createQuery("SELECT COUNT(o) FROM Office o", Long.class);
            System.out.println("Number of Offices: " + officeQuery.getSingleResult());

            // Count Employees
            TypedQuery<Long> employeeQuery = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
            System.out.println("Number of Employees: " + employeeQuery.getSingleResult());

            // Count Customers
            TypedQuery<Long> customerQuery = em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class);
            System.out.println("Number of Customers: " + customerQuery.getSingleResult());

            // Count ProductLines
            TypedQuery<Long> productLineQuery = em.createQuery("SELECT COUNT(pl) FROM ProductLine pl", Long.class);
            System.out.println("Number of Product Lines: " + productLineQuery.getSingleResult());

            // Count Products
            TypedQuery<Long> productQuery = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
            System.out.println("Number of Products: " + productQuery.getSingleResult());

            // Count Orders
            TypedQuery<Long> orderQuery = em.createQuery("SELECT COUNT(o) FROM Order o", Long.class);
            System.out.println("Number of Orders: " + orderQuery.getSingleResult());

            // Count OrderDetails
            TypedQuery<Long> orderDetailQuery = em.createQuery("SELECT COUNT(od) FROM OrderDetail od", Long.class);
            System.out.println("Number of Order Details: " + orderDetailQuery.getSingleResult());

            // Count Payments
            TypedQuery<Long> paymentQuery = em.createQuery("SELECT COUNT(p) FROM Payment p", Long.class);
            System.out.println("Number of Payments: " + paymentQuery.getSingleResult());

            // Display some sample data
            System.out.println("\n=== Sample Data ===");
            
            // Sample Office
            TypedQuery<Office> sampleOfficeQuery = em.createQuery("SELECT o FROM Office o", Office.class);
            sampleOfficeQuery.setMaxResults(1);
            Office sampleOffice = sampleOfficeQuery.getSingleResult();
            System.out.println("\nSample Office:");
            System.out.println("Office Code: " + sampleOffice.getOfficeCode());
            System.out.println("City: " + sampleOffice.getCity());
            System.out.println("Country: " + sampleOffice.getCountry());

            // Sample Product
            TypedQuery<Product> sampleProductQuery = em.createQuery("SELECT p FROM Product p", Product.class);
            sampleProductQuery.setMaxResults(1);
            Product sampleProduct = sampleProductQuery.getSingleResult();
            System.out.println("\nSample Product:");
            System.out.println("Product Code: " + sampleProduct.getProductCode());
            System.out.println("Product Name: " + sampleProduct.getProductName());
            System.out.println("Product Line: " + sampleProduct.getProductLine().getProductLine());

        } catch (Exception e) {
            System.err.println("Error verifying migration:");
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
} 