
let userService;

class UserService {

    currentUser = {};

    constructor() {
        this.loadUser();
    }

    _getAuthHeader() {
        return this.currentUser.token
            ? { Authorization: `Bearer ${this.currentUser.token}` }
            : {};
    }

    getHeaders() {
        return { 'Content-Type': 'application/json', ...this._getAuthHeader() };
    }

    saveUser(user) {
        this.currentUser = {
            token:    user.token,
            userId:   user.user.id,
            username: user.user.username,
            role:     user.user.authorities[0].name
        };
        localStorage.setItem('user', JSON.stringify(this.currentUser));
    }

    loadUser() {
        const raw = localStorage.getItem('user');
        if (raw) {
            this.currentUser = JSON.parse(raw);
            axios.defaults.headers.common = this._getAuthHeader();
        }
    }

    getUserName()    { return this.isLoggedIn() ? this.currentUser.username : ''; }
    isLoggedIn()     { return this.currentUser.token !== undefined; }
    getCurrentUser() { return this.currentUser; }

    setHeaderLogin() {
        templateBuilder.build('header', {
            username:  this.getUserName(),
            loggedin:  this.isLoggedIn(),
            loggedout: !this.isLoggedIn()
        }, 'header-user');
    }

    login(username, password) {
        axios.post(`${config.baseUrl}/login`, { username, password })
            .then(response => {
                this.saveUser(response.data);
                axios.defaults.headers.common = this._getAuthHeader();
                this.setHeaderLogin();
                productService.enableButtons();
                cartService.loadCart();
            })
            .catch(() => showError('Login failed. Check your username and password.'));
    }

    register(username, password, confirm) {
        axios.post(`${config.baseUrl}/register`, {
            username,
            password,
            confirmPassword: confirm,
            role: 'USER'
        })
            .then(() => showMessage('Account created! You can now sign in.'))
            .catch(() => showError('Registration failed. Username may already be taken.'));
    }

    logout() {
        localStorage.removeItem('user');
        delete axios.defaults.headers.common['Authorization'];
        this.currentUser = {};
        this.setHeaderLogin();
        productService.enableButtons();
        cartService.cart = { items: [], total: 0 };
        cartService.updateCartDisplay();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    userService = new UserService();
    userService.setHeaderLogin();
});
