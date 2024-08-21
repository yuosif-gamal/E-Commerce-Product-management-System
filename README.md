# E-Commerce management System

## Overview

This E-Commerce application is online shopping platform built using Spring Boot. It allows users to browse products,
manage carts, apply vouchers, and make purchases. The application includes robust authentication and authorization
mechanisms, along with various features for filtering and sorting products.

## installations

### Prerequisites

* Java 21
* Maven

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yuosif-gamal/E-Commerce-Product-management-System.git

2. **Setup Database connections** </br>

    * Ensure that MySQL is installed and running, and that the database schema is created.
    * You can configure the database connection by modifying the src/main/resources/application.properties file. </br>

        * spring.datasource.url=jdbc:mysql://localhost:3306/your_schema_name
        * spring.datasource.password=your_password
        * spring.datasource.username=your_username
3. **Build the Project**
    ```
   cd E-Commerce-Product-management-System
   mvn clean install
4. **Run the Project**
   ```
    .\mvnw spring-boot:run

5. **Swagger/OpenAPI** </br>
   and for testing  **endpoints** open a web browser and navigate to http://localhost:8080/swagger-ui.html

## Technologies Used

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA** (Hibernate)
- **Spring Security**
- **Spring Web**
- **Spring Cache**
- **Spring Validation**
- **Spring Testing** (Unit Test)
- **Spring Logging** (log4j)
- **Swagger/OpenAPI 3**
- **MySQL Database**
- **H2 Database**
- **Maven**

## Features

This E-Commerce application includes the following features:

* **Product Management**
    * Create, update, view, and delete products with details such as name, description, price, and category.
    * Apply vouchers or discounts to products dynamically.
    * **Product Filtering**
        * The application supports dynamic product filtering based on price range, name, and quantity using a flexible
          filter object and the Spring Data JPA `Specification` interface.
* **Category Management**
    * Manage product categories and subcategories.
    * Retrieve hierarchical category structures with parent-child relationships.
* **Cart Management**
    * Add, update, or remove items in the shopping cart.
    * Automatically update cart total based on current product prices.
    * Apply vouchers or discounts to cart items.
* **User Management**
    * Manage user roles with different levels of access control.
    * Handle user registration, authentication, and authorization using Spring Security.
* **Access Control by User Roles**
    * Ensure only authorized users can access specific features based on their roles.
* **Caching**
    * Improve application performance by caching frequently accessed data such as products, categories, and cart items.
* **API Documentation**
    * Utilize Swagger/OpenAPI for interactive API documentation, making it easy to test and understand available
      endpoints.
* **Logging:**
    * **Detailed Logging:** Added logging throughout the application to track input parameters, processing steps, and
      outcomes.
    * **Log Monitoring:** Logs provide insights into system operations, making it easier to debug and monitor.
* **Error Handling**
    * **Global Exception Handling:** A centralized mechanism for handling exceptions, providing meaningful error
      messages to the user.
    * **Custom Exceptions:** Specific exceptions for different error scenarios like resource not found, invalid input,
      etc.

## Testing

The E-Commerce application includes various tests to ensure the system works as expected:

1. **Unit Tests**
    * **JUnit 5:** Used for writing and executing unit tests.
    * **Mockito:** Used for mocking dependencies in unit tests.
2. **Run the Tests**
   ```
    .\mvnw test

## Contact

- Name : Yousif Gamal
- Email : yuosifgamal8@gmail.com