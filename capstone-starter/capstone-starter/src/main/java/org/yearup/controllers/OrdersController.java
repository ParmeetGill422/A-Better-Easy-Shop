package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrdersController {

    private final UserDao userDao;
    private final ShoppingCartDao shoppingCartDao;
    private final OrderDao orderDao;

    @Autowired
    public OrdersController(UserDao userDao, ShoppingCartDao shoppingCartDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
        this.orderDao = orderDao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        List<ShoppingCartItem> cartItems = shoppingCartDao.getCartByUserId(user.getId());
        if (cartItems == null || cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shopping cart is empty");
        }

        // Create order object
        Order order = new Order();
        order.setUserId(user.getId());
        order.setOrderDate(LocalDateTime.now());

        // Save order to get order ID
        Order createdOrder = orderDao.create(order);

        // Add each cart item as order line item
        for (ShoppingCartItem item : cartItems) {
            orderDao.addOrderLineItem(createdOrder.getOrderId(), item.getProduct().getProductId(), item.getQuantity());
        }

        // Clear shopping cart
        shoppingCartDao.clearCart(user.getId());

        return createdOrder;
    }
}
