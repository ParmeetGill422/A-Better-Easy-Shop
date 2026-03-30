let orderService;

class OrderService {

    placeOrder() {
        if (!userService.isLoggedIn()) {
            showError('Please sign in to place an order.');
            return;
        }
        if (cartService.cart.items.length === 0) {
            showError('Your cart is empty.');
            return;
        }

        axios.post(`${config.baseUrl}/orders`)
            .then(response => {
                showMessage(`Order #${response.data.orderId} placed successfully! Thank you.`);
                cartService.loadCart();
                loadHome();
            })
            .catch(() => showError('Failed to place order. Make sure your profile address is filled in.'));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    orderService = new OrderService();
});
