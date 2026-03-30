let cartService;

class ShoppingCartService {

    cart = { items: [], total: 0 };

    setCart(data) {
        this.cart = {
            total: data.total,
            items: Object.values(data.items).map(item => ({
                ...item,
                lineTotal: (item.product.price * item.quantity).toFixed(2)
            }))
        };
    }

    addToCart(productId) {
        axios.post(`${config.baseUrl}/cart/products/${productId}`, {})
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(() => showError('Failed to add item to cart.'));
    }

    loadCart() {
        axios.get(`${config.baseUrl}/cart`)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(() => showError('Failed to load cart.'));
    }

    loadCartPage() {
        const data = {
            ...this.cart,
            isEmpty: this.cart.items.length === 0
        };
        templateBuilder.build('cart', data, 'main');
    }

    updateQuantity(productId, newQuantity) {
        if (newQuantity < 1) return;
        axios.put(`${config.baseUrl}/cart/products/${productId}`, { quantity: newQuantity })
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();
            })
            .catch(() => showError('Failed to update quantity.'));
    }

    clearCart() {
        axios.delete(`${config.baseUrl}/cart`)
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();
            })
            .catch(() => showError('Failed to clear cart.'));
    }

    updateCartDisplay() {
        const el = document.getElementById('cart-items');
        if (el) el.innerText = this.cart.items.length;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();
    if (userService.isLoggedIn()) {
        cartService.loadCart();
    }
});
