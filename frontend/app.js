/**
 * ================================================================
 *  Pharmacy Inventory Management System - Frontend JavaScript
 *  Stack: Vanilla JS (ES6+), Fetch API, async/await
 * ================================================================
 */

"use strict";

// ─── Configuration ────────────────────────────────────────────────────────────

/** Base URL for all REST API calls */
const API_BASE_URL = "http://localhost:8080/api/medicines";

/** Predefined categories for the dropdown filter */
const CATEGORIES = [
    "Analgesic", "Antibiotic", "Antifungal", "Antiviral",
    "Antacid", "Antihistamine", "Antiseptic", "Cardiovascular",
    "Dermatological", "Diabetes", "Vitamin", "Other"
];

// ─── Utility Functions ────────────────────────────────────────────────────────

/**
 * Checks whether a medicine has expired.
 * @param {string} expiryDateStr - ISO date string (YYYY-MM-DD)
 * @returns {boolean} true if the medicine is expired
 */
function isExpired(expiryDateStr) {
    if (!expiryDateStr) return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0); // compare dates only, not time
    const expiryDate = new Date(expiryDateStr);
    return expiryDate < today;
}

/**
 * Formats a date string for display (DD/MM/YYYY).
 * @param {string} dateStr - ISO date string
 * @returns {string} formatted date or 'N/A'
 */
function formatDate(dateStr) {
    if (!dateStr) return "N/A";
    const date = new Date(dateStr);
    return date.toLocaleDateString("en-GB"); // DD/MM/YYYY
}

/**
 * Formats currency for display.
 * @param {number} amount
 * @returns {string} formatted currency string
 */
function formatCurrency(amount) {
    return "₱" + parseFloat(amount).toFixed(2);
}

/**
 * Returns a Bootstrap badge based on stock quantity.
 * @param {number} qty
 * @returns {string} HTML badge string
 */
function getStockBadge(qty) {
    if (qty <= 0)  return `<span class="stock-badge stock-critical">Out of Stock</span>`;
    if (qty <= 10) return `<span class="stock-badge stock-low">${qty} (Low)</span>`;
    return `<span class="stock-badge stock-ok">${qty}</span>`;
}

// ─── Toast Notification ───────────────────────────────────────────────────────

/**
 * Shows a Bootstrap toast notification.
 * @param {string} message - message to display
 * @param {'success'|'danger'|'warning'|'info'} type - Bootstrap color type
 */
