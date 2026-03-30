
class TemplateBuilder {

    constructor() {
        this._cache = {};
    }

    _fetch(templateName) {
        if (this._cache[templateName]) {
            return Promise.resolve(this._cache[templateName]);
        }
        return axios.get(`templates/${templateName}.html`).then(response => {
            this._cache[templateName] = response.data;
            return response.data;
        });
    }

    build(templateName, value, target, callback) {
        this._fetch(templateName)
            .then(template => {
                try {
                    document.getElementById(target).innerHTML = Mustache.render(template, value);
                    if (callback) callback();
                } catch (e) {
                    console.error(`TemplateBuilder.build error for '${templateName}':`, e);
                }
            })
            .catch(e => console.error(`Failed to load template '${templateName}':`, e));
    }

    append(templateName, value, target, { ttl } = {}) {
        this._fetch(templateName)
            .then(template => {
                try {
                    const element = this.createElementFromHTML(Mustache.render(template, value));
                    const parent = document.getElementById(target);
                    parent.appendChild(element);
                    if (ttl) setTimeout(() => {
                        if (parent.contains(element)) parent.removeChild(element);
                    }, ttl);
                } catch (e) {
                    console.error(`TemplateBuilder.append error for '${templateName}':`, e);
                }
            })
            .catch(e => console.error(`Failed to load template '${templateName}':`, e));
    }

    clear(target) {
        document.getElementById(target).innerHTML = '';
    }

    createElementFromHTML(htmlString) {
        const div = document.createElement('div');
        div.innerHTML = htmlString.trim();
        return div.firstChild;
    }
}

function showError(message) {
    templateBuilder.append('error', { error: message }, 'errors', { ttl: 3000 });
}

function showMessage(message) {
    templateBuilder.append('message', { message }, 'errors', { ttl: 3000 });
}

function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

const templateBuilder = new TemplateBuilder();
