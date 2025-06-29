const signup_btn = document.querySelector('button.signup-btn');

if (signup_btn) {
    signup_btn.addEventListener('click', async function(event) {
        event.preventDefault();

        const firstName = document.getElementById('first-name').value;
        const lastName = document.getElementById('last-name').value;
        const email = document.getElementById('email').value;
        const mobileNumber = document.getElementById('mobile').value;
        const buildingStreet = document.getElementById('street').value;
        const barangayDistrict = document.getElementById('barangay').value;
        const cityMunicipality = document.getElementById('city').value;
        const postalCode = document.getElementById('postal-code').value;
        const password = document.getElementById('signup-password').value;
        const confirmPassword = document.getElementById('signup-password-confirm').value;

        if (password !== confirmPassword) {
            alert('Passwords do not match. Please re-enter.');
            return;
        }

        if (firstName.trim() === '' ||
            lastName.trim() === '' ||
            email.trim() === '' ||
            mobileNumber.trim() === '' ||
            buildingStreet.trim() === '' ||
            barangayDistrict.trim() === '' ||
            cityMunicipality.trim() === '' ||
            postalCode.trim() === '' ||
            password.trim() === '') {
            alert('All fields are required. Please provide all information.');
            return;
        }

        const userData = {
            first_name: firstName,
            last_name: lastName,
            email: email,
            mobile_number: mobileNumber,
            building_street: buildingStreet,
            barangay_district: barangayDistrict,
            city_municipality: cityMunicipality,
            postal_code: postalCode,
            password: password
        };

        try {
            const response = await fetch('/api/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Signup successful!', result);
                alert('Sign up successful! Welcome.');
                window.location.href = '/account/login';
            } else {
                const errorData = await response.json();
                if (errorData.message && typeof errorData.message === 'string' && errorData.message.toLowerCase().includes('duplicate')) {
                    alert('Email is already taken. Please use a different email.');
                } else {
                    alert('Sign up failed: ' + (errorData.message || 'Unknown error.'));
                }
            }
        } catch (error) {
            console.error('Network error during signup:', error);
            alert('A network error occurred. Please try again.');
        }
    });
}

const loginForm = document.querySelector('button.login-btn-01');

if (loginForm) {
    loginForm.addEventListener('click', async function(event) {
        event.preventDefault();

        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value.trim();

        if (!email || !password) {
            alert('Please enter both email and password.');
            return;
        }

        const loginData = {
            email: email,
            password: password
        };

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Login successful!', result);
                window.location.href = '/account';
            } else {
                const errorData = await response.json();
                console.error('Login failed:', response.status, errorData);
                alert('Login failed: ' + (errorData.message || 'Invalid credentials.'));
            }
        } catch (error) {
            console.error('Network error during login:', error);
            alert('A network error occurred. Please try again.');
        }
    });
}

const logoutBtn = document.querySelector('a.logout-btn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', async function(event) {
        event.preventDefault();
        const messageData = {
            message: "logout"
        };
        try {
            const response = await fetch('/api/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(messageData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Logout successful!', result);
                window.location.href = '/home';
            } else {
                const errorData = await response.json();
                alert("Unable to logout!")
            }
        } catch (error) {
            console.error('Network error during login:', error);
            alert('A network error occurred. Please try again.');
        }
    });
}

const productAddToCart = document.querySelectorAll('button.add-to-cart-btn');
if (productAddToCart) {
    productAddToCart.forEach(button => {
        button.addEventListener('click', async function(event) {
            event.preventDefault();
            
            const productIdElement = document.querySelector('[data-product-id]');
            if (!productIdElement) {
                alert('Product information not found. Please refresh the page.');
                return;
            }
            
            const productId = parseInt(productIdElement.dataset.productId);
            const quantity = document.querySelector('input.quantity-input');
            const data = { productId: productId, quantity: parseInt(quantity.value) };

            try {
                const response = await fetch('/api/add-to-cart', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    alert(`Item added to cart!`);
                } else {
                    const errorData = await response.json();
                    alert('Failed to add item to cart: ' + (errorData.message || 'Unknown error.'));
                }
            } catch (error) {
                alert('A network error occurred. Please try again.');
            }
        });
    });
}

