package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public Order createOrder(Principal principal) {

        String username = principal.getName();
        User user = userDao.getByUserName(username);
        int userId = user.getId();


        List<ShoppingCartItem> cartItems = shoppingCartDao.getCartByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }


        Order order = new Order();
        order.setUserId(user.getId());
        order.setOrderDate(LocalDateTime.now());
        Order createdOrder = orderDao.create(order);


        for (ShoppingCartItem item : cartItems) {
            orderDao.addOrderLineItem(createdOrder.getOrderId(), item.getProduct().getProductId(), item.getQuantity());
        }


        shoppingCartDao.clearCart(user.getId());

        return createdOrder;
    }
}
