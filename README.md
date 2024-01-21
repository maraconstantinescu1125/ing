# Spring Boot Application with MySQL Database

This is a Spring Boot application that uses MySQL database and includes three controllers for managing categories, products, and stock.

## Table of Contents

- [Setup](#setup)
- [Controllers](#controllers)
  - [Category Controller](#category-controller)
  - [Product Controller](#product-controller)
  - [Stock Controller](#stock-controller)
  - [User Details](#user-details)

## Setup

### Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 Development Kit (JDK)
- Maven
- MySQL

### Configuration

1. Configure MySQL database properties in `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password


2. Build the application:

    ```bash
    mvn clean install

3. Run the application
     ```bash
     mvn spring-boot:run


## Controllers

### Category Controller

**Get Category by ID**

  ```http
  GET /api/categories/{categoryId}
```
    
Retrieve a category by its id. 

**Get All Categories**

```http
GET /api/categories
```

Retrieve all categories.


**Add Category**
```http
POST /api/categories
```
 Add a new category (requires ADMIN role).


**Update Category**
```http
PUT /api/categories/{categoryId}
```
Update a category by its ID (requires ADMIN role).


**Delete Category**
```http
DELETE /api/categories/{categoryId}
```
Delete a category by its ID (requires ADMIN role).

### Product Controller

**Add Product**
```http
POST /api/products
```
Add a new product (requires ADMIN role).

**Get Product by ID**
```http
GET /api/products/{productId}
````
Retrieve a product by its ID.



**Get All Products**
```http
GET /api/products
```
Retrieve all products.

**Delete Product**
```http
DELETE /api/products/{productId}
```
Delete a product by its ID (requires ADMIN role).

**Change Product Price**
```http
PATCH /api/products/{productId}/change-price/{newPrice}
```
Change the price of a product by its ID (requires ADMIN role).


**Get Products by Category**
```http
GET /api/products/{categoryId}/products
```
Retrieve products by category ID.

**Get Products by Producer**
```http
GET /api/products/producer/{producer}
```
Retrieve products by producer.

**Get Products by Price Range**
```http
GET /api/products/price-range/{minPrice}/{maxPrice}
```
Retrieve products within a specified price range.

### Stock Controller

**Get Stock for Product**
```http
GET /api/stock/product/{productId}
```
Retrieve stock information for a product(requires authentication).

**Add Stock for Product**
```http
POST /api/stock
```
Add stock for a product(requires authentication).

**Update Quantity for Product**
```http
PATCH /api/stock/update-quantity/{productId}/{newQuantity}
```
Update the quantity of stock for a product(requires authentication).

### User Details

To configure user details for authentication, the application provides an in-memory UserDetailsService with two predefined users:

- User: mara

  **Username: mara**
  
  **Password: mara123**
  
  Roles: **USER**

- Admin: admin

  **Username: admin**
  
  **Password: admin**
  
  Roles: **ADMIN**


      