function showToast(message, type = "success") {
    const container = document.getElementById("toastContainer");
    if (!container) return;

    const toastId = "toast-" + Date.now();
    const icons = { success: "check-circle", danger: "x-circle", warning: "exclamation-triangle", info: "info-circle" };
    const icon  = icons[type] || "info-circle";

    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type} border-0 mb-2" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-${icon} me-2"></i>${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>`;
    container.insertAdjacentHTML("beforeend", toastHTML);

    const toastEl = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastEl, { delay: 4000 });
    bsToast.show();

    // Remove from DOM after hidden
    toastEl.addEventListener("hidden.bs.toast", () => toastEl.remove());
}

// ─── API Functions (Fetch API with async/await) ───────────────────────────────

/**
 * Fetches all medicines from the REST API.
 * GET /api/medicines
 * @returns {Promise<Array>} array of medicine objects
 */
async function fetchAllMedicines() {
    const response = await fetch(API_BASE_URL);
    if (!response.ok) throw new Error("Failed to fetch medicines");
    return await response.json();
}

/**
 * Fetches a single medicine by ID.
 * GET /api/medicines/{id}
 * @param {number} id
 * @returns {Promise<Object>} medicine object
 */
async function fetchMedicineById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    if (!response.ok) throw new Error(`Medicine with ID ${id} not found`);
    return await response.json();
}

/**
 * Creates a new medicine.
 * POST /api/medicines
 * @param {Object} medicineData - medicine payload
 * @returns {Promise<Object>} created medicine object
 */
async function createMedicine(medicineData) {
    const response = await fetch(API_BASE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(medicineData)
    });

    const data = await response.json();
    if (!response.ok) {
        // Extract validation errors if present
        const errors = data.details
            ? Object.values(data.details).join(", ")
            : data.message || "Failed to create medicine";
        throw new Error(errors);
    }
    return data;
}

/**
 * Updates an existing medicine.
 * PUT /api/medicines/{id}
 * @param {number} id
 * @param {Object} medicineData - updated medicine payload
 * @returns {Promise<Object>} updated medicine object
 */
async function updateMedicine(id, medicineData) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(medicineData)
    });

    const data = await response.json();
    if (!response.ok) {
        const errors = data.details
            ? Object.values(data.details).join(", ")
            : data.message || "Failed to update medicine";
        throw new Error(errors);
    }
    return data;
}

/**
 * Deletes a medicine by ID.
 * DELETE /api/medicines/{id}
 * @param {number} id
 */
async function deleteMedicineById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
        const data = await response.json();
        throw new Error(data.message || "Failed to delete medicine");
    }
}

/**
 * Searches medicines by name.
 * GET /api/medicines/search?name=
 * @param {string} name
 * @returns {Promise<Array>}
 */
async function searchMedicinesByName(name) {
    const response = await fetch(`${API_BASE_URL}/search?name=${encodeURIComponent(name)}`);
    if (!response.ok) throw new Error("Search failed");
    return await response.json();
}

/**
 * Filters medicines by category.
 * GET /api/medicines/category/{category}
 * @param {string} category
 * @returns {Promise<Array>}
 */
async function filterByCategory(category) {
    const response = await fetch(`${API_BASE_URL}/category/${encodeURIComponent(category)}`);
    if (!response.ok) throw new Error("Filter failed");
    return await response.json();
}

// ─── Table Rendering ──────────────────────────────────────────────────────────

/**
 * Renders the medicines array into the HTML table.
 * Applies red row highlight for expired medicines.
 * @param {Array} medicines - array of medicine objects from API
 */
function renderMedicinesTable(medicines) {
    const tbody    = document.getElementById("medicineTableBody");
    const emptyEl  = document.getElementById("emptyState");
    const countEl  = document.getElementById("resultCount");

    if (!tbody) return;

    // Update result count badge
    if (countEl) countEl.textContent = medicines.length;

    if (medicines.length === 0) {
        tbody.innerHTML = "";
        if (emptyEl) emptyEl.style.display = "block";
        return;
    }

    if (emptyEl) emptyEl.style.display = "none";

    tbody.innerHTML = medicines.map(med => {
        const expired     = isExpired(med.expiryDate);
        const rowClass    = expired ? "expired-row" : "";
        const expiredBadge= expired
            ? `<span class="badge bg-dark ms-1" title="This medicine has expired">EXPIRED</span>`
            : "";

        return `
            <tr class="${rowClass}" data-id="${med.medicineId}">
                <td><strong>${escapeHtml(med.medicineName)}</strong>${expiredBadge}</td>
                <td><span class="category-badge">${escapeHtml(med.category)}</span></td>
                <td>${escapeHtml(med.manufacturer || "—")}</td>
                <td><strong>${formatCurrency(med.price)}</strong></td>
                <td>${getStockBadge(med.stockQuantity)}</td>
                <td>${formatDate(med.expiryDate)}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary btn-action me-1"
                            onclick="openEditModal(${med.medicineId})"
                            title="Edit">
                        <i class="bi bi-pencil-square"></i> Edit
                    </button>
                    <button class="btn btn-sm btn-outline-danger btn-action"
                            onclick="confirmDelete(${med.medicineId}, '${escapeHtml(med.medicineName)}')"
                            title="Delete">
                        <i class="bi bi-trash3"></i> Delete
                    </button>
                </td>
            </tr>`;
    }).join("");
}

/**
 * Escapes HTML special characters to prevent XSS.
 * @param {string} str
 * @returns {string}
 */
function escapeHtml(str) {
    if (!str) return "";
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

// ─── Dashboard Stats ──────────────────────────────────────────────────────────

/**
 * Updates the 4 stat cards at the top of the dashboard.
 * @param {Array} medicines
 */
function updateDashboardStats(medicines) {
    const totalEl      = document.getElementById("totalCount");
    const expiredEl    = document.getElementById("expiredCount");
    const lowStockEl   = document.getElementById("lowStockCount");
    const categoriesEl = document.getElementById("categoriesCount");

    if (totalEl)      totalEl.textContent      = medicines.length;
    if (expiredEl)    expiredEl.textContent     = medicines.filter(m => isExpired(m.expiryDate)).length;
    if (lowStockEl)   lowStockEl.textContent    = medicines.filter(m => m.stockQuantity <= 10).length;
    if (categoriesEl) categoriesEl.textContent  = new Set(medicines.map(m => m.category)).size;
}

// ─── Load All Medicines (Main function) ───────────────────────────────────────

/**
 * Loads all medicines from the API and renders them.
 * Called on page load and after any CRUD operation.
 */
async function loadMedicines() {
    const spinner = document.getElementById("loadingSpinner");
    if (spinner) spinner.style.display = "block";

    try {
        const medicines = await fetchAllMedicines();
        renderMedicinesTable(medicines);
        updateDashboardStats(medicines);
        populateCategoryFilter(medicines);
    } catch (error) {
        console.error("Error loading medicines:", error);
        showToast("Failed to load medicines. Is the backend running?", "danger");
    } finally {
        if (spinner) spinner.style.display = "none";
    }
}

// ─── Search & Filter ──────────────────────────────────────────────────────────

/**
 * Handles the search input event.
 * Debounced to avoid firing on every keystroke.
 */
let searchDebounceTimer;
async function handleSearch(event) {
    clearTimeout(searchDebounceTimer);
    const query = event.target.value.trim();

    searchDebounceTimer = setTimeout(async () => {
        try {
            if (query.length === 0) {
                await loadMedicines(); // reset to all
            } else {
                const results = await searchMedicinesByName(query);
                renderMedicinesTable(results);
            }
        } catch (error) {
            showToast("Search failed: " + error.message, "danger");
        }
    }, 350); // 350ms debounce
}

/**
 * Handles category filter change event.
 */
async function handleCategoryFilter(event) {
    const category = event.target.value;
    try {
        if (!category) {
            await loadMedicines();
        } else {
            const results = await filterByCategory(category);
            renderMedicinesTable(results);
        }
    } catch (error) {
        showToast("Filter failed: " + error.message, "danger");
    }
}

/**
 * Populates the category filter dropdown with distinct categories.
 * @param {Array} medicines
 */
function populateCategoryFilter(medicines) {
    const select = document.getElementById("categoryFilter");
    if (!select) return;

    const currentVal = select.value;
    const categories = [...new Set(medicines.map(m => m.category))].sort();

    // Clear existing options except the first "All Categories" option
    while (select.options.length > 1) select.remove(1);

    categories.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat;
        option.textContent = cat;
        select.appendChild(option);
    });

    select.value = currentVal; // preserve selection
}

// ─── Add/Edit Medicine Form Validation ────────────────────────────────────────

/**
 * Validates the medicine form fields.
 * Returns true if valid, false otherwise.
 * @param {HTMLFormElement} form
 * @returns {boolean}
 */
function validateMedicineForm(form) {
    let isValid = true;

    // Clear previous errors
    form.querySelectorAll(".is-invalid").forEach(el => el.classList.remove("is-invalid"));

    const name  = form.querySelector("#medicineName") || form.querySelector("#editMedicineName");
    const cat   = form.querySelector("#category")     || form.querySelector("#editCategory");
    const price = form.querySelector("#price")        || form.querySelector("#editPrice");
    const stock = form.querySelector("#stockQuantity") || form.querySelector("#editStockQuantity");

    if (name && !name.value.trim()) {
        name.classList.add("is-invalid");
        isValid = false;
    }
    if (cat && !cat.value.trim()) {
        cat.classList.add("is-invalid");
        isValid = false;
    }
    if (price && (!price.value || parseFloat(price.value) <= 0)) {
        price.classList.add("is-invalid");
        isValid = false;
    }
    if (stock && (!stock.value || parseInt(stock.value) <= 0)) {
        stock.classList.add("is-invalid");
        isValid = false;
    }

    return isValid;
}

/**
 * Reads form field values and builds a medicine payload object.
 * @param {HTMLFormElement} form
 * @param {string} prefix - field ID prefix ("" for add form, "edit" for modal)
 * @returns {Object} medicine data
 */
function buildMedicinePayload(form, prefix = "") {
    const get = (id) => {
        const el = form.querySelector(`#${prefix}${id}`);
        return el ? el.value.trim() : "";
    };

    const payload = {
        medicineName:  get("medicineName") || get("MedicineName"),
        category:      get("category")     || get("Category"),
        manufacturer:  get("manufacturer") || get("Manufacturer"),
        price:         parseFloat(get("price") || get("Price")) || null,
        stockQuantity: parseInt(get("stockQuantity") || get("StockQuantity")) || null,
        expiryDate:    get("expiryDate")   || get("ExpiryDate") || null
    };

    // Remove null expiryDate (optional field)
    if (!payload.expiryDate) delete payload.expiryDate;

    return payload;
}

