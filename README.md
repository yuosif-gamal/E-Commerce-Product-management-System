# E-Commerce management System

## Overview

This E-Commerce application is online shopping platform built using Spring Boot. It allows users to browse products,
manage carts, apply vouchers, and make purchases. The application includes robust authentication and authorization
mechanisms, along with various features for filtering and sorting products.

## Microservices Architecture

The E-Commerce application is designed using a microservices architecture, consisting of the following services:

* **API Gateway:**
    * **Role:** The API Gateway is the entry point for all client requests. It routes the requests to the appropriate
      microservice and handles cross-cutting concerns like security, logging, and rate limiting.
    * **Technology:** Spring Cloud Gateway.
* **Notification Service:**

    * **Role:** Handles sending notifications to users, such as email notifications when item still in cart more than 2
      hours is marked as not reserved.
    * **Technology:** Spring Boot with JavaMailSender for sending emails.

* **E-commerce Service:**
    * **Role:** Central service managing core e-commerce functionalities, including products, categories, and carts. It
      coordinates product and cart management, handles user roles and access control, and interacts with other services
      via the API Gateway.
    * **Technology:** Spring Boot with Spring Data JPA for database interactions, Spring Security for authentication and
      authorization.

## installations

### Prerequisites

* Java 21
* Maven
* MySql

### Steps

1. **Clone the Repository**
    clone all microservices
   ```bash
   git clone https://github.com/yuosif-gamal/E-Commerce-Product-management-System.git

   git clone https://github.com/yuosif-gamal/Notification.git
   
   git clone https://github.com/yuosif-gamal/API-Gateway.git
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
   
   cd Notification
   mvn clean install
   
   cd API-Gateway
   mvn clean install
4. **Run the Project**
   * for each service.
      ```
       .\mvnw spring-boot:run

5. **Swagger/OpenAPI** </br>
   and for testing  **endpoints** open a web browser and navigate to http://localhost:8080/swagger-ui.html

## Technologies Used

- **Java 21**: The programming language used for developing the application.
- **Spring Boot 3**: The framework used to build the application, providing features for dependency injection,
  configuration, and more.
- **Spring Data JPA (Hibernate)**: Used for data access and ORM (Object-Relational Mapping).
- **Spring Security**: [Provides authentication and authorization for securing the application.](https://docs.google.com/document/d/1WVqrsbo5-NaU8YUyhgyKr20ZCQZeZ2ieGTLZiRLYnCc/edit#heading=h.foorz6d7lthh)
- **Spring Web**: Used for building web applications and RESTful APIs.
- **Spring Cache**: Implements caching strategies to improve application performance.
- **Spring Validation**: Handles validation of user input and data.
- **Spring Testing (Unit Test)**: Used for writing and running unit tests to ensure code quality.
- **Spring Logging (log4j)**: Manages application logging using log4j for logging events.
- **Spring Cloud:** Provides a set of tools for building and managing microservices, including API Gateway.
- **Spring AOP (Aspect-Oriented Programming):** provides a mechanism for separating cross-cutting concerns such as logging and performance monitoring from the core business logic.
- **Swagger/OpenAPI 3**: Provides API documentation and testing capabilities.
- **MySQL Database**: The primary relational database management system used.
- **H2 Database**: An in-memory database used for development and testing purposes.
- **Maven**: The build automation tool used for dependency management and project builds.
- **Email (SMTP)**: Implements email functionality using SMTP for sending notifications and alerts.

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
    * user can subscribe and unsubscribe the notification.
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
* **Scheduler for Cart Item Expiration**
    * In this project, a scheduler has been implemented to manage cart item expiration. The scheduler periodically
      checks for cart items that have not been reserved within a specified time frame (e.g., 2 hours) and marks them as
      not reserved.
    * **Key Features:**
        * **Automatic Expiration:** Cart items that have not been reserved within 2 hours are automatically marked as
          NOT_RESERVED.
        * **Product Quantity Restoration:** When an item expires, the quantity of the associated product is restored to
          its original state.
* **Email Notification:**
    * the project includes an email notification system that sends notifications to users when certain events occur,
      such as when a cart item is marked as NOT_RESERVED because he reserved from 2 hours ago.
    * also include email regeneration when he complete register .
    * implemented using Spring Boot's _**JavaMailSender**_ to interact with an SMTP server and **_HTML_** for emails templates .
* **Logging Aspect** 
    * To enhance observability and debugging, a Logging Aspect has been implemented using Aspect-Oriented Programming (AOP).
    * This aspect provides comprehensive logging for method executions across all service classes.


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