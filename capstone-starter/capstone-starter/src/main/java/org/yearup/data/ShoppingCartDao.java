package org.yearup.data;

import org.yearup.models.CartItem;

import java.util.List;

public interface ShoppingCartDao {
    List<CartItem> getCartByUserId(int userId);
    void addProductToCart(int userId, int productId);
    void updateProductQuantity(int userId, int productId, int quantity);
    void clearCart(int userId);
}
