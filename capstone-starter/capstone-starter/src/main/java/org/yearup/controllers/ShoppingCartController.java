package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart getCart(Principal principal) {
        String username = principal.getName();
        // Use the correct method name
        User user = userDao.getByUserName(username);
        List<ShoppingCartItem> items = shoppingCartDao.getCartByUserId(user.getId());
        return new ShoppingCart(items);
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")

    public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal) {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        shoppingCartDao.addProductToCart(userId, productId);
        return new ShoppingCart(shoppingCartDao.getCartByUserId(userId));
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")

    public ShoppingCart updateProductQuantity(@PathVariable int productId, @RequestBody Map<String, Integer> body, Principal principal) {
        int quantity = body.get("quantity");
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        shoppingCartDao.updateProductQuantity(userId, productId, quantity);
        return new ShoppingCart(shoppingCartDao.getCartByUserId(userId));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")

    public ShoppingCart clearCart(Principal principal) {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        shoppingCartDao.clearCart(userId);
        return new ShoppingCart(shoppingCartDao.getCartByUserId(userId));
    }

}
