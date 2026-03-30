
// ── Auth ──────────────────────────────────────────────────────────────────────

function showLoginForm() {
    templateBuilder.build('login-form', {}, 'login');
}

function hideModalForm() {
    templateBuilder.clear('login');
}

function switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.auth-pane').forEach(p => p.classList.remove('active'));
    document.querySelector(`.auth-tab[data-tab="${tab}"]`).classList.add('active');
    document.getElementById(`${tab}-pane`).classList.add('active');
}

function login() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    if (!username || !password) { showError('Please enter your username and password.'); return; }
    userService.login(username, password);
    hideModalForm();
}

function register() {
    const username = document.getElementById('reg-username').value.trim();
    const password = document.getElementById('reg-password').value;
    const confirm  = document.getElementById('reg-confirm').value;
    if (!username || !password) { showError('Please fill in all fields.'); return; }
    if (password !== confirm)   { showError('Passwords do not match.'); return; }
    userService.register(username, password, confirm);
    hideModalForm();
}

// ── Navigation ────────────────────────────────────────────────────────────────

function showImageDetailForm(product, imageUrl) {
    templateBuilder.build('image-detail', { name: product, imageUrl }, 'login');
}

// Fix race condition: search and load categories only after 'home' template is
// injected into the DOM, ensuring #content and #category-select exist.
function loadHome() {
    templateBuilder.build('home', {}, 'main', () => {
        productService.search();
        categoryService.getAllCategories(loadCategories);
    });
}

// ── Profile ───────────────────────────────────────────────────────────────────

function editProfile() {
    profileService.loadProfile();
}

function saveProfile() {
    const profile = {
        firstName: document.getElementById('firstName').value,
        lastName:  document.getElementById('lastName').value,
        phone:     document.getElementById('phone').value,
        email:     document.getElementById('email').value,
        address:   document.getElementById('address').value,
        city:      document.getElementById('city').value,
        state:     document.getElementById('state').value,
        zip:       document.getElementById('zip').value
    };
    profileService.updateProfile(profile);
}

// ── Cart ──────────────────────────────────────────────────────────────────────

function showCart() {
    cartService.loadCartPage();
}

// ── Filters ───────────────────────────────────────────────────────────────────

function setCategory(control) {
    productService.addCategoryFilter(control.value);
    productService.search();
}

function setColor(control) {
    productService.addColorFilter(control.value);
    productService.search();
}

// Unified price-slider handler. Sentinel is the "no filter" default value
// for each slider (0 for min, 1500 for max). Debounced to avoid firing an
// API request on every drag tick.
const _debouncedSearch = debounce(() => productService.search(), 350);

function _setPriceFilter(control, displayId, sentinel, filterFn) {
    document.getElementById(displayId).innerText = `$${control.value}`;
    filterFn(control.value != sentinel ? control.value : '');
    _debouncedSearch();
}

function setMinPrice(control) {
    _setPriceFilter(control, 'min-price-display', 0, v => productService.addMinPriceFilter(v));
}

function setMaxPrice(control) {
    _setPriceFilter(control, 'max-price-display', 1500, v => productService.addMaxPriceFilter(v));
}

// ── Bootstrap ─────────────────────────────────────────────────────────────────

document.addEventListener('DOMContentLoaded', () => {
    loadHome();
});
