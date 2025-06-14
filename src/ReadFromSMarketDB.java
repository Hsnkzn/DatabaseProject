import java.sql.*;
import java.util.*;
import javax.persistence.*;

public class ReadFromSMarketDB {
    // Connection string for SQL Server with Windows Authentication
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=SMarketDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    // Load the SQL Server JDBC driver
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Office> readOffices() throws SQLException {
        List<Office> offices = new ArrayList<>();
        String query = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM Offices";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Office office = new Office();
                office.setOfficeCode(rs.getString("officeCode"));
                office.setCity(rs.getString("city"));
                office.setPhone(rs.getString("phone"));
                office.setAddressLine1(rs.getString("addressLine1"));
                office.setAddressLine2(rs.getString("addressLine2"));
                office.setState(rs.getString("state"));
                office.setCountry(rs.getString("country"));
                office.setPostalCode(rs.getString("postalCode"));
                office.setTerritory(rs.getString("territory"));
                offices.add(office);
            }
        }
        return offices;
    }

    public static List<Employee> readEmployees(Map<String, Office> officeMap, Map<Integer, Employee> employeeMap) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle FROM Employees";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastName"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setJobTitle(rs.getString("jobTitle"));

                // Set office reference
                String officeCode = rs.getString("officeCode");
                if (officeMap != null && officeMap.containsKey(officeCode)) {
                    employee.setOffice(officeMap.get(officeCode));
                }

                // Set manager reference
                int reportsTo = rs.getInt("reportsTo");
                if (employeeMap != null && employeeMap.containsKey(reportsTo)) {
                    employee.setManager(employeeMap.get(reportsTo));
                }

                employees.add(employee);
            }
        }
        return employees;
    }

    public static List<Customer> readCustomers(Map<Integer, Employee> employeeMap) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, " +
                      "addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM Customers";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerNumber(rs.getInt("customerNumber"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setContactLastName(rs.getString("contactLastName"));
                customer.setContactFirstName(rs.getString("contactFirstName"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddressLine1(rs.getString("addressLine1"));
                customer.setAddressLine2(rs.getString("addressLine2"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setPostalCode(rs.getString("postalCode"));
                customer.setCountry(rs.getString("country"));
                customer.setCreditLimit(rs.getDouble("creditLimit"));

                // Set sales rep reference
                int salesRepNumber = rs.getInt("salesRepEmployeeNumber");
                if (employeeMap != null && employeeMap.containsKey(salesRepNumber)) {
                    customer.setSalesRep(employeeMap.get(salesRepNumber));
                }

                customers.add(customer);
            }
        }
        return customers;
    }

    public static List<ProductLine> readProductLines() throws SQLException {
        List<ProductLine> productLines = new ArrayList<>();
        String query = "SELECT productLine, textDescription, htmlDescription FROM ProductLines";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                ProductLine productLine = new ProductLine();
                productLine.setProductLine(rs.getString("productLine"));
                productLine.setTextDescription(rs.getString("textDescription"));
                productLine.setHtmlDescription(rs.getString("htmlDescription"));
                productLines.add(productLine);
            }
        }
        return productLines;
    }

    public static List<Product> readProducts(Map<String, ProductLine> productLineMap) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT productCode, productName, productLine, productScale, productVendor, " +
                      "productDescription, quantityInStock, buyPrice, MSRP FROM Products";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductCode(rs.getString("productCode"));
                product.setProductName(rs.getString("productName"));
                product.setProductScale(rs.getString("productScale"));
                product.setProductVendor(rs.getString("productVendor"));
                product.setProductDescription(rs.getString("productDescription"));
                product.setQuantityInStock(rs.getInt("quantityInStock"));
                product.setBuyPrice(rs.getDouble("buyPrice"));
                product.setMSRP(rs.getDouble("MSRP"));

                // Set product line reference
                String productLine = rs.getString("productLine");
                if (productLineMap != null && productLineMap.containsKey(productLine)) {
                    product.setProductLine(productLineMap.get(productLine));
                }

                products.add(product);
            }
        }
        return products;
    }

    public static List<Order> readOrders(Map<Integer, Customer> customerMap) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderNumber(rs.getInt("orderNumber"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setRequiredDate(rs.getDate("requiredDate"));
                order.setShippedDate(rs.getDate("shippedDate"));
                order.setStatus(rs.getString("status"));
                order.setComments(rs.getString("comments"));
                
                // Set customer reference
                int customerNumber = rs.getInt("customerNumber");
                if (customerMap != null && customerMap.containsKey(customerNumber)) {
                    order.setCustomer(customerMap.get(customerNumber));
                }

                orders.add(order);
            }
        }
        return orders;
    }

    public static List<OrderDetail> readOrderDetails(Map<Integer, Order> orderMap, Map<String, Product> productMap) throws SQLException {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String query = "SELECT orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber FROM OrderDetails";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantityOrdered(rs.getInt("quantityOrdered"));
                orderDetail.setPriceEach(rs.getDouble("priceEach"));
                orderDetail.setOrderLineNumber(rs.getInt("orderLineNumber"));

                // Set order reference
                int orderNumber = rs.getInt("orderNumber");
                if (orderMap != null && orderMap.containsKey(orderNumber)) {
                    orderDetail.setOrder(orderMap.get(orderNumber));
                }

                // Set product reference
                String productCode = rs.getString("productCode");
                if (productMap != null && productMap.containsKey(productCode)) {
                    orderDetail.setProduct(productMap.get(productCode));
                }

                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }

    public static List<Payment> readPayments(Map<Integer, Customer> customerMap) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT customerNumber, checkNumber, paymentDate, amount FROM Payments";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setCheckNumber(rs.getString("checkNumber"));
                payment.setPaymentDate(rs.getDate("paymentDate"));
                payment.setAmount(rs.getDouble("amount"));

                // Set customer reference
                int customerNumber = rs.getInt("customerNumber");
                if (customerMap != null && customerMap.containsKey(customerNumber)) {
                    payment.setCustomer(customerMap.get(customerNumber));
                }

                payments.add(payment);
            }
        }
        return payments;
    }
}
