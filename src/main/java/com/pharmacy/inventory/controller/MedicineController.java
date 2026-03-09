package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.model.Medicine;
import com.pharmacy.inventory.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MedicineController - REST API Controller.
 *
 * @RestController = @Controller + @ResponseBody
 *   Automatically serializes return values to JSON.
 *
 * @RequestMapping sets the base URL for all endpoints in this controller.
 *
 * @CrossOrigin allows the frontend (served from a different port) to call this API.
 *
 * HTTP Methods used:
 *   POST   - Create a new resource
 *   GET    - Read/retrieve a resource
 *   PUT    - Update an existing resource
 *   DELETE - Remove a resource
 */
@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")  // Allow all origins (configure for production)
public class MedicineController {

    /** Service layer dependency */
    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // ─────────────────────────────────────────────────────────────
    //  POST /api/medicines
    //  Add a new medicine to inventory
    // ─────────────────────────────────────────────────────────────

    /**
     * Creates a new medicine.
     *
     * @Valid triggers Jakarta validation on the request body.
     * Returns HTTP 201 Created with the saved medicine object.
     *
     * Example Request Body:
     * {
     *   "medicineName": "Paracetamol",
     *   "category": "Analgesic",
     *   "manufacturer": "ABC Pharma",
     *   "price": 5.99,
     *   "stockQuantity": 100,
     *   "expiryDate": "2026-12-31"
     * }
     */
    @PostMapping
    public ResponseEntity<Medicine> addMedicine(@Valid @RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineService.addMedicine(medicine);
        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED); // 201
    }

    // ─────────────────────────────────────────────────────────────
    //  GET /api/medicines
    //  Retrieve all medicines
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns a list of all medicines in inventory.
     * HTTP 200 OK with JSON array.
     */
    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        List<Medicine> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(medicines); // 200
    }

    // ─────────────────────────────────────────────────────────────
    //  GET /api/medicines/{id}
    //  Retrieve a specific medicine by ID
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns a single medicine by its ID.
     * Returns HTTP 404 if not found (handled by GlobalExceptionHandler).
     *
     * @param id path variable from URL
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine); // 200
    }

    // ─────────────────────────────────────────────────────────────
    //  PUT /api/medicines/{id}
    //  Update an existing medicine
    // ─────────────────────────────────────────────────────────────

    /**
     * Updates an existing medicine record.
     * Returns updated medicine or HTTP 404 if ID not found.
     *
     * @param id             path variable - ID of medicine to update
     * @param medicineDetails request body with updated fields
     */
    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody Medicine medicineDetails) {

        Medicine updatedMedicine = medicineService.updateMedicine(id, medicineDetails);
        return ResponseEntity.ok(updatedMedicine); // 200
    }

    // ─────────────────────────────────────────────────────────────
    //  DELETE /api/medicines/{id}
    //  Delete a medicine
    // ─────────────────────────────────────────────────────────────

    /**
     * Deletes a medicine from inventory.
     * Returns HTTP 200 with a success message, or HTTP 404 if not found.
     *
     * @param id path variable - ID of medicine to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok("Medicine with ID " + id + " deleted successfully."); // 200
    }

    // ─────────────────────────────────────────────────────────────
    //  GET /api/medicines/search?name=
    //  Search medicines by name
    // ─────────────────────────────────────────────────────────────

    /**
     * Searches medicines by name (case-insensitive partial match).
     * Example: GET /api/medicines/search?name=para
     *
     * @param name query parameter for search
     */
    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> searchByName(@RequestParam String name) {
        List<Medicine> results = medicineService.searchByName(name);
        return ResponseEntity.ok(results); // 200
    }

    // ─────────────────────────────────────────────────────────────
    //  GET /api/medicines/category/{category}
    //  Filter medicines by category
    // ─────────────────────────────────────────────────────────────

    /**
     * Filters and returns medicines by category.
     * Example: GET /api/medicines/category/Antibiotics
     *
     * @param category path variable - category name to filter
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Medicine>> getByCategory(@PathVariable String category) {
        List<Medicine> medicines = medicineService.getByCategory(category);
        return ResponseEntity.ok(medicines); // 200
    }
}
