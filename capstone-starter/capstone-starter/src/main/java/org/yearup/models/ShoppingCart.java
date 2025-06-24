package org.yearup.models;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCart {
    private List<CartItem> items;
    private BigDecimal total;

    public ShoppingCart(List<CartItem> items) {
        this.items = items;
        this.total = calculateTotal();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        this.total = calculateTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    private BigDecimal calculateTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem item : items) {
            sum = sum.add(item.getLineTotal());
        }
        return sum;
    }
}