const addToCartButtons = document.querySelectorAll('button.add-to-cart');

function getCategoryFromUrl() {
    const pathName = window.location.pathname;
    const segments = pathName.split('/');
    const filteredSegments = segments.filter(segment => segment !== '');

    if (filteredSegments.length > 0) {
        return filteredSegments[filteredSegments.length - 1];
    }

    return null;
}

const currentCategory = getCategoryFromUrl();
if (addToCartButtons) {
    addToCartButtons.forEach(button => {
        button.addEventListener('click', async function(event) {
            event.preventDefault();

            const productId = event.target.dataset.productId;
            const url = '/api/add-to-cart';
            const data = { productId: parseInt(productId), quantity: 1 };

            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    alert(`Item added to cart!`);
                } else {
                    const errorData = await response.json();
                    alert('Failed to add item to cart: ' + (errorData.message || 'Unknown error.'));
                }
            } catch (error) {
                alert('A network error occurred. Please try again.');
            }
        });
    });
}

const removeFromCartButtons = document.querySelectorAll('button.remove-from-cart-btn');

if (removeFromCartButtons) {
    removeFromCartButtons.forEach(button => {
        button.addEventListener('click', async function(event) {
            event.preventDefault();

            const productId = button.getAttribute('data-product-id');

            const data = {
                productId: parseInt(productId)
            };

            try {
                const response = await fetch('/api/remove-from-cart', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.type === 'SUCCESS') {
                        alert(result.message || 'Item removed.');
                        window.location.reload();
                    } else {
                        alert('Error: ' + (result.message || 'Failed to remove item.'));
                    }
                } else {
                    const errorData = await response.json();
                    alert('Remove failed: ' + (errorData.message || 'Server error.'));
                }
            } catch (error) {
                console.error('Remove error:', error);
                alert('A network error occurred. Please try again.');
            }
        });
    });
}

const placeOrderBtn = document.querySelector('button.checkout-place-order');

