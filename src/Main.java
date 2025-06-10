import javax.persistence.*;
import java.io.File;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class Main {
    public static void main(String[] args) {
        // 1. Create EntityManagerFactory (embedded mode)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SMarketDB.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // 2. Read JSON (customers.json as example)
            ObjectMapper mapper = new ObjectMapper();
            List<Customer> customers = mapper.readValue(
                    new File("src/resources/customers.json"), new TypeReference<List<Customer>>() {});

            // 3. Start transaction
            em.getTransaction().begin();

            // 4. Persist each customer and their related orders
            for (Customer c : customers) {
                // Set customer reference in orders
                if (c.getOrders() != null) {
                    for (Order o : c.getOrders()) {
                        o.setCustomer(c);
                    }
                }
                em.persist(c);
            }

            // 5. Commit transaction
            em.getTransaction().commit();
            System.out.println("Import successful. Data saved to SMarketDB.odb");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. Cleanup
            em.close();
            emf.close();
        }
    }

}