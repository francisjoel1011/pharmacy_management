# Pharmacy Inventory Management System

A full-stack web application built with **Spring Boot** (backend) and **HTML/CSS/Bootstrap/JavaScript** (frontend) to manage pharmacy medicine inventory.

---

## Tech Stack

| Layer     | Technology                              |
|-----------|-----------------------------------------|
| Backend   | Java 17, Spring Boot 3.2, Spring Data JPA, Maven |
| Database  | MySQL 8+                                |
| Frontend  | HTML5, CSS3, Bootstrap 5, Vanilla JS (ES6) |

---

## Prerequisites

Ensure the following are installed:

- Java 17 or higher
- Maven 3.8+
- MySQL 8+
- A modern browser (Chrome, Firefox, Edge)

---

## Setup Instructions

### Step 1 — MySQL Database Setup

Open MySQL Workbench or CLI and run:

```sql
CREATE DATABASE pharmacy_inventory;
```

> The tables will be created automatically by Spring Boot (ddl-auto=update).

---

### Step 2 — Configure Database Password

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

### Step 3 — Build and Run the Backend

```bash
cd pharmacy-inventory
mvn clean install
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

---

### Step 4 — Open the Frontend

Open any of the following files directly in your browser:

- `frontend/index.html` — Medicine Dashboard
- `frontend/add-medicine.html` — Add New Medicine

> **Tip:** Use VS Code Live Server extension for best experience (avoids CORS issues with file://).

---

## API Endpoints

| Method | URL                                  | Description            |
|--------|--------------------------------------|------------------------|
| POST   | /api/medicines                       | Add new medicine       |
| GET    | /api/medicines                       | Get all medicines      |
| GET    | /api/medicines/{id}                  | Get medicine by ID     |
| PUT    | /api/medicines/{id}                  | Update medicine        |
| DELETE | /api/medicines/{id}                  | Delete medicine        |
| GET    | /api/medicines/search?name=para      | Search by name         |
| GET    | /api/medicines/category/Analgesic    | Filter by category     |

---

## Postman Test Examples

### Add Medicine (POST)
```
POST http://localhost:8080/api/medicines
Content-Type: application/json

{
  "medicineName": "Paracetamol 500mg",
  "category": "Analgesic",
  "manufacturer": "ABC Pharma",
  "price": 5.99,
  "stockQuantity": 100,
  "expiryDate": "2026-12-31"
}
```

### Update Medicine (PUT)
```
PUT http://localhost:8080/api/medicines/1
Content-Type: application/json

{
  "medicineName": "Paracetamol 500mg",
  "category": "Analgesic",
  "manufacturer": "ABC Pharma",
  "price": 6.50,
  "stockQuantity": 80,
  "expiryDate": "2027-06-30"
}
```

### Search by Name
```
GET http://localhost:8080/api/medicines/search?name=para
```

### Filter by Category
```
GET http://localhost:8080/api/medicines/category/Antibiotic
```

### Delete Medicine
```
DELETE http://localhost:8080/api/medicines/1
```

---

## Frontend Features

- **Dashboard** with 4 stat cards (total, low stock, expired, categories)
- **Medicine table** with search and category filter
- **Expired row highlight**: rows turn RED for expired medicines
- **Add Medicine** form with client-side + server-side validation
- **Edit Medicine** modal popup
- **Delete** with confirmation dialog
- **Toast notifications** for success/error feedback

---

## Project Structure

```
pharmacy-inventory/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/pharmacy/inventory/
│       │   ├── PharmacyInventoryApplication.java
│       │   ├── config/CorsConfig.java
│       │   ├── controller/MedicineController.java
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── ResourceNotFoundException.java
│       │   ├── model/Medicine.java
│       │   ├── repository/MedicineRepository.java
│       │   └── service/
│       │       ├── MedicineService.java
│       │       └── MedicineServiceImpl.java
│       └── resources/
│           └── application.properties
└── frontend/
    ├── index.html
    ├── add-medicine.html
    ├── style.css
    └── app.js
```
