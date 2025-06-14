import javax.persistence.*;
import java.util.*;

public class DatabaseMigration {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // Initialize ObjectDB connection
            emf = Persistence.createEntityManagerFactory("objectdb:smarket.odb");
            em = emf.createEntityManager();
            
            System.out.println("Starting database migration...");
            em.getTransaction().begin();

            // Read all data from SQL Server
            System.out.println("Reading data from SQL Server...");
            
            // Read offices first
            List<Office> offices = ReadFromSMarketDB.readOffices();
            Map<String, Office> officeMap = SaveToObjectDB.createOfficeMap(offices);

            // Read employees (needs two passes for manager relationships)
            List<Employee> employees = ReadFromSMarketDB.readEmployees(officeMap, new HashMap<>());
            Map<Integer, Employee> employeeMap = SaveToObjectDB.createEmployeeMap(employees);
            employees = ReadFromSMarketDB.readEmployees(officeMap, employeeMap);
            employeeMap = SaveToObjectDB.createEmployeeMap(employees);

            // Read remaining data
            List<Customer> customers = ReadFromSMarketDB.readCustomers(employeeMap);
            Map<Integer, Customer> customerMap = SaveToObjectDB.createCustomerMap(customers);

            List<ProductLine> productLines = ReadFromSMarketDB.readProductLines();
            Map<String, ProductLine> productLineMap = SaveToObjectDB.createProductLineMap(productLines);

            List<Product> products = ReadFromSMarketDB.readProducts(productLineMap);
            Map<String, Product> productMap = SaveToObjectDB.createProductMap(products);

            List<Order> orders = ReadFromSMarketDB.readOrders(customerMap);
            Map<Integer, Order> orderMap = SaveToObjectDB.createOrderMap(orders);

            List<OrderDetail> orderDetails = ReadFromSMarketDB.readOrderDetails(orderMap, productMap);
            List<Payment> payments = ReadFromSMarketDB.readPayments(customerMap);

            // Clear existing data
            System.out.println("Clearing existing data...");
            SaveToObjectDB.clearDatabase(em);

            // Persist all data to ObjectDB
            System.out.println("Persisting data to ObjectDB...");
            SaveToObjectDB.persistBatch(em, offices, 100);
            SaveToObjectDB.persistBatch(em, employees, 100);
            SaveToObjectDB.persistBatch(em, customers, 100);
            SaveToObjectDB.persistBatch(em, productLines, 100);
            SaveToObjectDB.persistBatch(em, products, 100);
            SaveToObjectDB.persistBatch(em, orders, 100);
            SaveToObjectDB.persistOrderDetailsBatch(em, orderDetails, orderMap, productMap, 100);
            SaveToObjectDB.persistPaymentsBatch(em, payments, customerMap, 100);

            em.getTransaction().commit();
            System.out.println("Migration completed successfully!");

            // Verify the migration
            SaveToObjectDB.verifyMigration();

        } catch (Exception e) {
            System.err.println("Migration failed:");
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("Transaction rolled back due to errors");
            }
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
} 