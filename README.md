# E-Commerce management System
## Overview

A Java-based E-Commerce application built using Spring Boot. This system provides a platform to manage products, categories, carts, and user roles with various levels of access control.
## installations

### Prerequisites
* Java 17
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

- **Java 17**
- **Spring Boot**
- **Spring Data JPA** (Hibernate)
- **Spring Security**
- **Spring Web**
- **Spring Cache**
- **Spring Validation**
- **Swagger/OpenAPI 3**
- **MySQL Database**
- **H2 Database**
- **Maven**

## Contact
- Name : Yousif Gamal
- Email : yuosifgamal8@gmail.com