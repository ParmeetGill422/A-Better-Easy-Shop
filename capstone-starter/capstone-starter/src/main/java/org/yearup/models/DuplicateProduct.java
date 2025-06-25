package org.yearup.models;

public class DuplicateProduct {
    private int productId;
    private String name;

    public DuplicateProduct(int productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    // getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