// ─── Edit Modal ───────────────────────────────────────────────────────────────

/**
 * Opens the edit modal, pre-populated with the medicine's current data.
 * @param {number} id - Medicine ID to edit
 */
async function openEditModal(id) {
    try {
        const med = await fetchMedicineById(id);
        const modal = document.getElementById("editModal");
        if (!modal) return;

        // Fill modal fields
        modal.querySelector("#editMedicineId").value    = med.medicineId;
        modal.querySelector("#editMedicineName").value  = med.medicineName;
        modal.querySelector("#editCategory").value      = med.category;
        modal.querySelector("#editManufacturer").value  = med.manufacturer || "";
        modal.querySelector("#editPrice").value         = med.price;
        modal.querySelector("#editStockQuantity").value = med.stockQuantity;
        modal.querySelector("#editExpiryDate").value    = med.expiryDate || "";

        // Show modal using Bootstrap JS API
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();
    } catch (error) {
        showToast("Failed to load medicine: " + error.message, "danger");
    }
}

/**
 * Handles the edit form submission inside the modal.
 */
async function handleEditSubmit(event) {
    event.preventDefault();
    const form = event.target;
    if (!validateMedicineForm(form)) return;

    const id = form.querySelector("#editMedicineId").value;
    const payload = {
        medicineName:  form.querySelector("#editMedicineName").value.trim(),
        category:      form.querySelector("#editCategory").value.trim(),
        manufacturer:  form.querySelector("#editManufacturer").value.trim(),
        price:         parseFloat(form.querySelector("#editPrice").value),
        stockQuantity: parseInt(form.querySelector("#editStockQuantity").value),
        expiryDate:    form.querySelector("#editExpiryDate").value || null
    };

    try {
        await updateMedicine(id, payload);
        showToast("Medicine updated successfully!", "success");

        // Close modal
        const modal = bootstrap.Modal.getInstance(document.getElementById("editModal"));
        if (modal) modal.hide();

        await loadMedicines(); // Refresh table
    } catch (error) {
        showToast("Update failed: " + error.message, "danger");
    }
}