if (placeOrderBtn) {
    placeOrderBtn.addEventListener('click', async function(event) {
        event.preventDefault();

        const fullName = document.getElementById('shipping-full-name').value.trim();
        const address = document.getElementById('shipping-address').value.trim();
        const email = document.getElementById('shipping-email').value.trim();
        const mobileNumber = document.getElementById('shipping-mobile_number').value.trim();
        const cardNumber = document.getElementById('card-number').value.trim();
        const cardExpiry = document.getElementById('card-expiry').value.trim();
        const cardCvv = document.getElementById('card-cvv').value.trim();
        
        // Get selected payment method
        const selectedPaymentMethod = document.querySelector('input[name="payment-method"]:checked');
        const paymentMethod = selectedPaymentMethod ? selectedPaymentMethod.value : 'cod';

        if (!fullName || !address || !email || !mobileNumber) {
            alert('Please fill in all shipping details.');
            return;
        }

        if (paymentMethod === 'card') {
            // NOTE: this is only basic hardcoded validation
            // Constraints:
            //    - card must be prefixed by either 4 or 5
            //    - month should be atleast minimum 06/25
            //    - cvv should be 3 digit
            const cardNumberRegex = /^(4\d{15}|5\d{15})$/;
            if (!cardNumberRegex.test(cardNumber)) {
                alert('Card Number must be 16 digits long and start with 4 or 5.');
                return;
            }

            const expiryRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
            if (!expiryRegex.test(cardExpiry)) {
                alert('Expiry Date must be in MM/YY format (e.g., 01/26).');
                return;
            }

            const [expMonthStr, expYearStr] = cardExpiry.split('/');
            const expMonth = parseInt(expMonthStr, 10);
            const expYear = parseInt('20' + expYearStr, 10);

            const currentDate = new Date();
            const currentMonth = currentDate.getMonth() + 1;
            const currentYear = currentDate.getFullYear();

            if (expMonth === 6 && expYear === 2025) {
                alert('Cards expiring in 06/25 are not accepted.');
                return;
            }

            if (expYear < currentYear || (expYear === currentYear && expMonth < currentMonth)) {
                alert('Expiry Date cannot be in the past.');
                return;
            }

            const cvvRegex = /^\d{3}$/;
            if (!cvvRegex.test(cardCvv)) {
                alert('CVV must be a 3-digit number.');
                return;
            }
        }

        const orderData = {
            shippingDetails: {
                fullName: fullName,
                address: address,
                email: email,
                mobileNumber: mobileNumber
            },
            paymentMethod: paymentMethod
        };

        try {
            const response = await fetch('/api/place-order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Order placed successfully!', result);
                alert('Your order has been placed successfully!');
                window.location.href = '/account/orders';
            } else {
                const errorData = await response.json();
                console.error('Order placement failed:', response.status, errorData);
                alert('Failed to place order: ' + (errorData.message || 'Unknown error.'));
            }
        } catch (error) {
            console.error('Network error during order placement:', error);
            alert('A network error occurred while placing your order. Please try again.');
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const initialTabButton = document.querySelector('.panel-tabs .tab-button.active-tab-button');
    if (initialTabButton) {
        initialTabButton.click();
    } else {
        const firstTabButton = document.querySelector('.panel-tabs .tab-button');
        if (firstTabButton) {
            firstTabButton.click();
        }
    }
});

function openPanel(evt, panelName) {
    let tabContents = document.getElementsByClassName("panel-content");
    for (let i = 0; i < tabContents.length; i++) {
        tabContents[i].style.display = "none";
    }

    let tabButtons = document.getElementsByClassName("tab-button");
    for (let i = 0; i < tabButtons.length; i++) {
        tabButtons[i].classList.remove("active-tab-button");
    }

    document.getElementById(panelName).style.display = "block";

    evt.currentTarget.classList.add("active-tab-button");
}


async function updateStock(productId) {
    const stockInput = document.getElementById('stock-input-' + productId);

    if (!stockInput) {
        alert("Stock input field not found for product ID: " + productId);
        return;
    }

    const newStock = parseInt(stockInput.value, 10);

    if (isNaN(newStock) || newStock < 0) {
        alert("Please enter a valid non-negative number for stock.");
        return;
    }

    const row = stockInput.closest('tr');
    if (!row) {
        alert("Could not find table row for product ID: " + productId);
        return;
    }

    const productNameElement = row.querySelector('.inventory-product-name');
    const categoryElement = row.querySelector('.inventory-category');

    if (!productNameElement || !categoryElement) {
        alert("Could not find product name or category in the row.");
        return;
    }

    const productName = productNameElement.textContent.trim();
    const category = categoryElement.textContent.trim();

    try {
        const response = await fetch('/api/inventory/update-stock', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                category: category,
                productName: productName,
                new_stock: newStock
            })
        });

        const data = await response.json();

        if (response.ok) {
            alert(data.message);
            localStorage.setItem('adminActiveTab', 'inventoryManagement');
            window.location.reload();
        } else {
            alert(data.message || "Failed to update stock. Unknown error.");
        }
    } catch (error) {
        console.error('Error sending update request:', error);
        alert("A network error occurred while communicating with the server. Please try again.");
    }
}

// Function to restore active tab on page load
function restoreActiveTab() {
    const activeTab = localStorage.getItem('adminActiveTab');
    if (activeTab) {
        // Find the tab button and trigger the openPanel function
        const tabButtons = document.querySelectorAll('.tab-button');
        tabButtons.forEach(button => {
            const onclick = button.getAttribute('onclick');
            if (onclick && onclick.includes(activeTab)) {
                // Create a mock event and call openPanel
                const mockEvent = { currentTarget: button };
                openPanel(mockEvent, activeTab);
            }
        });
        // Clear the stored tab after restoring
        localStorage.removeItem('adminActiveTab');
    }
}

