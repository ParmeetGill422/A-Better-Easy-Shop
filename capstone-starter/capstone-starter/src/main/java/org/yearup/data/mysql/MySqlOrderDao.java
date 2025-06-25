package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    order.setOrderId(keys.getInt(1));
                    return order;
                }
            }
            throw new SQLException("Creating order failed, no ID obtained.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOrderLineItem(int orderId, int productId, int quantity) {
        String sql = "INSERT INTO order_line_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