// ─── Delete Medicine ──────────────────────────────────────────────────────────

/**
 * Shows a confirmation dialog before deleting.
 * @param {number} id
 * @param {string} name
 */
function confirmDelete(id, name) {
    if (confirm(`Are you sure you want to delete "${name}"?\n\nThis action cannot be undone.`)) {
        performDelete(id, name);
    }
}

/**
 * Performs the actual delete API call.
 * @param {number} id
 * @param {string} name
 */
async function performDelete(id, name) {
    try {
        await deleteMedicineById(id);
        showToast(`"${name}" deleted successfully!`, "success");
        await loadMedicines(); // Refresh table
    } catch (error) {
        showToast("Delete failed: " + error.message, "danger");
    }
}

// ─── Add Medicine Form (add-medicine.html) ────────────────────────────────────

/**
 * Handles the Add Medicine form submission.
 * @param {Event} event
 */
async function handleAddMedicine(event) {
    event.preventDefault();
    const form = event.target;
    if (!validateMedicineForm(form)) {
        showToast("Please fill in all required fields correctly.", "warning");
        return;
    }

    const payload = {
        medicineName:  form.querySelector("#medicineName").value.trim(),
        category:      form.querySelector("#category").value.trim(),
        manufacturer:  form.querySelector("#manufacturer").value.trim(),
        price:         parseFloat(form.querySelector("#price").value),
        stockQuantity: parseInt(form.querySelector("#stockQuantity").value),
        expiryDate:    form.querySelector("#expiryDate").value || null
    };

    const submitBtn = form.querySelector("#submitBtn");
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span>Saving...`;
    }

    try {
        await createMedicine(payload);
        showToast("Medicine added successfully!", "success");
        form.reset();
        // Redirect to dashboard after short delay
        setTimeout(() => { window.location.href = "index.html"; }, 1500);
    } catch (error) {
        showToast("Failed to add medicine: " + error.message, "danger");
    } finally {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = `<i class="bi bi-save me-2"></i>Save Medicine`;
        }
    }
}

// ─── Populate Category Dropdowns on Forms ─────────────────────────────────────

/**
 * Dynamically populates category <select> elements with predefined options.
 */
function populateCategoryDropdowns() {
    const selects = document.querySelectorAll(".category-select");
    selects.forEach(select => {
        CATEGORIES.forEach(cat => {
            const option = document.createElement("option");
            option.value = cat;
            option.textContent = cat;
            select.appendChild(option);
        });
    });
}

// ─── Page Initialization ──────────────────────────────────────────────────────

/**
 * Initializes the current page based on which elements are present.
 * Called when the DOM is fully loaded.
 */
document.addEventListener("DOMContentLoaded", function () {
    // Populate category dropdowns on any page that has them
    populateCategoryDropdowns();

    // ── Dashboard / Medicine List Page (index.html) ──
    if (document.getElementById("medicineTableBody")) {
        loadMedicines();

        // Wire up search input
        const searchInput = document.getElementById("searchInput");
        if (searchInput) searchInput.addEventListener("input", handleSearch);

        // Wire up category filter
        const catFilter = document.getElementById("categoryFilter");
        if (catFilter) catFilter.addEventListener("change", handleCategoryFilter);

        // Wire up edit modal form
        const editForm = document.getElementById("editMedicineForm");
        if (editForm) editForm.addEventListener("submit", handleEditSubmit);

        // Wire up "Reset Filters" button
        const resetBtn = document.getElementById("resetFilters");
        if (resetBtn) resetBtn.addEventListener("click", () => {
            const si = document.getElementById("searchInput");
            const cf = document.getElementById("categoryFilter");
            if (si) si.value = "";
            if (cf) cf.value = "";
            loadMedicines();
        });
    }

    // ── Add Medicine Page (add-medicine.html) ──
    const addForm = document.getElementById("addMedicineForm");
    if (addForm) {
        addForm.addEventListener("submit", handleAddMedicine);
    }
});