// Payment method selection functionality
document.addEventListener('DOMContentLoaded', function() {
    // Restore active tab if we're on admin panel
    if (window.location.pathname.includes('admin-panel')) {
        restoreActiveTab();
    }
    
    const paymentMethods = document.querySelectorAll('input[name="payment-method"]');
    const cardSection = document.getElementById('card-payment-section');
    const cardExpiry = document.getElementById('card-expiry');

    paymentMethods.forEach(method => {
        method.addEventListener('change', function() {
            if (this.value === 'card') {
                cardSection.style.display = 'block';
            } else {
                cardSection.style.display = 'none';
            }
        });
    });

    if (cardExpiry) {
        cardExpiry.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            
            e.target.value = value;
        });
    }
});

// Sort functionality for catalog
document.addEventListener('DOMContentLoaded', function() {
    const sortSelect = document.getElementById('sort');
    
    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            const sortValue = this.value;
            const currentUrl = window.location.pathname;
            
            const url = new URL(window.location.href);
            url.searchParams.set('sort', sortValue);
            
            window.location.href = url.toString();
        });
        
        const urlParams = new URLSearchParams(window.location.search);
        const currentSort = urlParams.get('sort');
        if (currentSort) {
            sortSelect.value = currentSort;
        }
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const searchButton = document.getElementById('searchButton');
    const searchModal = document.getElementById('searchModal');
    const searchResults = document.getElementById('searchResults');
    const searchBackgroundOverlay = document.getElementById('searchBackgroundOverlay');
    const searchInput = document.querySelector('.search-input');
    const closeSearch = document.getElementById('closeSearch');
    let allProducts = [];
    let searchTimeout;

    loadAllProducts();

    if (searchButton) {
        searchButton.addEventListener('click', function() {
            showSearchModal();
        });
    }

    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            
            if (searchTimeout) {
                clearTimeout(searchTimeout);
            }

            searchTimeout = setTimeout(() => {
                if (query.length > 0) {
                    performSearch(query);
                } else {
                    showAllProducts();
                }
            }, 300);
        });

        searchInput.addEventListener('focus', function() {
            if (this.value.trim() === '') {
                showAllProducts();
            }
        });
    }

    if (closeSearch) {
        closeSearch.addEventListener('click', function() {
            hideSearchModal();
        });
    }

    document.addEventListener('click', function(event) {
        if (searchBackgroundOverlay && event.target === searchBackgroundOverlay) {
            hideSearchModal();
        }
    });

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            hideSearchModal();
        }
    });

    async function loadAllProducts() {
        try {
            const response = await fetch('/api/products');
            if (response.ok) {
                allProducts = await response.json();
            }
        } catch (error) {
            console.error('Error loading products:', error);
        }
    }

    function showSearchModal() {
        if (searchModal && searchBackgroundOverlay) {
            searchModal.classList.add('active');
            searchBackgroundOverlay.classList.add('active');
            searchInput.focus();
            showNoResults();
        }
    }

    function hideSearchModal() {
        if (searchModal && searchBackgroundOverlay) {
            searchModal.classList.remove('active');
            searchBackgroundOverlay.classList.remove('active');
            if (searchInput) {
                searchInput.value = '';
            }
        }
    }

    function performSearch(query) {
        const results = allProducts.filter(product => {
            const searchTerm = query.toLowerCase();
            return product.name.toLowerCase().includes(searchTerm) ||
                   product.category.toLowerCase().includes(searchTerm) ||
                   product.description.toLowerCase().includes(searchTerm);
        });

        displaySearchResults(results);
    }

    function showAllProducts() {
        displaySearchResults(allProducts.slice(0, 10));
    }

    function showNoResults() {
        if (searchResults) {
            searchResults.innerHTML = '<div class="no-results">No matches</div>';
        }
    }

    function displaySearchResults(results) {
        if (!searchResults) return;

        if (results.length === 0) {
            searchResults.innerHTML = '<div class="no-results">No products found</div>';
            return;
        }

        const resultsHtml = results.map(product => `
            <div class="search-result-item" onclick="window.location.href='/product/${product.id}'">
                <img src="${product.imageUrl}" alt="${product.name}" class="search-result-image">
                <div class="search-result-info">
                    <div class="search-result-name">${product.name}</div>
                    <div class="search-result-price">₱ ${product.price}</div>
                    <div class="search-result-category">${product.category}</div>
                </div>
            </div>
        `).join('');

        searchResults.innerHTML = resultsHtml;
    }
});
