let productService;

class ProductService {

    photos = new Set();

    filter = {
        cat:      undefined,
        minPrice: undefined,
        maxPrice: undefined,
        color:    undefined,
    };

    constructor() {
        axios.get('/images/products/photos.json')
            .then(response => { this.photos = new Set(response.data); });
    }

    hasPhoto(photo) {
        return this.photos.has(photo);
    }

    _queryString() {
        const params = new URLSearchParams();
        if (this.filter.cat)      params.set('cat',      this.filter.cat);
        if (this.filter.minPrice) params.set('minPrice', this.filter.minPrice);
        if (this.filter.maxPrice) params.set('maxPrice', this.filter.maxPrice);
        if (this.filter.color)    params.set('color',    this.filter.color);
        const qs = params.toString();
        return qs ? `?${qs}` : '';
    }

    addCategoryFilter(cat)    { this.filter.cat      = cat  == 0   ? undefined : cat; }
    addMinPriceFilter(price)  { this.filter.minPrice = price == '' ? undefined : price; }
    addMaxPriceFilter(price)  { this.filter.maxPrice = price == '' ? undefined : price; }
    addColorFilter(color)     { this.filter.color    = color == '' ? undefined : color; }

    search() {
        const url = `${config.baseUrl}/products${this._queryString()}`;

        axios.get(url)
            .then(response => {
                const products = response.data.map(p => ({
                    ...p,
                    imageUrl: this.hasPhoto(p.imageUrl) ? p.imageUrl : 'no-image.jpg'
                }));
                templateBuilder.build('product', { products }, 'content', () => this.enableButtons());
            })
            .catch(() => showError('Searching products failed.'));
    }

    enableButtons() {
        const loggedIn = userService.isLoggedIn();
        document.querySelectorAll('.add-button').forEach(btn => {
            btn.classList.toggle('invisible', !loggedIn);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    productService = new ProductService();
});
