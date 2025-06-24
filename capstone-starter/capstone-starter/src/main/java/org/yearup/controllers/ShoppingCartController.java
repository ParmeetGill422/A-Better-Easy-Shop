package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import java.util.List;
import java.util.Map;
import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {

    private final UserDao userDao;
    private final ShoppingCartDao shoppingCartDao;

    @Autowired
    public ShoppingCartController(UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUsername(username);
        List<CartItem> items = shoppingCartDao.getCartByUserId(user.getUserId());
        // Calculate total and return structured cart JSON
        return new ShoppingCart(items);
    }

    @PostMapping("/products/{productId}")
    public void addProductToCart(@PathVariable int productId, Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUsername(username);
        shoppingCartDao.addProductToCart(user.getUserId(), productId);
    }

    @PutMapping("/products/{productId}")
    public void updateProductQuantity(@PathVariable int productId, @RequestBody Map<String, Integer> body, Principal principal) {
        int quantity = body.get("quantity");
        String username = principal.getName();
        User user = userDao.getByUsername(username);
        shoppingCartDao.updateProductQuantity(user.getUserId(), productId, quantity);
    }

    @DeleteMapping
    public void clearCart(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUsername(username);
        shoppingCartDao.clearCart(user.getUserId());
    }
}
