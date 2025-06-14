import javax.persistence.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SaveToObjectDB {
    public static void main(String[] args) throws SQLException {
        // Read all data
        List<Office> offices = ReadFromSMarketDB.readOffices();
        Map<String, Office> officeMap = createOfficeMap(offices);

        // Employees need to be read in two passes for manager relationships
        List<Employee> employees = ReadFromSMarketDB.readEmployees(officeMap, new HashMap<>());
        Map<Integer, Employee> employeeMap = createEmployeeMap(employees);

        // Read employees again with complete employeeMap for manager relationships
        employees = ReadFromSMarketDB.readEmployees(officeMap, employeeMap);
        employeeMap = createEmployeeMap(employees); // Update the map with complete employee data

        List<Customer> customers = ReadFromSMarketDB.readCustomers(employeeMap);
        Map<Integer, Customer> customerMap = createCustomerMap(customers);

        List<ProductLine> productLines = ReadFromSMarketDB.readProductLines();
        Map<String, ProductLine> productLineMap = createProductLineMap(productLines);

        List<Product> products = ReadFromSMarketDB.readProducts(productLineMap);
        Map<String, Product> productMap = createProductMap(products);

        List<Order> orders = ReadFromSMarketDB.readOrders(customerMap);
        Map<Integer, Order> orderMap = createOrderMap(orders);

        List<OrderDetail> orderDetails = ReadFromSMarketDB.readOrderDetails(orderMap, productMap);
        List<Payment> payments = ReadFromSMarketDB.readPayments(customerMap);

        // Migration
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("objectdb:$objectdb/db/SMarket.odb");
            em = emf.createEntityManager();

            System.out.println("Starting migration...");
            em.getTransaction().begin();

            // Clear existing data in proper order (respecting foreign key constraints)
            System.out.println("Clearing existing data...");
            clearDatabase(em);

            // Persist all entities in proper order
            System.out.println("Persisting offices...");
            persistBatch(em, offices, 100);

            System.out.println("Persisting employees...");
            persistBatch(em, employees, 100);

            System.out.println("Persisting customers...");
            persistBatch(em, customers, 100);

            System.out.println("Persisting product lines...");
            persistBatch(em, productLines, 100);

            System.out.println("Persisting products...");
            persistBatch(em, products, 100);

            System.out.println("Persisting orders...");
            persistBatch(em, orders, 100);

            System.out.println("Persisting order details...");
            persistOrderDetailsBatch(em, orderDetails, orderMap, productMap, 100);

            System.out.println("Persisting payments...");
            persistPaymentsBatch(em, payments, customerMap, 100);

            em.getTransaction().commit();
            System.out.println("Migration completed successfully!");

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

        verifyMigration();
    }

    public static void clearDatabase(EntityManager em) {
        // Clear tables in reverse order of dependencies
        // Use try-catch for each delete in case the entity type doesn't exist yet
        try {
            em.createQuery("DELETE FROM OrderDetail").executeUpdate();
            System.out.println("Cleared OrderDetail entities");
        } catch (Exception e) {
            System.out.println("OrderDetail entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Payment").executeUpdate();
            System.out.println("Cleared Payment entities");
        } catch (Exception e) {
            System.out.println("Payment entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Order").executeUpdate();
            System.out.println("Cleared Order entities");
        } catch (Exception e) {
            System.out.println("Order entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Product").executeUpdate();
            System.out.println("Cleared Product entities");
        } catch (Exception e) {
            System.out.println("Product entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM ProductLine").executeUpdate();
            System.out.println("Cleared ProductLine entities");
        } catch (Exception e) {
            System.out.println("ProductLine entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Customer").executeUpdate();
            System.out.println("Cleared Customer entities");
        } catch (Exception e) {
            System.out.println("Customer entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Employee").executeUpdate();
            System.out.println("Cleared Employee entities");
        } catch (Exception e) {
            System.out.println("Employee entities not found or already empty");
        }

        try {
            em.createQuery("DELETE FROM Office").executeUpdate();
            System.out.println("Cleared Office entities");
        } catch (Exception e) {
            System.out.println("Office entities not found or already empty");
        }
    }

    static <T> void persistBatch(EntityManager em, List<T> entities, int batchSize) {
        for (int i = 0; i < entities.size(); i++) {
            em.persist(entities.get(i));
            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
                System.out.println("Processed " + i + " of " + entities.size() + " entities");
            }
        }
        // Final flush for remaining entities
        em.flush();
        em.clear();
        System.out.println("Completed persisting " + entities.size() + " entities");
    }

    // Special method for persisting OrderDetails with managed references
    static void persistOrderDetailsBatch(EntityManager em, List<OrderDetail> orderDetails,
                                         Map<Integer, Order> orderMap, Map<String, Product> productMap,
                                         int batchSize) {
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail od = orderDetails.get(i);

            // Get managed references from the persistence context
            Order managedOrder = em.find(Order.class, od.getOrder().getOrderNumber());
            Product managedProduct = em.find(Product.class, od.getProduct().getProductCode());

            if (managedOrder == null) {
                System.err.println("Order not found in ObjectDB: " + od.getOrder().getOrderNumber());
                continue;
            }

            if (managedProduct == null) {
                System.err.println("Product not found in ObjectDB: " + od.getProduct().getProductCode());
                continue;
            }

            // Create new OrderDetail with managed references
            OrderDetail managedOD = new OrderDetail();
            managedOD.setOrder(managedOrder);
            managedOD.setProduct(managedProduct);
            managedOD.setQuantityOrdered(od.getQuantityOrdered());
            managedOD.setPriceEach(od.getPriceEach());
            managedOD.setOrderLineNumber(od.getOrderLineNumber());

            em.persist(managedOD);

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
                System.out.println("Processed " + i + " of " + orderDetails.size() + " order details");
            }
        }
        // Final flush for remaining entities
        em.flush();
        em.clear();
        System.out.println("Completed persisting " + orderDetails.size() + " order details");
    }

    // Special method for persisting Payments with managed references
    static void persistPaymentsBatch(EntityManager em, List<Payment> payments,
                                     Map<Integer, Customer> customerMap, int batchSize) {
        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);

            // Get managed customer reference from the persistence context
            Customer managedCustomer = em.find(Customer.class, payment.getCustomer().getCustomerNumber());

            if (managedCustomer == null) {
                System.err.println("Customer not found in ObjectDB: " + payment.getCustomer().getCustomerNumber());
                continue;
            }

            // Create new Payment with managed reference
            Payment managedPayment = new Payment();
            managedPayment.setCustomer(managedCustomer);
            managedPayment.setCheckNumber(payment.getCheckNumber());
            managedPayment.setPaymentDate(payment.getPaymentDate());
            managedPayment.setAmount(payment.getAmount());

            em.persist(managedPayment);

            if (i % batchSize == 0 && i > 0) {
                em.flush();
                em.clear();
                System.out.println("Processed " + i + " of " + payments.size() + " payments");
            }
        }
        // Final flush for remaining entities
        em.flush();
        em.clear();
        System.out.println("Completed persisting " + payments.size() + " payments");
    }

    static void verifyMigration() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("objectdb:$objectdb/db/SMarket.odb");
            em = emf.createEntityManager();

            System.out.println("\nVerifying migration...");

            // Verify counts
            verifyCount(em, "Office", Office.class);
            verifyCount(em, "Employee", Employee.class);
            verifyCount(em, "Customer", Customer.class);
            verifyCount(em, "ProductLine", ProductLine.class);
            verifyCount(em, "Product", Product.class);
            verifyCount(em, "Order", Order.class);
            verifyCount(em, "OrderDetail", OrderDetail.class);
            verifyCount(em, "Payment", Payment.class);

            // Sample verification of relationships
            verifySampleRelationships(em);

        } catch (Exception e) {
            System.err.println("Verification failed:");
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }

    private static <T> void verifyCount(EntityManager em, String entityName, Class<T> entityClass) {
        try {
            Long count = em.createQuery("SELECT COUNT(e) FROM " + entityName + " e", Long.class)
                    .getSingleResult();
            System.out.println(entityName + " count: " + count);
        } catch (Exception e) {
            System.out.println(entityName + " count: 0 (entity not found or empty)");
        }
    }

    private static void verifySampleRelationships(EntityManager em) {
        System.out.println("\nSample relationship verification:");

        try {
            // Verify office-employee relationship
            TypedQuery<Office> officeQuery = em.createQuery(
                    "SELECT o FROM Office o WHERE SIZE(o.employees) > 0", Office.class);
            officeQuery.setMaxResults(1);
            List<Office> officesWithEmployees = officeQuery.getResultList();
            if (!officesWithEmployees.isEmpty()) {
                Office office = officesWithEmployees.get(0);
                System.out.println("Office " + office.getOfficeCode() + " has " +
                        office.getEmployees().size() + " employees");
            }
        } catch (Exception e) {
            System.out.println("Could not verify office-employee relationships");
        }

        try {
            // Verify customer orders
            TypedQuery<Customer> customerQuery = em.createQuery(
                    "SELECT c FROM Customer c WHERE SIZE(c.orders) > 0", Customer.class);
            customerQuery.setMaxResults(1);
            List<Customer> customersWithOrders = customerQuery.getResultList();
            if (!customersWithOrders.isEmpty()) {
                Customer customer = customersWithOrders.get(0);
                System.out.println("Customer " + customer.getCustomerNumber() + " has " +
                        customer.getOrders().size() + " orders");
            }
        } catch (Exception e) {
            System.out.println("Could not verify customer-order relationships");
        }

        try {
            // Verify order details
            TypedQuery<Order> orderQuery = em.createQuery(
                    "SELECT o FROM Order o WHERE SIZE(o.orderDetails) > 0", Order.class);
            orderQuery.setMaxResults(1);
            List<Order> ordersWithDetails = orderQuery.getResultList();
            if (!ordersWithDetails.isEmpty()) {
                Order order = ordersWithDetails.get(0);
                System.out.println("Order " + order.getOrderNumber() + " has " +
                        order.getOrderDetails().size() + " order details");
            }
        } catch (Exception e) {
            System.out.println("Could not verify order-orderdetail relationships");
        }
    }

    // Helper methods to create maps
    public static Map<String, Office> createOfficeMap(List<Office> offices) {
        return offices.stream()
                .collect(Collectors.toMap(Office::getOfficeCode, o -> o));
    }

    public static Map<Integer, Employee> createEmployeeMap(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(Employee::getEmployeeNumber, e -> e));
    }

    public static Map<Integer, Customer> createCustomerMap(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.toMap(Customer::getCustomerNumber, c -> c));
    }

    public static Map<String, ProductLine> createProductLineMap(List<ProductLine> productLines) {
        return productLines.stream()
                .collect(Collectors.toMap(ProductLine::getProductLine, pl -> pl));
    }

    public static Map<String, Product> createProductMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getProductCode, p -> p));
    }

    public static Map<Integer, Order> createOrderMap(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.toMap(Order::getOrderNumber, o -> o));
    }
}