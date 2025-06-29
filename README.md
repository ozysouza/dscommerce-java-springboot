# 🛒 DSCommerce - E-commerce REST API

This is a backend RESTful API for an e-commerce platform built using **Java Spring Boot**. It provides core features like user management, product browsing, order creation, and payment processing.

---

## 📚 Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA / Hibernate
- Bean Validation (Jakarta Validation API)
- H2 Database (for test)
- PostgreSQL (for development)
- Maven
- JUnit (for testing, to be implemented) 

---

## 🧱 Domain Model

Below is the database structure for the application:

**DSCommerce ER Diagram**
![dscommerce-table](https://github.com/user-attachments/assets/7d3742c0-0288-4e44-8223-f6194ef2e5c7)

### Entity Overview

- **User**  
  Contains personal information and authentication credentials. A user can place multiple orders.

- **Order**  
  Represents a purchase made by a user. Contains a status (e.g., PAID, SHIPPED) and can have an associated payment.

- **OrderItem**  
  Represents an item within an order. Connects a product to the order with quantity and price.

- **Product**  
  Items available in the store. Each product can belong to one or more categories.

- **Category**  
  Used to classify products.

- **Payment**  
  Represents a payment related to a specific order.

- **OrderStatus (Enum)**  
  Values include:
  - `WAITING_PAYMENT`
  - `PAID`
  - `SHIPPED`
  - `DELIVERED`
  - `CANCELED`

---

## 🔐 Authentication

This API uses **JWT** for stateless authentication.

### How it works:

- Users authenticate via `/auth/login`
- A token is returned in the response
- Use this token in the `Authorization` header as `Bearer <token>` for all secured endpoints
- Roles are used to restrict access (e.g., `ROLE_ADMIN`, `ROLE_CLIENT`)

--

## 🔍 Query Parameters
📄 Pagination Support
This API supports pagination for endpoints that return large lists (e.g., products, categories). It follows Spring Data’s standard pagination mechanism.

You can customize the paginated responses using the following query parameters:

## Parameter	Description	Example
- page	Page number (0-based index)	?page=0
- size	Number of items per page	?size=10
- sort	Sorting criteria in the format `asc	desc`
- You can chain multiple sort params	?sort=name,asc&sort=price,desc


## 🚀 Getting Started

### Prerequisites

- Java 17
- Maven
- (Optional) PostgreSQL if not using the embedded H2 database

## ▶️ How to Run the Project
### Clone the Repository

```bash
git clone https://github.com/ozysouza/dscommerce-java-springboot.git
cd dscommerce-java-springboot
```
```bash
./mvnw spring-boot:run
```

### H2 Console
You can access the H2 in-memory database at:

- URL: http://localhost:8081/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (leave blank)

### Postman Collection 📬
You can use the Postman Collection to test and explore the available endpoints.

To use it:
- Download Postman Collection on root file DScommerce.postman_collection.json
- Open Postman
- Import the collection file
- Set your environment variables or replace token placeholders manually
  
![postmancollection](https://github.com/user-attachments/assets/eb3cc69c-0d03-41cb-9b89-b0d5f5a2d653)


### Work in Progress 🚧
The following features are still in progress:

 <s>- Create/Delete/Update Categories</s>
 - Create/Delete/Update Users
 - Delete/Update Orders

 - Integration Tests using Spring Boot Test

 - API Automation Testing using Playwright

 - Swagger/OpenAPI Documentation



