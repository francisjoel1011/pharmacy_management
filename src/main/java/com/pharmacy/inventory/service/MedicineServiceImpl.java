package com.pharmacy.inventory.service;

import com.pharmacy.inventory.exception.ResourceNotFoundException;
import com.pharmacy.inventory.model.Medicine;
import com.pharmacy.inventory.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MedicineServiceImpl - Business Logic Implementation.
 *
 * @Service marks this as a Spring-managed service bean.
 *
 * This class:
 *   - Implements all MedicineService interface methods
 *   - Delegates database operations to MedicineRepository
 *   - Applies business rules (e.g., update field mapping)
 *   - Throws ResourceNotFoundException when entity is not found
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    /** Repository for database CRUD operations */
    private final MedicineRepository medicineRepository;

    /**
     * Constructor-based dependency injection (preferred over @Autowired on field).
     * Makes the dependency explicit and supports immutability.
     */
    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    /**
     * Saves a new medicine record to the database.
     * @PrePersist in Medicine entity auto-sets createdDateTime.
     */
    @Override
    public Medicine addMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    /**
     * Retrieves all medicines ordered by database default.
     */
    @Override
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    /**
     * Finds a medicine by its primary key.
     * Throws ResourceNotFoundException (mapped to HTTP 404) if not found.
     */
    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with ID: " + id));
    }

    /**
     * Updates an existing medicine by ID.
     * Fetches existing record, applies new values, saves back.
     */
    @Override
    public Medicine updateMedicine(Long id, Medicine medicineDetails) {
        // Fetch the existing medicine (throws 404 if missing)
        Medicine existingMedicine = getMedicineById(id);

        // Apply updates from the request body
        existingMedicine.setMedicineName(medicineDetails.getMedicineName());
        existingMedicine.setCategory(medicineDetails.getCategory());
        existingMedicine.setManufacturer(medicineDetails.getManufacturer());
        existingMedicine.setPrice(medicineDetails.getPrice());
        existingMedicine.setStockQuantity(medicineDetails.getStockQuantity());
        existingMedicine.setExpiryDate(medicineDetails.getExpiryDate());

        // Note: createdDateTime is NOT updated (updatable = false in @Column)
        return medicineRepository.save(existingMedicine);
    }

    /**
     * Deletes a medicine by its ID.
     * Validates existence first to return proper 404 if not found.
     */
    @Override
    public void deleteMedicine(Long id) {
        // Validate existence (throws 404 if missing)
        getMedicineById(id);
        medicineRepository.deleteById(id);
    }

    /**
     * Case-insensitive partial name search.
     */
    @Override
    public List<Medicine> searchByName(String name) {
        return medicineRepository.findByMedicineNameContainingIgnoreCase(name);
    }

    /**
     * Case-insensitive exact category filter.
     */
    @Override
    public List<Medicine> getByCategory(String category) {
        return medicineRepository.findByCategoryIgnoreCase(category);
    }
}
