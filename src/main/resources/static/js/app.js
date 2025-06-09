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

            const itemIndex = event.target.dataset.index;
            const url = '/api/add-to-cart';
            const data = { index: itemIndex, quantity: 1, category: currentCategory };

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
                    alert(`Item ${itemIndex} added to cart!`);
                } else {
                    const errorData = await response.json();
                    alert('Failed to add item to cart. Please try again.');
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

            const itemIndex = button.getAttribute('data-index');
            const category = button.getAttribute('data-category');

            const data = {
                index: parseInt(itemIndex),
                category: category
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
                        window.location.reload();  // Refresh cart
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
        if (!fullName || !address || !email || !mobileNumber) {
            alert('Please fill in all shipping details.');
            return;
        }

        const orderData = {
            shippingDetails: {
                fullName: fullName,
                address: address,
                email: email,
                mobileNumber: mobileNumber
            }
        };

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
                window.location.href = '/account/orders'; // Redirect to an orders page
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