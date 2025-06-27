package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private Map<Integer, ShoppingCartItem> items = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;


    public ShoppingCart(List<ShoppingCartItem> itemsList) {
        for (ShoppingCartItem item : itemsList) {
            items.put(item.getProduct().getProductId(), item);
            total = total.add(item.getLineTotal());
        }
    }

    public Map<Integer, ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
