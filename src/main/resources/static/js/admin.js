function openPanel(evt, panelName) {
    var i, panelContent, tabButtons;
    
    panelContent = document.getElementsByClassName("panel-content");
    for (i = 0; i < panelContent.length; i++) {
        panelContent[i].style.display = "none";
    }
    
    tabButtons = document.getElementsByClassName("tab-button");
    for (i = 0; i < tabButtons.length; i++) {
        tabButtons[i].className = tabButtons[i].className.replace(" active", "");
    }
    
    document.getElementById(panelName).style.display = "block";
    evt.currentTarget.className += " active";
}

document.addEventListener('DOMContentLoaded', function() {
    const addProductForm = document.getElementById('addProductForm');
    if (addProductForm) {
        addProductForm.addEventListener('submit', function(e) {
            e.preventDefault();
            createProduct();
        });
    }
    
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
            loadProducts();
            localStorage.setItem('adminActiveTab', 'productManagement');
            window.location.reload();
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
    fetch(`/api/products/${productId}`)
    .then(response => response.json())
    .then(product => {
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
                    localStorage.setItem('adminActiveTab', 'productManagement');
                    window.location.reload();
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
                localStorage.setItem('adminActiveTab', 'productManagement');
                window.location.reload();
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

function updateStock(productId) {
    const stockInput = document.getElementById(`stock-input-${productId}`);
    const newStock = parseInt(stockInput.value);
    
    if (isNaN(newStock) || newStock < 0) {
        alert('Please enter a valid stock number');
        return;
    }
    
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
                loadProducts();
                localStorage.setItem('adminActiveTab', 'inventoryManagement');
                window.location.reload();
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

function updateOrderStatus(orderId, newStatus) {
    if (!newStatus) {
        alert('Please select a valid status');
        return;
    }
    
    const updateData = {
        orderId: orderId,
        newStatus: newStatus,
        reason: "Admin status update"
    };

    fetch('/api/admin/update-order-status', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.type === 'SUCCESS') {
            const statusSelect = document.querySelector(`select[data-order-id="${orderId}"]`);
            const originalBackground = statusSelect.style.backgroundColor;
            statusSelect.style.backgroundColor = '#d4edda';
            statusSelect.style.color = '#155724';
            
            setTimeout(() => {
                statusSelect.style.backgroundColor = originalBackground;
                statusSelect.style.color = 'black';
            }, 1000);
            
            statusSelect.setAttribute('data-current-status', newStatus);
            localStorage.setItem('adminActiveTab', 'viewAllOrders');
            window.location.reload();
        } else {
            alert('Failed to update order status: ' + (data.message || 'Unknown error.'));
            const statusSelect = document.querySelector(`select[data-order-id="${orderId}"]`);
            const currentStatus = statusSelect.getAttribute('data-current-status');
            statusSelect.value = currentStatus;
        }
    })
    .catch(error => {
        console.error('Network error during order status update:', error);
        alert('A network error occurred. Please try again.');
        const statusSelect = document.querySelector(`select[data-order-id="${orderId}"]`);
        const currentStatus = statusSelect.getAttribute('data-current-status');
        statusSelect.value = currentStatus;
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const statusSelects = document.querySelectorAll('.status-select');
    console.log('Found status selects:', statusSelects.length);
    
    statusSelects.forEach(select => {
        console.log('Adding event listener to select:', select.dataset.orderId);
        select.addEventListener('change', function(event) {
            event.preventDefault();
            const orderId = parseInt(this.dataset.orderId);
            const newStatus = this.value;
            const currentStatus = this.getAttribute('data-current-status');
            
            console.log('Status change detected:', orderId, currentStatus, '->', newStatus);
            
            if (newStatus !== currentStatus) {
                updateOrderStatus(orderId, newStatus);
            }
        });
    });
}); 