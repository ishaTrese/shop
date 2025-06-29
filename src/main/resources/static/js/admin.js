// Admin Panel Management
function openPanel(evt, panelName) {
    var i, panelContent, tabButtons;
    
    // Hide all panel content
    panelContent = document.getElementsByClassName("panel-content");
    for (i = 0; i < panelContent.length; i++) {
        panelContent[i].style.display = "none";
    }
    
    // Remove active class from all tab buttons
    tabButtons = document.getElementsByClassName("tab-button");
    for (i = 0; i < tabButtons.length; i++) {
        tabButtons[i].className = tabButtons[i].className.replace(" active", "");
    }
    
    // Show the selected panel and add active class to the button
    document.getElementById(panelName).style.display = "block";
    evt.currentTarget.className += " active";
}

// Product Management Functions
document.addEventListener('DOMContentLoaded', function() {
    // Add Product Form Handler
    const addProductForm = document.getElementById('addProductForm');
    if (addProductForm) {
        addProductForm.addEventListener('submit', function(e) {
            e.preventDefault();
            createProduct();
        });
    }
    
    // Load products on page load
    loadProducts();
});

function createProduct() {
    const formData = new FormData(document.getElementById('addProductForm'));
    const productData = {
        name: formData.get('name'),
        description: formData.get('description'),
        category: formData.get('category'),
        stock: parseInt(formData.get('stock')),
        price: parseFloat(formData.get('price')),
        imageUrl: formData.get('imageUrl') || ''
    };
    
    fetch('/api/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.type === 'SUCCESS') {
            alert('Product created successfully!');
            document.getElementById('addProductForm').reset();
            loadProducts(); // Refresh the product list
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating product');
    });
}

function loadProducts() {
    fetch('/api/products')
    .then(response => response.json())
    .then(products => {
        const tbody = document.getElementById('productTableBody');
        if (tbody) {
            tbody.innerHTML = '';
            products.forEach(product => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.category}</td>
                    <td>₱${parseFloat(product.price).toFixed(2)}</td>
                    <td>${product.stock}</td>
                    <td class="action-buttons">
                        <button onclick="editProduct(${product.id})" class="btn-edit">Edit</button>
                        <button onclick="deleteProduct(${product.id})" class="btn-delete">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        }
    })
    .catch(error => {
        console.error('Error loading products:', error);
    });
}

function editProduct(productId) {
    // Fetch product details and populate a modal or form
    fetch(`/api/products/${productId}`)
    .then(response => response.json())
    .then(product => {
        // For now, we'll use a simple prompt approach
        // In a real application, you'd want a proper modal
        const newName = prompt('Product Name:', product.name);
        const newDescription = prompt('Description:', product.description);
        const newCategory = prompt('Category (Bracelets/Earrings/Necklaces/Rings):', product.category);
        const newPrice = prompt('Price:', product.price);
        const newStock = prompt('Stock:', product.stock);
        
        if (newName && newDescription && newCategory && newPrice && newStock) {
            const updatedProduct = {
                name: newName,
                description: newDescription,
                category: newCategory,
                price: parseFloat(newPrice),
                stock: parseInt(newStock),
                imageUrl: product.imageUrl || ''
            };
            
            fetch(`/api/products/${productId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedProduct)
            })
            .then(response => response.json())
            .then(data => {
                if (data.type === 'SUCCESS') {
                    alert('Product updated successfully!');
                    loadProducts();
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error updating product');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error loading product details');
    });
}

function deleteProduct(productId) {
    if (confirm('Are you sure you want to delete this product?')) {
        fetch(`/api/products/${productId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.type === 'SUCCESS') {
                alert('Product deleted successfully!');
                loadProducts();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting product');
        });
    }
}

// Inventory Management Functions
function updateStock(productId) {
    const stockInput = document.getElementById(`stock-input-${productId}`);
    const newStock = parseInt(stockInput.value);
    
    if (isNaN(newStock) || newStock < 0) {
        alert('Please enter a valid stock number');
        return;
    }
    
    // First, get the product details to find category and name
    fetch(`/api/products/${productId}`)
    .then(response => response.json())
    .then(product => {
        const requestBody = {
            category: product.category,
            productName: product.name,
            new_stock: newStock
        };
        
        fetch('/api/inventory/update-stock', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        })
        .then(response => response.json())
        .then(data => {
            if (data.type === 'SUCCESS') {
                alert('Stock updated successfully!');
                loadProducts(); // Refresh the product list
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error updating stock');
        });
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error loading product details');
    });
} 