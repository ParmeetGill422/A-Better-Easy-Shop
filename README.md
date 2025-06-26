<h1>A Better Easy Shop</h1>

![BETTER SHOP](https://github.com/user-attachments/assets/73cd1a5e-1745-4911-b65c-4ed88a4b1f15)

---

This  is e-commerce web application that enables users to browse, select, and purchase products online with ease. It includes secure user authentication and role-based authorization, allowing different levels of access for regular customers and administrators. Users can filter products by category and price, manage their shopping cart by adding or removing items.

The backend API is developed with Java Spring Boot, providing robust RESTful endpoints secured by JWT tokens. The frontend is built using JavaScript with Axios for API communication, delivering a smooth and responsive user interface.

---

## Features
- 🔐 **User Authentication & Authorization**
  
Secure login and registration with JWT token-based authentication. Role-based access control distinguishes regular users and administrators.

- 🛍️ **Product Catalog**

Users can browse and search products by category, price range, and other filters.

- 🛒 **Shopping Cart**

Logged-in users can add products to a persistent cart, update quantities, and clear the cart.

- 📦 **Order Management**

Users can place orders from their cart. Administrators can manage products and orders.

- 🌐 **RESTful API**

Backend exposes REST endpoints for frontend consumption, with Spring Security enforcing access restrictions.

- 💾 **Database Integration**

Data persistence handled via MySQL, including user data, products, carts, and orders.

- 🖥️ **Frontend**

Responsive user interface using vanilla JavaScript, Axios for API calls, and Bootstrap for styling.

---

## 📸 Screenshots

### 🏠 Home Screen 

![Home screeen](https://github.com/user-attachments/assets/dd030cae-de10-473f-b713-4b009383c0bc)

### 🛒 Cart Screen

![Cart screen](https://github.com/user-attachments/assets/1bd9543f-df1f-418d-8b28-5ffa5fdf9379)

### 📬 Postman

![postman screen](https://github.com/user-attachments/assets/e2b74b72-5643-48d4-a25a-e56bb7e402ae)

### 🧍‍♂️ Login

![Login screen](https://github.com/user-attachments/assets/7e44c99d-82fc-41cb-9129-7a19115b308e)

### 🎭 Profile

![Profile screen](https://github.com/user-attachments/assets/32f8c43e-59c5-4a5a-b673-d1f5441e41c3)

---

## 🔍 Interesting Code
```java
@PostMapping("/cart/products/{productId}")
public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal) {
    if (principal == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    String username = principal.getName();
    User user = userDao.getByUserName(username);

    shoppingCartDao.addProductToCart(user.getId(), productId);

    return new ShoppingCart(shoppingCartDao.getCartByUserId(user.getId()));
}
```
## Why it’s cool:

- It uses Spring Security’s Principal object to securely identify the logged-in user without needing extra input.

- It prevents unauthorized access by throwing a 401 Unauthorized if the user is not logged in.

- The method then adds the specified product to that user’s cart in the database.

- Finally, it returns the updated cart so the frontend can immediately show the latest state.
