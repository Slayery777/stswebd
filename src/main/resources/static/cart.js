var cart = {
    items: [],

    load: function() {
        var cookieValue = Cookies.get('cart');
        if (cookieValue) {
            this.items = JSON.parse(cookieValue);
        }
    },

    save: function() {
        var cookieValue = JSON.stringify(this.items);
        Cookies.set('cart', cookieValue);
    },

    add: function(productId, quantity) {
        var item = this.items.find(function(item) {
            return item.productId === productId;
        });
        if (item) {
            item.quantity += quantity;
        } else {
            // Fetch the product information and add it to the cart
            fetch('/product/' + productId)
                .then(response => response.json())
                .then(product => {
                    this.items.push({ productId: productId, quantity: quantity, product: product });
                    this.save();
                });
        }
    },    

    update: function(productId, quantity) {
        var item = this.items.find(function(item) {
            return item.productId === productId;
        });
        if (item) {
            item.quantity = quantity;
            this.save();
        }
    },

    remove: function(productId) {
        var index = this.items.findIndex(function(item) {
            return item.productId === productId;
        });
        if (index !== -1) {
            this.items.splice(index, 1);
            this.save();
        }
    },
    getItems: function() {
        return this.items;
    }
};

cart.load();
