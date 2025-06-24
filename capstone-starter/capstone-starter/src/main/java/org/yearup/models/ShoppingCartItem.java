package org.yearup.models;

import java.math.BigDecimal;

public class ShoppingCartItem {
    private Product product;
    private int quantity;
    private int discountPercent;
    private BigDecimal lineTotal;

    public ShoppingCartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.discountPercent = 0;
        this.lineTotal = product.getPrice().multiply(new BigDecimal(quantity));
    }

    // Getters and setters

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lineTotal = product.getPrice().multiply(new BigDecimal(quantity));
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
