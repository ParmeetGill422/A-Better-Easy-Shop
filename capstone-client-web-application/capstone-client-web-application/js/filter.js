function loadCategories(categories) {
    const select = document.getElementById('category-select');
    // Clear existing options (except the first "Show All") before populating
    // to prevent duplicates when loadHome() is called more than once.
    while (select.options.length > 1) {
        select.remove(1);
    }

    categories.forEach(c => {
        const option = document.createElement('option');
        option.value = c.categoryId;
        option.innerText = c.name;
        select.appendChild(option);
    });
}
