let categoryService;

class CategoryService {

    _cached = null;

    getAllCategories(callback) {
        if (this._cached) {
            callback(this._cached);
            return;
        }

        axios.get(`${config.baseUrl}/categories`)
            .then(response => {
                this._cached = response.data;
                callback(this._cached);
            })
            .catch(() => showError('Loading categories failed.'));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    categoryService = new CategoryService();
});
