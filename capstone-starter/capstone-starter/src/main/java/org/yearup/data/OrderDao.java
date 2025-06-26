package org.yearup.data;

import org.yearup.models.Order;

import java.math.BigDecimal;

public interface OrderDao {
    Order create(Order order);
    void addOrderLineItem(int orderId, int productId, int quantity, BigDecimal price);
}
