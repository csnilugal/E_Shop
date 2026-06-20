const API_BASE = 'http://localhost:8080';

// ======================================
// SAFE USER PARSE
// ======================================
function getCurrentUser() {

    try {

        const userData =
            localStorage.getItem('user');

        if (
            userData &&
            userData !== 'undefined'
        ) {

            return JSON.parse(userData);
        }

    } catch (e) {

        console.error(
            'Invalid localStorage data'
        );

        localStorage.removeItem('user');
    }

    return null;
}

// ======================================
// API FETCH
// ======================================
async function apiFetch(
    endpoint,
    options = {}
) {

    const user = getCurrentUser();

    const headers = {
        ...options.headers
    };

    // JSON HEADER
    if (!(options.body instanceof FormData)) {

        headers['Content-Type'] =
            headers['Content-Type']
            || 'application/json';
    }

    // USER HEADER
    if (user?.id) {

        headers['X-User-Id'] =
            user.id;
    }

    try {

        const response = await fetch(
            `${API_BASE}${endpoint}`,
            {
                ...options,
                headers
            }
        );

        // HANDLE ERRORS
        if (!response.ok) {

            let errorMessage =
                'API Error';

            try {

                errorMessage =
                    await response.text();

            } catch (e) {}

            throw new Error(
                errorMessage
            );
        }

        // HANDLE JSON
        const contentType =
            response.headers.get(
                'content-type'
            );

        if (
            contentType &&
            contentType.includes(
                'application/json'
            )
        ) {

            return response.json();
        }

        return response.text();

    } catch (err) {

        console.error(
            'API ERROR:',
            err
        );

        throw err;
    }
}

// ======================================
// AUTH CHECK
// ======================================
function checkAuth(
    requireRole = null
) {

    const user =
        getCurrentUser();

    if (!user) {

        window.location.href =
            '/login.html';

        return null;
    }

    // ROLE CHECK
    if (
        requireRole &&
        user.role !== requireRole
    ) {

        if (
            user.role === 'OWNER'
        ) {

            window.location.href =
                '/admin.html';

        } else {

            window.location.href =
                '/products.html';
        }

        return null;
    }

    setupNav(user);

    return user;
}

// ======================================
// NAVBAR
// ======================================
function setupNav(user) {

    const navLinks =
        document.getElementById(
            'navLinks'
        );

    if (!navLinks) return;

    navLinks.innerHTML = '';

    // CUSTOMER NAV
    if (
        user.role === 'CUSTOMER'
    ) {

        navLinks.innerHTML += `
            <a href="/products.html">
                Products
            </a>

            <a href="/cart.html">
                Cart
            </a>

            <a href="/orders.html">
                Orders
            </a>
        `;
    }

    // OWNER NAV
    else if (
        user.role === 'OWNER'
    ) {

        navLinks.innerHTML += `
            <a href="/admin.html">
                Dashboard
            </a>
        `;
    }

    // USER EMAIL
    navLinks.innerHTML += `
        <span class="nav-user">

            👤 ${user.email}

        </span>
    `;

    // LOGOUT BUTTON
    const logoutBtn =
        document.createElement(
            'button'
        );

    logoutBtn.className =
        'btn';

    logoutBtn.textContent =
        'Logout';

    logoutBtn.onclick = logout;

    navLinks.appendChild(
        logoutBtn
    );
}

// ======================================
// LOGOUT
// ======================================
function logout() {

    localStorage.removeItem(
        'user'
    );

    window.location.href =
        '/login.html';
}

// ======================================
// ALERTS
// ======================================
function showAlert(
    message,
    isError = false
) {

    const alertDiv =
        document.getElementById(
            'alertMsg'
        );

    if (!alertDiv) return;

    alertDiv.textContent =
        message;

    alertDiv.className =
        `alert ${
            isError
                ? 'alert-error'
                : 'alert-success'
        }`;

    alertDiv.style.display =
        'block';

    setTimeout(() => {

        alertDiv.style.display =
            'none';

    }, 3000);
}

// ======================================
// FORMAT PRICE
// ======================================
function formatPrice(price) {

    return `₹${Number(price)
        .toFixed(2)}`;
}

// ======================================
// FORMAT DATE
// ======================================
function formatDate(dateString) {

    return new Date(
        dateString
    ).toLocaleString();
}