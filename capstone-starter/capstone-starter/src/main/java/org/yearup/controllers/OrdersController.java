package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrdersController {

    private final UserDao userDao;
    private final ProfileDao profileDao;
    private final ShoppingCartDao shoppingCartDao;
    private final OrderDao orderDao;

    @Autowired
    public OrdersController(
            UserDao userDao,
            ProfileDao profileDao,
            ShoppingCartDao shoppingCartDao,
            OrderDao orderDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.shoppingCartDao = shoppingCartDao;
        this.orderDao = orderDao;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String username = principal.getName();
            User user = userDao.getByUserName(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<ShoppingCartItem> cartItems = shoppingCartDao.getCartByUserId(user.getId());
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Profile profile = profileDao.getByUserId(user.getId());
            if (profile == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Order order = new Order();
            order.setUserId(user.getId());
            order.setDate(LocalDateTime.now());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
            order.setShippingAmount(BigDecimal.ZERO);

            Order createdOrder = orderDao.create(order);

            for (ShoppingCartItem item : cartItems) {
                BigDecimal price = item.getProduct().getPrice();
                orderDao.addOrderLineItem(createdOrder.getOrderId(), item.getProduct().getProductId(), item.getQuantity(), price);
            }

            shoppingCartDao.clearCart(user.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
